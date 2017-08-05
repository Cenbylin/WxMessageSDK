package com.github.cenbylin.wxmessage.sdk.util;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * xml工具类
 *
 * @author Cenbylin
 */
public class XMLUtil {
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception 异常
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx=0; idx<nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            stream.close();
        }
        catch (Exception ex) {

        }
        return data;
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception 异常
     */
    public static String mapToXml(Map<String, String> data, String rootName) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement(rootName);
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }
    /**
     * 将Map转换为XML格式的字符串-xml为根
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception 异常
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        return mapToXml(data, "xml");
    }
    /**
     * 将Map转换为XML格式的字符串-xml为根-递归深度
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception 异常
     */
    public static String mapToXmlDeep(Map<String, Object> data) throws Exception {
        return mapToXmlDeep(data, "xml");
    }
    /**
     * 将Map转换为XML格式的字符串-递归深度
     * @param rootName rootName
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception 异常
     */
    public static String mapToXmlDeep(Map<String, Object> data, String rootName) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement(rootName);
        document.appendChild(root);
        buildElement(document, root, data);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    public static void buildElement(org.w3c.dom.Document document, Element root, Map<String, Object> data) throws Exception{
        for (String key: data.keySet()) {
            org.w3c.dom.Element filed = document.createElement(key);
            Object value = data.get(key);
            if (value instanceof String){
                filed.appendChild(document.createTextNode((String)value));
            } else if (value instanceof Map){
                buildElement(document, filed, (Map)value);
            } else if (value instanceof List){
                for (Object o : (List)value){
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("item", o);
                    buildElement(document, filed, m);
                }
            }
            root.appendChild(filed);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        List<String> l = new LinkedList<String>();
        l.add("111");
        l.add("222");
        m.put("key1", "!11");
        m.put("in", l);
        System.out.println(mapToXmlDeep(m, "xml"));
    }

}
