package org.antelop.poi.imp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Module Description：
 * http://blog.csdn.net/jinshuaiwang/article/details/15499685
 * 
 * Class Description：
 * 
 * 
 * @author antelop Creation Time： 2015年1月21日 下午5:20:08
 * @version
 */
public class Excel2003Reader implements HSSFListener
{
    private int minColumns = -1;
    private POIFSFileSystem fs;
    private int lastRowNumber;
    private int lastColumnNumber;

    /** Should we output the formula, or the value it has? */
    private boolean outputFormulaValues = true;

    /** For parsing Formulas */
    private SheetRecordCollectingListener workbookBuildingListener;
    // excel2003工作薄
    private HSSFWorkbook stubWorkbook;

    // Records we pick up as we process
    private SSTRecord sstRecord;
    private FormatTrackingHSSFListener formatListener;

    // 表索引
    private int sheetIndex = -1;

    private ArrayList<Record> boundSheetRecords = new ArrayList<Record>();

    // For handling formulas with string results
    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;

    // 当前行索引
    private int mRowIndex = 1;

    // 存储行记录的容器
    private List<String> mRowList = new ArrayList<String>();

    // 记住excel中表头对应的列号
    private Map<Integer, String> mColunMapping = null;

    // 当前行的值 key:excel中的表头 value:列值
    private Map<String, String> mRowMap = new HashMap<String, String>();

    private Boolean mIsExistTitle = true;

    private IReader mRowReader;

    public void setRowReader(IReader rowReader)
    {
        this.mRowReader = rowReader;
    }

    /**
     * 遍历excel下所有的sheet
     * 
     * @throws IOException
     */
    public void process(String fileName) throws IOException
    {
        this.fs = new POIFSFileSystem(new FileInputStream(fileName));
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
            this);
        formatListener = new FormatTrackingHSSFListener(listener);
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        }
        else {
            workbookBuildingListener = new SheetRecordCollectingListener(
                formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }
        factory.processWorkbookEvents(request, fs);
    }

    /**
     * HSSFListener 监听方法，处理 Record
     */
    @Override
    public void processRecord(Record record)
    {
        int thisRow = -1;
        int thisColumn = -1;
        String thisStr = null;
        switch (record.getSid()) {
        case BoundSheetRecord.sid:
            boundSheetRecords.add(record);
            break;
        case BOFRecord.sid:
            BOFRecord br = (BOFRecord) record;
            if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                // 如果有需要，则建立子工作薄
                if (workbookBuildingListener != null && stubWorkbook == null) {
                    stubWorkbook = workbookBuildingListener
                        .getStubHSSFWorkbook();
                }

                sheetIndex++;
                mRowIndex = 1;
            }
            break;

        case SSTRecord.sid:
            sstRecord = (SSTRecord) record;
            break;

        case BlankRecord.sid:
            BlankRecord brec = (BlankRecord) record;
            thisRow = brec.getRow();
            thisColumn = brec.getColumn();
            thisStr = "";

            AppendCellValue(thisColumn, thisStr);
            break;
        case BoolErrRecord.sid: // 单元格为布尔类型
            BoolErrRecord berec = (BoolErrRecord) record;
            thisRow = berec.getRow();
            thisColumn = berec.getColumn();
            thisStr = berec.getBooleanValue() + "";

            AppendCellValue(thisColumn, thisStr);
            break;

        case FormulaRecord.sid: // 单元格为公式类型
            FormulaRecord frec = (FormulaRecord) record;
            thisRow = frec.getRow();
            thisColumn = frec.getColumn();
            if (outputFormulaValues) {
                if (Double.isNaN(frec.getValue())) {
                    // Formula result is a string
                    // This is stored in the next record
                    outputNextStringRecord = true;
                    nextRow = frec.getRow();
                    nextColumn = frec.getColumn();
                }
                else {
                    thisStr = formatListener.formatNumberDateCell(frec);
                }
            }
            else {
                thisStr = '"' + HSSFFormulaParser.toFormulaString(
                    stubWorkbook, frec.getParsedExpression()) + '"';
            }

            AppendCellValue(thisColumn, thisStr);
            break;
        case StringRecord.sid:// 单元格中公式的字符串
            if (outputNextStringRecord) {
                // String for formula
                StringRecord srec = (StringRecord) record;
                thisStr = srec.getString();
                thisRow = nextRow;
                thisColumn = nextColumn;
                outputNextStringRecord = false;
            }
            break;
        case LabelRecord.sid:
            LabelRecord lrec = (LabelRecord) record;
            thisRow = lrec.getRow();
            thisColumn = lrec.getColumn();
            thisStr = lrec.getValue().trim();
            thisStr = thisStr.equals("") ? " " : thisStr;

            AppendCellValue(thisColumn, thisStr);
            break;
        case LabelSSTRecord.sid: // 单元格为字符串类型
            LabelSSTRecord lsrec = (LabelSSTRecord) record;
            thisRow = lsrec.getRow();
            thisColumn = lsrec.getColumn();
            if (sstRecord == null) {
                thisStr = " ";
                mRowList.add(thisColumn, " ");
            }
            else {
                thisStr = sstRecord
                    .getString(lsrec.getSSTIndex()).toString().trim();
            }

            AppendCellValue(thisColumn, thisStr);
            break;
        case NumberRecord.sid: // 单元格为数字类型
            NumberRecord numrec = (NumberRecord) record;
            thisRow = numrec.getRow();
            thisColumn = numrec.getColumn();
            thisStr = formatListener.formatNumberDateCell(numrec).trim();

            // 向容器加入列值
            AppendCellValue(thisColumn, thisStr);
            break;
        default:
            break;
        }

        // 遇到新行的操作
        if (thisRow != -1 && thisRow != lastRowNumber) {
            lastColumnNumber = -1;
        }

        // 空值的操作
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            thisRow = mc.getRow();
            thisColumn = mc.getColumn();
            mRowList.add(thisColumn, " ");
        }

        // 更新行和列的值
        if (thisRow > -1)
            lastRowNumber = thisRow;
        if (thisColumn > -1)
            lastColumnNumber = thisColumn;

        // 行结束时的操作
        if (record instanceof LastCellOfRowDummyRecord) {
            if (minColumns > 0) {
                // 列值重新置空
                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }
            }
            lastColumnNumber = -1;
            try {
                // 获得excel中表头对应的列号的映射
                if (mRowIndex == 1)
                    mColunMapping = mRowReader.CreateColumnMappingHandler(
                        mIsExistTitle, mRowList);

                mRowReader.RowsHandler(
                    sheetIndex, mRowIndex, mRowList, mRowMap, mIsExistTitle);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // 清空容器
            mRowList.clear();
            mRowIndex++;
        }
    }

    private void AppendCellValue(int mCellIndex, String mCellValue)
    {
        mRowList.add(mCellIndex, mCellValue);

        if (mColunMapping != null)
            mRowMap.put(mColunMapping.get(mCellIndex), mCellValue);
    }
}