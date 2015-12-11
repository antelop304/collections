package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antelop.poi.imp.ExportUtil;
import org.antelop.poi.imp.ImportUtils;
import org.antelop.poi.imp.TemplateReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.xml.sax.SAXException;

public class POITest
{
//	@Test
	public void testExcelVersion() throws FileNotFoundException {
		System.out.println(isExcel2007(new FileInputStream(new File("E:\\poiEXCEL\\createDate2003-2.xlsx"))));
//		System.out.println(isExcel2007(new FileInputStream(new File("E:\\poiEXCEL\\1.xlsx"))));
//		System.out.println(isExcel2003(new FileInputStream(new File("E:\\poiEXCEL\\createDate2003-2.xlsx"))));
	}
	
	public boolean isExcel2007(InputStream is) {
	    try {
	      new XSSFWorkbook(is);
	    } catch (Exception e) {
	      return false;
	    }
	    return true;
	  }
	
	public boolean isExcel2003(InputStream is) {
		try {
			new HSSFWorkbook(is);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Test
    public void readExcel()
    {
        long start = System.currentTimeMillis();
//        createDate2Excel();
        readExcel("E:\\poiEXCEL\\createDate2003-2.xls");
//        readExcel("E:\\poiEXCEL\\1.xlsx");
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end-start)/1000);
        // TODO Auto-generated method stub
        
    }
    public static void readExcel(String fileName)
    {
        TemplateReader reader = new TemplateReader(null) {
            @Override
            public void ContentsHandler(
                int sheetIndex,
                int rowIndex,
                List<String> rowList,
                Map<String, String> rowMap) throws SAXException
            {
                System.out.println(sheetIndex+"：" + rowIndex + "," +rowMap);
//                if(sheetIndex == 1)
//                    System.out.println(sheetIndex + "," + rowIndex + ":" + "size:"  + rowList.size() + rowList);
            }
        };
        try {
            new ImportUtils().ReadFile(reader, fileName, null, true, 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void createDate2Excel()
    {//30s 45s
        String fileFullName = "E:\\poiEXCEL\\createDate2003-2.xls";
        LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> reportDataMap = new LinkedHashMap<Integer, LinkedHashMap<Integer,Object>>();
        for (int i = 0; i < 10; i++) {
            LinkedHashMap<Integer, Object> reportRowMap = new LinkedHashMap<Integer, Object>();
            for (int j = 0; j < 12; j++) {
                reportRowMap.put(j, "value"+j);
            }
            reportDataMap.put(i, reportRowMap);
            System.out.println(i);
        }
        HSSFWorkbook hw = new HSSFWorkbook();
        Sheet hsData2 = hw.createSheet("测试数据2");
        Sheet hsData = hw.createSheet("测试数据");
       /* Sheet hsData3 = hw.createSheet("测试数据3");
        Sheet hsData4 = hw.createSheet("测试数据4");
        Sheet hsData5 = hw.createSheet("测试数据5");
        Sheet hsData6 = hw.createSheet("测试数据6");
        Sheet hsData7 = hw.createSheet("测试数据7");
        Sheet hsData8 = hw.createSheet("测试数据8");
        Sheet hsData9 = hw.createSheet("测试数据9");*/
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData2);
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData);
        /*ExportUtil.AddDataToSheet(reportDataMap, hw, hsData3);
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData4);
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData5);
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData6);
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData7);
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData8);
        ExportUtil.AddDataToSheet(reportDataMap, hw, hsData9);*/
        
        FileOutputStream out = null;
        try {
            System.out.println("ok");
            out = new FileOutputStream(fileFullName);
            hw.write(out);
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(out!=null)out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}