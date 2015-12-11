package org.antelop.utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;


public class JsonSerializer
{
    private static ObjectMapper sObjectMapper = null;
    public static String dataFormat = "yyyy-MM-dd HH:mm:ss";

    private static synchronized void InitializeObjectMapper()
    {
        if (sObjectMapper == null) {
            sObjectMapper = new ObjectMapper();

            sObjectMapper
                .configure(
                    DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            // 时间序列化格式
            sObjectMapper.setDateFormat(new SimpleDateFormat(dataFormat));
        }
    }

    private static ObjectMapper GetObjectMapper()
    {
        if (sObjectMapper == null)
            InitializeObjectMapper();
        
        sObjectMapper.setDateFormat(new SimpleDateFormat(dataFormat));
        return sObjectMapper;
    }

    /**
     * 将对象序列化成JSON字节数组
     * 
     * @param object
     *            对象
     * 
     * @return JSON字节数组,失败返回空
     */
    public static <T> byte[] Serialize(T object)
    {
        if (object == null)
            return null;

        try {
            return GetObjectMapper().writeValueAsBytes(object);
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * JSON字节数组反序列化为对象
     * 
     * @param data
     *            JSON字节数组
     * 
     * @param clazz
     *            对象类型
     * 
     * @return 对象,失败返回空
     */
    public static <T> T Deserialize(byte[] data, Class<T> clazz)
    {
        if ((data == null) || (clazz == null))
            return null;

        try {
            return GetObjectMapper().readValue(data, clazz);
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * 将对象序列化成JSON字符串
     * 
     * @param object
     *            对象
     * 
     * @return JSON字符串,失败返回空
     */
    public static <T> String ObjectToJson(T object)
    {
        if (object == null)
            return null;

        try {
            return GetObjectMapper().writeValueAsString(object);
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * JSON字符串反序列化为对象
     * 
     * @param data
     *            JSON字符串
     * @param clazz
     *            对象类型
     * 
     * @return 对象,失败返回空
     */
    public static <T> T JsonToObject(String data, Class<T> clazz)
    {
        if ((data == null) || (clazz == null))
            return null;

        try {
            return GetObjectMapper().readValue(data, clazz);
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * 结果集数据转为JSON字符串
     * 
     * @param rs
     *            结果集
     * 
     * @return JSON字符串,失败返回空
     */
    public static String ResultSetToJson(ResultSet rs)
    {
        if (rs == null)
            return "";

        try {
            return GetObjectMapper().writeValueAsString(
                GetEntitiesFromResultSet(rs));
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * JSON字符串转为散列表
     * 
     * @param json
     *            字符串
     * 
     * @return 散列表,失败返回空
     */
    public static Map<String, Object> JsonToMap(String json)
    {
        if (StringUtils.IsNullOrEmpty(json))
            return null;

        try {
            return GetObjectMapper().readValue(
                json, new TypeReference<HashMap<String, Object>>() {
                });
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * JSON字符串转为大小写不敏感散列表
     * 
     * @param json
     *            字符串
     * 
     * @return 大小写不敏感散列表,失败返回空
     */
    public static CaseInsensitiveMap<String, Object> JsonToCaseInsensitiveMap(
        String json)
    {
        if (StringUtils.IsNullOrEmpty(json))
            return null;

        try {
            Map<String, Object> map = GetObjectMapper().readValue(
                json, new TypeReference<HashMap<String, Object>>() {
                });

            return MakeCaseInsensitiveMap(map);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 列表数据转为JSON字符串
     * 
     * @param data
     *            列表数据
     * 
     * @return JSON字符串,失败返回空
     */
    public static <T> String ListToJson(List<T> data)
    {
        if (data == null)
            return null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[");

            for (T t : data) {
                sb.append(ObjectToJson(t));
                sb.append(",");
            }

            if (sb.length() > 1)
                sb.delete(sb.length() - 1, sb.length());
            sb.append("]");

            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * JSON字符串反序列化为列表数据
     * 
     * @param data
     *            JSON字符串
     * @param clazz
     *            列表数据类型
     * 
     * @return 列表数据,失败返回空
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> JsonToList(String data, Class<T> clazz)
    {
        try {
            JavaType javaType = GetObjectMapper()
                .getTypeFactory().constructParametricType(List.class, clazz);

            return (List<T>) GetObjectMapper().readValue(data, javaType);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 将结果集转换为列表
     * 
     * @param rs
     *            结果集
     * 
     * @return 列表,失败返回空
     * 
     * @throws Exception
     */
    public static List<Map<String, Object>> GetEntitiesFromResultSet(
        ResultSet rs) throws Exception
    {
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();

        List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();

        do {
            entities.add(GetEntityFromResultSet(rs, md, columnCount));
        } while (rs.next());

        return entities;
    }

    /**
     * 将结果集转换为散列表
     * 
     * @param rs
     *            结果集
     * @param md
     *            结果集元数据
     * @param columnCount
     *            结果集字段总数
     * 
     * @return 散列表
     * 
     * @throws Exception
     */
    private static Map<String, Object> GetEntityFromResultSet(
        ResultSet rs,
        ResultSetMetaData md,
        int columnCount) throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();

        for (int i = 1; i <= columnCount; ++i) {
            String columnName = md.getColumnLabel(i);
            Object object = rs.getObject(i);
            result.put(columnName, object);
        }

        return result;
    }

    /**
     * 创建大小写不敏感散列表
     * 
     * @param map
     *            原始散列表
     * 
     * @return 大小写不敏感散列表
     * 
     * @throws Exception
     */
    private static CaseInsensitiveMap<String, Object> MakeCaseInsensitiveMap(
        Map<String, Object> map) throws Exception
    {
        CaseInsensitiveMap<String, Object> result = new CaseInsensitiveMap<String, Object>();

        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = (Entry<String, Object>) it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}