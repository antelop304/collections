package org.antelop.poi.imp.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antelop.poi.imp.IReader;
import org.antelop.utils.StringUtils;

public class CSVReader
{
    private List<String> mRowList = new ArrayList<String>();

    // 记住excel中表头对应的列号
    private Map<Integer, String> mColunMapping = null;

    // 当前行的值 key:excel中的表头 value:列值
    private Map<String, String> mRowMap = new HashMap<String, String>();

    private IReader mRowReader;

    private int mRowIndex = 1;

    private int mSheetIndex = 0;

    private String mSeparator = ",";

    private Boolean mIsExistTitle = true;

    public void setRowReader(IReader rowReader)
    {
        mRowReader = rowReader;
    }

    public void setSeparator(String separator)
    {
        mSeparator = separator;
    }

    public void setIsExistTitle(Boolean isExistTitle)
    {
        mIsExistTitle = isExistTitle;
    }

    public void Process(String fileName) throws Exception
    {   
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(fileName), "GB2312"));
        String items[] = null;
        String line = null;
        String item = null;

        while ((line = reader.readLine()) != null) {

            items = line.split(mSeparator);
            for (int i = 0; i < items.length; i++) {

                item = items[i].trim();
                if ((StringUtils.isNotNull(item)))
                    mRowList.add(items[i]);
                else
                    mRowList.add(i, null);

                if (mColunMapping != null)
                    mRowMap.put(mColunMapping.get(i), item);
            }

            // 获得excel中表头对应的列号的映射
            if (mRowIndex == 1)
                mColunMapping = mRowReader.CreateColumnMappingHandler(
                    mIsExistTitle, mRowList);

            mRowReader.RowsHandler(
                mSheetIndex, mRowIndex, mRowList, mRowMap,mIsExistTitle);
            mRowList.clear();
            mRowIndex++;
        }

        reader.close();
    }
}