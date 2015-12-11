package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antelop.utils.XmlUtils;
import org.dom4j.Document;
import org.dom4j.Element;

public class ReadXml
{

    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        try {
            Document document = XmlUtils.getDocument("E:/ImportExport.xml");
            Element root = document.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> entrys = root
                .element("Resources") .elements("Resource");
            List<Map<String,String>> resources = new ArrayList<Map<String,String>>();
            Map<String,String> resourceMap = new HashMap<String,String>();
            for (Element entry : entrys) {
                String method = entry.attribute("method").getText();
                String clazz = entry.attribute("class").getText();
                String name = entry.attribute("name").getText();
                resourceMap.put("method",method);
                resourceMap.put("class",clazz);
                resourceMap.put("name",name);
                resources.add(resourceMap);
            }
            System.out.println(resources.iterator().next().get("class"));
            System.out.println("成功");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
