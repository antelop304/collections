package org.antelop.poi.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.antelop.poi.imp.csv.CSVReader;
import org.antelop.utils.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportUtils
{
    // excel2003扩展名
//     private static final String EXCEL03_EXTENSION = ".xls";

    // excel2007扩展名
//    private static final String EXCEL07_EXTENSION = ".xlsx";

    // csv(逗号分隔符)拓展名
    private static final String CSV_EXTENSION = ".csv";

    // txt拓展名
    private static final String TXT_EXTENSION = ".txt";

    public static ImportUtils GetInstance()
    {
        return new ImportUtils();
    }

    /**
     * 读取文件
     * 
     * @param reader
     * @param fileName
     * 
     * @throws Exception
     */
    public void ReadFile(IReader reader, String fileName) throws Exception
    {
        ReadFile(reader, fileName, null, null);
    }

    /**
     * 读取文件
     * 
     * @param reader
     * @param fileName
     * @param separator
     * 
     * @throws Exception
     */
    public void ReadFile(IReader reader, String fileName, String separator)
        throws Exception
    {
        ReadFile(reader, fileName, separator, null);
    }

    /**
     * 读取文件
     * 
     * @param reader
     * @param fileName
     * @param isExistTitle
     * 
     * @throws Exception
     */
    public void ReadFile(IReader reader, String fileName, Boolean isExistTitle)
        throws Exception
    {
        ReadFile(reader, fileName, null, isExistTitle);
    }

    /**
     * 读取单个sheet
     * 
     * @param reader
     * @param fileName
     * @param separator
     * @param isExistTitle
     * 
     * @throws Exception
     */
    public void ReadFile(
        IReader reader,
        String fileName,
        String separator,
        Boolean isExistTitle,
        int sheetId) throws Exception
    {
        if (isExcel2003(fileName)) {
            /*XLSReader xlsReader = new XLSReader();
            xlsReader.setmRowReader(reader);
            xlsReader.Process(fileName, sheetId);*/
            Excel2003Reader xlsReader = new Excel2003Reader();
            xlsReader.setRowReader(reader);
            xlsReader.process(fileName);
        }
        else if (isExcel2007(fileName)) {
            XLSXReader xslxReader = new XLSXReader();
            if (isExistTitle != null)
                xslxReader.setIsExistTitle(isExistTitle);
            xslxReader.setRowReader(reader);
            xslxReader.Process(fileName,sheetId);
        }
        else if (fileName.endsWith(CSV_EXTENSION)) {
            CSVReader csvReader = new CSVReader();
            if (StringUtils.isNotNull(separator))
                csvReader.setSeparator(separator);
            if (isExistTitle != null)
                csvReader.setIsExistTitle(isExistTitle);
            csvReader.setRowReader(reader);
            csvReader.Process(fileName);
        }
        else if (fileName.endsWith(TXT_EXTENSION)) {
            CSVReader csvReader = new CSVReader();
            if (!StringUtils.isNotNull(separator))
                csvReader.setSeparator(separator);
            if (isExistTitle != null)
                csvReader.setIsExistTitle(isExistTitle);
            csvReader.setRowReader(reader);
            csvReader.Process(fileName);
        }
        else {
            throw new Exception("文件格式错误,扩展名格式不正确.");
        }
    }
    
    /**
     * 读取文件
     * 
     * @param reader
     * @param fileName
     * @param separator
     * @param isExistTitle
     * 
     * @throws Exception
     */
    public void ReadFile(
        IReader reader,
        String fileName,
        String separator,
        Boolean isExistTitle) throws Exception
    {
        if (isExcel2007(fileName)) {
            XLSXReader xslxReader = new XLSXReader();
            if (isExistTitle != null)
                xslxReader.setIsExistTitle(isExistTitle);
            xslxReader.setRowReader(reader);
            xslxReader.Process(fileName);
        }
        else if (fileName.endsWith(CSV_EXTENSION)) {
            CSVReader csvReader = new CSVReader();
            if (!StringUtils.isNotNull(separator))
                csvReader.setSeparator(separator);
            if (isExistTitle != null)
                csvReader.setIsExistTitle(isExistTitle);
            csvReader.setRowReader(reader);
            csvReader.Process(fileName);
        }
        else if (fileName.endsWith(TXT_EXTENSION)) {
            CSVReader csvReader = new CSVReader();
            if (!StringUtils.isNotNull(separator))
                csvReader.setSeparator(separator);
            if (isExistTitle != null)
                csvReader.setIsExistTitle(isExistTitle);
            csvReader.setRowReader(reader);
            csvReader.Process(fileName);
        }
        else {
            throw new Exception("文件格式错误,扩展名格式不正确.");
        }
    }
    
	private boolean isExcel2007(String fileName) {
		try {
			return isExcel2007(new FileInputStream(new File(fileName)));
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean isExcel2007(InputStream is) {
		try {
			new XSSFWorkbook(is);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isExcel2003(String fileName) {
		try {
			return isExcel2003(new FileInputStream(new File(fileName)));
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean isExcel2003(InputStream is) {
		try {
			new HSSFWorkbook(is);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}