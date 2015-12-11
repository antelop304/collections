package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.antelop.utils.JsonSerializer;

public class JsonSerializerTest
{
    public static void main(String[] args) throws ParseException
    {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name3", "ff");
        m.put("name3", new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-11"));
        JsonSerializer.dataFormat = "yyyy-MM-dd";
        System.out.println(JsonSerializer.ObjectToJson(m));
        System.out.println(JsonSerializer.JsonToMap("{\"name3\": \"ff\", \"mc\": \"aaaaaaaaaaaaaa\", \"bmbh\": \"aa\", \"xmbh\": \"aa\",\"Id\": 1, \"remark1\": \"b\", \"name1\": \"bb\", \"name2\": \"cc\", \"remark0\": \"a\",      \"remark3\": \"ff\", \"remark2\": \"c\", \"name0\": \"aa\" }"));
        
        String json = "{\"name3\":\"2014-12-29 15:11:12\"}";
        System.out.println(JsonSerializer.JsonToMap(json));
        json = "{\"name3\":\"2014-12-29  15:11\"}";
        System.out.println(JsonSerializer.JsonToMap(json));
    }
}
