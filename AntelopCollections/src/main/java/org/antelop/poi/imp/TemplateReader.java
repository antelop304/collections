package org.antelop.poi.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.SAXException;

public abstract class TemplateReader implements IReader
{
    protected List<String> mTitles = new ArrayList<String>();

    protected long mRowCount = 0;

    protected long mIgnoreRowCount = 0;

    protected Map<String, String> titlesMappings = null;

    public TemplateReader(Map<String, String> titleMappings)
    {
        this.titlesMappings = titleMappings;
    }

    public long GetRowCount()
    {
        return mRowCount;
    }

    public long GetIgnoreRowCount()
    {
        return mIgnoreRowCount;
    }

    @Override
    public void RowsHandler(
        int sheetIndex,
        int rowIndex,
        List<String> rowList,
        Map<String, String> rowMap,
        Boolean isExistTitle) throws SAXException
    {
        if (rowIndex == 1) {
            if (isExistTitle) {
                for (String title : rowList) {
                    mTitles.add(title);
                }

                return;
            }else{
                for (Entry<String, String> columns : titlesMappings.entrySet())
                    mTitles.add(columns.getKey());
            }
        }

        if (mTitles.size() > 0) {
            int sizeInterval = mTitles.size() - rowList.size();
            if (sizeInterval > 0)
                for (int i = 0; i < sizeInterval; i++) {
                    rowList.add(null);
                }
            else if (sizeInterval < 0)
                return;
        }

        ContentsHandler(sheetIndex, rowIndex, rowList, rowMap);

        mRowCount++;
    }

    /**
     * 获得excel中表头对应的列号的映射
     * 
     * @param rowIndex
     * @param rowList
     * @return
     * @throws SAXException
     */
    @Override
    public Map<Integer, String> CreateColumnMappingHandler(
        Boolean isExistTitle,
        List<String> rowList) throws SAXException
    {
        Map<Integer, String> titles = new LinkedHashMap<Integer, String>();
        int i = 0;
        if (isExistTitle) {
            for (Iterator<String> it = rowList.iterator(); it.hasNext();) {
                titles.put(i++, it.next());
            }
        }
        else {
            for (Entry<String, String> columns : titlesMappings.entrySet())
                titles.put(i++, columns.getKey());
        }
        return titles;
    }

    /**
     * 业务逻辑实现方法
     * 
     * @param sheetIndex
     * @param rowIndex
     * @param rowList
     */
    public abstract void ContentsHandler(
        int sheetIndex,
        int rowIndex,
        List<String> rowList,
        Map<String, String> rowMap) throws SAXException;
}