package org.antelop.poi.imp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class XLSXReader extends DefaultHandler
{
    // 共享字符串表
    private SharedStringsTable mSharedStringsTable;

    // 样式表
    private StylesTable mStyleTable;

    private int mSheetIndex = -1;

    private List<String> mRowlist = new ArrayList<String>();

    // 记住excel中表头对应的列号
    private Map<Integer, String> mColunMapping = null;

    // 当前行的值 key:excel中的表头 value:列值
    private Map<String, String> mRowMap = new HashMap<String, String>();

    // 当前行索引
    private int mRowIndex = 1;

    // 列索引
    private int mCellIndex = 0;

    // 上一个列名索引
    private int mLastColumnIndex = 0;

    // 当前列索引,由A,B,C...转化得到
    private int mColumnIndex;

    // 标题行列数
    private int mSpans = 0;

    private String mCellValue;

    private boolean mIsTElement;

    private final DataFormatter mFormatter = new DataFormatter();

    private short mFormatIndex;

    private String mFormatString;

    private IReader mRowReader;

    private Boolean mIsExistTitle = true;

    /**
     * 单元格中的数据可能的数据类型
     */
    private enum CellDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }

    // 单元格数据类型，默认为字符串类型
    private CellDataType mNextDataType;

    public void setRowReader(IReader rowReader)
    {
        mRowReader = rowReader;
    }

    public void setIsExistTitle(Boolean isExistTitle)
    {
        mIsExistTitle = isExistTitle;
    }

    /**
     * 只遍历一个电子表格,其中sheetId为要遍历的sheet索引,从1开始,1-3
     * 
     * @param fileName
     * @param sheetId
     *            要遍历的sheet索引,从1开始,1-3
     * 
     * @throws Exception
     */
    public void Process(String fileName, int sheetId) throws Exception
    {
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        StylesTable st = r.getStylesTable();
        XMLReader parser = FetchSheetParser(sst, st);

        InputStream sheetStream = r.getSheet("rId" + sheetId);
        InputSource sheetSource = new InputSource(sheetStream);
        mSheetIndex++;

        parser.parse(sheetSource);
        sheetStream.close();
    }

    /**
     * 遍历工作簿中所有的电子表格
     * 
     * @param fileName
     * 
     * @throws Exception
     */
    public void Process(String fileName) throws Exception
    {
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        StylesTable st = r.getStylesTable();
        XMLReader parser = FetchSheetParser(sst, st);

        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            mRowIndex = 1;
            mLastColumnIndex = 0;
            mSheetIndex++;

            InputStream sheetStream = sheets.next();
            InputSource sheetSource = new InputSource(sheetStream);
            parser.parse(sheetSource);

            sheetStream.close();
        }
    }

    public XMLReader FetchSheetParser(SharedStringsTable sst, StylesTable st)
        throws SAXException
    {
        XMLReader parser = XMLReaderFactory
            .createXMLReader("org.apache.xerces.parsers.SAXParser");

        mSharedStringsTable = sst;
        mStyleTable = st;
        parser.setContentHandler(this);

        return parser;
    }

    @Override
    public void startElement(
        String uri,
        String localName,
        String name,
        Attributes attributes) throws SAXException
    {
        if ("c".equals(name)) {

            SetNextDataType(attributes);
            String columnName = attributes.getValue("r");
            columnName = columnName.substring(
                0, (columnName.length() - ("" + mRowIndex).length()));
            mColumnIndex = ChangeColumnNameToIndex(columnName);
        }

        if ("t".equals(name))
            mIsTElement = true;
        else
            mIsTElement = false;

        mCellValue = "";
    }

    @Override
    public void endElement(String uri, String localName, String name)
        throws SAXException
    {
        if (mIsTElement) {
            AppendCellValue();
            mIsTElement = false;
        }
        else if ("v".equals(name)) {
            AppendCellValue();
        }
        else {
            if (name.equals("row")) {
                if (mSpans == 0)
                    mSpans = mCellIndex;

                int z = mSpans - mRowlist.size();
                for (int i = 0; i < z; i++) {
                    mRowlist.add(mCellIndex, null);
                    mCellIndex++;
                }

                // 获得excel中表头对应的列号的映射
                if (mRowIndex == 1)
                    mColunMapping = mRowReader.CreateColumnMappingHandler(
                        mIsExistTitle, mRowlist);

                mRowReader.RowsHandler(
                    mSheetIndex, mRowIndex, mRowlist, mRowMap, mIsExistTitle);

                mRowlist.clear();
                mRowMap.clear();
                mRowIndex++;
                mCellIndex = 0;
                mLastColumnIndex = 0;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
        mCellValue += new String(ch, start, length);
    }

    private static int ChangeColumnNameToIndex(String columnName)
    {
        int columnIndex = 0;
        char[] c = columnName.toCharArray();
        for (int i = 0; i < c.length; i++)
            columnIndex += ((int) c[i] - (int) 'A' + 1) * (int) Math.pow(
                26, c.length - i - 1);

        return columnIndex;
    }

    private void AppendCellValue()
    {
        int l = mColumnIndex - mLastColumnIndex;
        for (int i = 1; i < l; i++) {
            mRowlist.add(mCellIndex, null);
            mCellIndex++;
        }

        mCellValue = GetDataValue(mCellValue);
        mCellValue = mCellValue.trim().equals("") ? null : mCellValue;
        mRowlist.add(mCellIndex, mCellValue);

        if (mColunMapping != null)
            mRowMap.put(mColunMapping.get(mCellIndex), mCellValue);

        mCellIndex++;
        mLastColumnIndex = mColumnIndex;
    }

    /**
     * 处理数据类型
     * 
     * @param attributes
     */
    private void SetNextDataType(Attributes attributes)
    {
        mFormatIndex = -1;
        mFormatString = null;
        String cellType = attributes.getValue("t");

        if ("s".equals(cellType))
            mNextDataType = CellDataType.SSTINDEX;
        else if ("str".equals(cellType))
            mNextDataType = CellDataType.FORMULA;
        else if ("inlineStr".equals(cellType))
            mNextDataType = CellDataType.INLINESTR;
        else if ("b".equals(cellType))
            mNextDataType = CellDataType.BOOL;
        else if ("e".equals(cellType))
            mNextDataType = CellDataType.ERROR;
        else
            mNextDataType = CellDataType.NUMBER;

        String cellStyleStr = attributes.getValue("s");
        if (cellStyleStr != null) {
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = mStyleTable.getStyleAt(styleIndex);
            mFormatIndex = style.getDataFormat();
            mFormatString = style.getDataFormatString();

            if ("m/d/yy" == mFormatString) {
                mNextDataType = CellDataType.DATE;
                mFormatString = "yyyy-MM-dd hh:mm:ss.SSS";
            }

            if (mFormatString == null) {
                mNextDataType = CellDataType.NULL;
                mFormatString = BuiltinFormats.getBuiltinFormat(mFormatIndex);
            }
        }
    }

    /**
     * 对解析出来的数据进行类型处理
     * 
     * @param cellValue
     *            单元格的值（这时候是一串数字）
     * 
     * @return 当前单元格的值
     */
    public String GetDataValue(String cellValue)
    {
        // 这几个的顺序不能随便交换，交换了很可能会导致数据错误
        switch (mNextDataType) {
        case BOOL:
            char first = cellValue.charAt(0);
            cellValue = first == '0' ? "FALSE" : "TRUE";
            break;
        case ERROR:
            cellValue = "\"ERROR:" + cellValue.toString() + '"';
            break;
        case FORMULA:
            cellValue = '"' + cellValue.toString() + '"';
            break;
        case INLINESTR:
            XSSFRichTextString rtsi = new XSSFRichTextString(
                cellValue.toString());

            cellValue = rtsi.toString();
            rtsi = null;
            break;
        case SSTINDEX:
            String sstIndex = cellValue.toString();
            try {
                int idx = Integer.parseInt(sstIndex);
                XSSFRichTextString rtss = new XSSFRichTextString(
                    mSharedStringsTable.getEntryAt(idx));
                cellValue = rtss.toString();
                rtss = null;
            }
            catch (NumberFormatException ex) {
            }
            break;
        case NUMBER:
            if (mFormatString != null) {
                cellValue = mFormatter
                    .formatRawCellContents(
                        Double.parseDouble(cellValue), mFormatIndex,
                        mFormatString).trim();
            }

            cellValue = cellValue.replace("_", "").trim();
            break;
        case DATE:
            cellValue = mFormatter.formatRawCellContents(
                Double.parseDouble(cellValue), mFormatIndex, mFormatString);
            break;
        case NULL:
            cellValue = "";
            break;
        default:
            cellValue = null;
            break;
        }

        return cellValue;
    }
}