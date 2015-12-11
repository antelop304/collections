package org.antelop.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * sql语句相关的工具类.
 * @author antelop
 * @version 2014/08/19
 */
public class SqlUtils
{
    /**
     * 优化sql,用0代替查询条件.
     * 
     * 用于查询总记录数的sql的优化.还为解决distinct、嵌套子句如in的优化.
     * 
     * @param sql
     * @return
     */
    public static String optimizeSql(String sql)
    {
        LinkedHashMap<Integer, String> keywordMap = new LinkedHashMap<Integer, String>();
        String sqlch = "";
        // 获得sql语句中select和from的索引位置
        for (int i = 0; i < sql.length(); i++) {
            Character c = sql.charAt(i);
            sqlch += c.toString();
            if (sqlch.toUpperCase().contains("SELECT")) {
                keywordMap.put(i + 1, "SELECT");
                sqlch = "";
            }
            else if (sqlch.toUpperCase().contains("FROM")) {
                keywordMap.put(i - 4, "FROM");
                sqlch = "";
            }
        }

        LinkedList<Integer> selectList = new LinkedList<Integer>();
        for (Entry<Integer, String> e : keywordMap.entrySet()) {
            if ("SELECT".equals(e.getValue())) {
                selectList.add(e.getKey());
            }
            else {
                sql = whitespace(sql, selectList.pop(), e.getKey());
            }
        }

        return sql;
    }

    /**
     * 用空格和0代替查询条件.
     * 
     * @param sql
     * 
     * @param startIndex
     *            开始索引
     * @param endIndex
     *            结束索引
     * @return
     */
    private static String whitespace(String sql, int startIndex, int endIndex)
    {
        String wsp = "";
        for (int i = startIndex; i < endIndex - 1; i++) {
            wsp += " ";
        }
        return sql.subSequence(0, startIndex) + wsp + "0" + sql.subSequence(
            endIndex, sql.length());
    }
    
    public static void main(String[] args)
    {
       String sql = "select tb.id,(select max(num) from tableY) maxNum from tableX tb where tb.id";
       System.out.println(optimizeSql(sql));
    }
}
