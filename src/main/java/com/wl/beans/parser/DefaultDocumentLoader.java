package com.wl.beans.parser;


import com.wl.beans.model.BeanElement;
import com.wl.beans.model.PropertyElement;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class DefaultDocumentLoader {

    public static Document getXmlDocument(String fileName){
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        InputSource inputSource = null;
        Document document = null;
        try {
            InputStream inputStream = DefaultDocumentLoader.class.getClassLoader().getResourceAsStream(fileName);
            inputSource = new InputSource(inputStream);
            document = docBuilderFactory.newDocumentBuilder().parse(inputSource);
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return document;
    }

    public static Map<String,BeanElement> readDefinition(Document document) throws Exception {
        Map<String, BeanElement> beanDefinitionMap = new HashMap<String, BeanElement>();
        NodeList nodeList=document.getElementsByTagName("bean");
        //todo 可以多组node？
        for(int i=0;i<nodeList.getLength();i++){
            Node node=nodeList.item(i);
            BeanElement beanElement=parseOneBean(node);
            beanDefinitionMap.put(beanElement.getId(),beanElement);
        }
        return beanDefinitionMap;
    }

    private static BeanElement parseOneBean(Node beanNode) {
        BeanElement beanElement=new BeanElement();
        Map<String,String> properties=new HashMap<String, String>();

        NamedNodeMap namedNodeMap=beanNode.getAttributes();
        for(int i=0;i<namedNodeMap.getLength();i++){
            Node node=namedNodeMap.item(i);
            String nodeName=node.getNodeName();
            String nodeValue=node.getNodeValue();
            if("id".equals(nodeName)){
                beanElement.setId(nodeValue);
            }
            if("class".equals(nodeName)){
                beanElement.setClassName(nodeValue);
            }
        }

        //处理属性
        NodeList propertitList=beanNode.getChildNodes();
        for(int i=0;i<propertitList.getLength();i++){
            Node propertyNode=propertitList.item(i);
            NamedNodeMap propertyNodeMap=propertyNode.getAttributes();
            PropertyElement propertyElement = new PropertyElement();
            if(propertyNodeMap==null || propertyNodeMap.getLength()==0){continue;}
            for(int j=0;j<propertyNodeMap.getLength();j++){
                Node node=propertyNodeMap.item(j);
                String nodeName=node.getNodeName();
                String nodeValue=node.getNodeValue();
                if("name".equals(nodeName)){
                    propertyElement.setName(nodeValue);
                }
                if("value".equals(nodeName)){
                    propertyElement.setValue(nodeValue);
                }
            }
            properties.put(propertyElement.getName(),propertyElement.getValue());
        }
        beanElement.setProperties(properties);
        return beanElement;
    }
}
