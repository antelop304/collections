package org.antelop.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 使用dom4j进行xml解析.
 * 
 * @author antelop
 * @version 2014/08/19
 */
public class XmlUtils
{

    /**
     * 解析xml文件.
     * 
     * @param filename
     *            文件名
     * @return 得到文件对象.
     * @throws DocumentException
     * @throws MalformedURLException
     */
    public static Document getDocument(String filename)
        throws DocumentException, MalformedURLException
    {
        SAXReader reader = new SAXReader();
        return reader.read(new File(filename));
    }

    /**
     * 保存修改内容.
     * 
     * @param document
     * @param filename
     * @throws IOException
     */
    public static void write2Xml(Document document, String filename)
        throws IOException
    {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(filename), format);
        writer.write(document);
        writer.close();
    }

    public static List<Map<String,String>> getResources(){
        List<Map<String,String>> resources = new ArrayList<Map<String,String>>();
        try {
            Document document = XmlUtils.getDocument("E:/ImportExport.xml");
            Element root = document.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> entrys = root
                .element("Resources") .elements("Resource");
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resources;
    }

    public static void main(String[] args)
    {
        try {
            Document document = getDocument("D:/software/tomcat/apache-tomcat-7.0.52/webapps/geoserver/data/workspaces/easywlan2/easywlan2/datastore.xml");
            Element root = document.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> entrys = root
                .element("connectionParameters").elements("entry");
            for (Element entry : entrys) {
                String key = entry.attribute("key").getText();
                if ("port".equals(key)) {
                    entry.setText("3306");
                }
                else if ("host".equals(key)) {
                    entry.setText("localhost");
                }
                else if ("database".equals(key)) {
                    entry.setText("easywlan2");
                }
            }
            write2Xml(
                document,
                "D:/software/tomcat/apache-tomcat-7.0.52/webapps/geoserver/data/workspaces/easywlan2/easywlan2/datastore.xml");
            System.out.println("成功");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
