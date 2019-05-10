package org.antelop.poi.imp;

import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

public interface IReader
{
    /**
     * 业务逻辑实现方法
     * @param sheetIndex
     * @param rowIndex
     * @param rowList
     * @param rowMap
     * @param isExistTitle
     * @throws SAXException
     */
    public void RowsHandler(
        int sheetIndex,
        int rowIndex,
        List<String> rowList,
        Map<String,String> rowMap,
        Boolean isExistTitle) throws SAXException;

    /**
     * 获得excel中表头对应的列号的映射
     * @param isExistTitle
     * @param rowList
     * @return
     * @throws SAXException
     */
    public Map<Integer,String> CreateColumnMappingHandler(
        Boolean isExistTitle,
        List<String> rowList) throws SAXException;
}