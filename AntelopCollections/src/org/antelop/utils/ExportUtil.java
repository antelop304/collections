package org.antelop.utils;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
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
    public void AddDataToSheet(
        LinkedHashMap<Integer, LinkedHashMap<Integer, String>> reportDataMap,
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
            for (Entry<Integer, LinkedHashMap<Integer, String>> reportRowMap : reportDataMap
                .entrySet()) {
                hr = hsData.createRow(reportRowMap.getKey());
                hsData.autoSizeColumn(reportRowMap.getKey().intValue());
                for (Entry<Integer, String> reportColMap : reportRowMap
                    .getValue().entrySet()) {
                    hc = hr.createCell(reportColMap.getKey());
                    hc.setCellValue(reportColMap.getValue());
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
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

}
