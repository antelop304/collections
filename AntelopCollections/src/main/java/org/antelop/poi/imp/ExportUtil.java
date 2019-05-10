package org.antelop.poi.imp;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

public class ExportUtil
{

    /**
     * 
     * Description：添加数据到工作簿.
     * 
     * Author：antelop Creation Time： 2014年9月18日 下午3:32:30
     * 
     * Modification Record LastModifyTime： Modifier： Content Modification：
     * 
     * @param reportDataMap
     *            报表数据
     * @param hw
     *            工作表.
     * @param hsData
     *            图片工作簿
     */
    public static void AddDataToSheet(
        LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> reportDataMap,
        Workbook hw,
        Sheet hsData)
    {
        try {
            // 行
            Row hr = null;
            // 单元格
            Cell hc = null;
            /**
             * 按指定格式输出描述信息
             */
            if (reportDataMap != null) {
                Map<Integer, Integer> sizeMap = new HashMap<Integer, Integer>();
                for (Entry<Integer, LinkedHashMap<Integer, Object>> reportRowMap : reportDataMap
                    .entrySet()) {
                    hr = hsData.createRow(reportRowMap.getKey());
                    System.out.println("rownum:"+hr.getRowNum());
                    for (Entry<Integer, Object> reportColMap : reportRowMap
                        .getValue().entrySet()) {
                        hc = hr.createCell(reportColMap.getKey());
                        Object value = reportColMap.getValue();
                        autoColWidth(value, hsData, hc, sizeMap);
                        if (value == null) {
                            hc.setCellValue("");
                        }
                        else if (value instanceof String) {
                            hc.setCellValue((String) value);
                        }
                        else if (value instanceof Timestamp) {
                            Timestamp t = (Timestamp) value;
                            hc.setCellValue(new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm").format(new Date(t
                                .getTime())) + ":00");
                        }
                        else if (value instanceof Integer) {
                            System.out.println(value);
                            hc.setCellType(Cell.CELL_TYPE_NUMERIC);
                            hc.setCellValue((Integer) value);
                        }
                        else if (value instanceof Double) {
                            hc.setCellType(Cell.CELL_TYPE_NUMERIC);
                            hc.setCellValue((Double) value);
                        }
                        else if (value instanceof Date) {
                            hc.setCellValue((Date) value);
                        }
                        else if (value instanceof Boolean) {
                            hc.setCellValue((Boolean) value);
                        }
                        else {
                            throw new RuntimeException("不支持该数据类型");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * Description： 自适应列宽 Author：antelop
     * 
     * Creation Time： 2015年1月22日 上午9:59:12
     * 
     * Modification Record LastModifyTime： Modifier： Content Modification：
     * 
     * @param value
     *            单元格值
     * @param hsData
     *            sheet
     * @param hc
     *            列
     * @param sizeMap
     *            列号对应的最大的列宽值 key:列号 value:列宽
     */
    private static void autoColWidth(
        Object value,
        Sheet hsData,
        Cell hc,
        Map<Integer, Integer> sizeMap)
    {
        Integer colsize = sizeMap.get(hc.getColumnIndex());
        int size = (int) (((value + "").getBytes()).length * 256 * 1.1);
        size = (colsize == null || colsize < size) ? size : colsize;
        hsData.setColumnWidth(hc.getColumnIndex(), size);
        sizeMap.put(hc.getColumnIndex(), size);
    }

    /**
     * 
     * Description：将图片添加到工作簿.
     * 
     * Author：antelop Creation Time： 2014年9月18日 下午3:33:21
     * 
     * Modification Record LastModifyTime： Modifier： Content Modification：
     * 
     * @param baos
     *            图片输出流.
     * @param hw
     *            工作表.
     * @param hsImage
     *            图片工作簿
     */
    public void AddImageToSheet(
        ByteArrayOutputStream baos,
        Workbook hw,
        Sheet hsImage)
    {
        try {
            // 默认图片格式
            int format = HSSFWorkbook.PICTURE_TYPE_JPEG;

            // 图形管理顶级容器
            Drawing patriarch = hsImage.createDrawingPatriarch();
            ClientAnchor hca = new XSSFClientAnchor();
            hca.setCol1(0);
            hca.setRow1(hsImage.getLastRowNum() + 2);

            // 画图
            patriarch.createPicture(
                hca, hw.addPicture(baos.toByteArray(), format)).resize();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * Description： 构造导出excel的数据 Author：antelop
     * 
     * Creation Time： 2015年1月22日 上午9:57:07
     * 
     * Modification Record LastModifyTime： Modifier： Content Modification：
     * 
     * @param mExcelMap
     *            表头映射 key:列名 value:数据库字段名
     * @param rSet
     *            原始数据
     * @return
     * @throws SQLException
     */
    public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object>>
        getReportDataMap(Map<String, String> mExcelMap, ResultSet rSet)
            throws SQLException
    {
        LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> reportDataMap = new LinkedHashMap<Integer, LinkedHashMap<Integer, Object>>();
        ResultSetMetaData rsmd = rSet.getMetaData();
        int columnCount = rsmd.getColumnCount();
        int rowIndex = 0;
        LinkedHashMap<Integer, Object> reportRowMap = new LinkedHashMap<Integer, Object>();

        Map<String, Integer> map = new HashMap<String, Integer>();// 记住excel中所有的列的索引号
        int index = 0;// 列的索引
        for (Map.Entry<String, String> en : mExcelMap.entrySet()) {
            String key = en.getKey();
            String value = en.getValue();
            boolean isConts = false;
            for (int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                if (colName.equals(value)) {
                    isConts = true;
                    reportRowMap.put(index, key);
                    map.put(value, index++);
                    break;
                }
            }
            // 数据库没值的列
            if (!isConts) {
                reportRowMap.put(index++, key);
            }
        }
        reportDataMap.put(rowIndex++, reportRowMap);
        do {
            reportRowMap = new LinkedHashMap<Integer, Object>();
            for (int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                Integer cl = map.get(colName);
                if (cl != null) {
                    reportRowMap.put(
                        map.get(colName), rSet.getObject(colName));
                }
            }
            reportDataMap.put(rowIndex++, reportRowMap);
        } while (rSet.next());

        return reportDataMap;
    }
}
