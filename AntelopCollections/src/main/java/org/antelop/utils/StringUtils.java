package org.antelop.utils;


/**
 * 字符串工具类
 * 
 * @author antelop
 * @version 2014/08/19
 */
public class StringUtils
{
    /**
     * 判断字符串是否为空串
     * 
     * @param string
     *            字符串
     * 
     * @return 是否为空串
     */
    public static boolean IsNullOrEmpty(String string)
    {
        return ((string == null) || (string.length() <= 0));
    }
    
    /**
     * 验证 字符床 是空
     * @param str 字符串
     * @return true or false
     */
    public static boolean isNull(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 验证字符串不为空
     * @param str
     * @return true or false
     */
    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    public static boolean IsIgnoreContains(String source,String str)
    {
        return source.toUpperCase().contains(str.toUpperCase());
    }
}