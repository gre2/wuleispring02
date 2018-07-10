package com.wl.aop.parser;

import com.wl.aop.model.AspectElement;
import com.wl.aop.proxy.AopBeanProxy;
import com.wl.aop.utils.XmlUtils;
import com.wl.beans.container.BeanContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class AopConfigParser {


    public static void getAopConfig(Document document) {
        List<AspectElement> aspectList=getAspectList(document);
        for(AspectElement aspectElement:aspectList){
            //target被bean代理
            Object target = BeanContainer.get(aspectElement.getTarget());
            Object bean = BeanContainer.get(aspectElement.getBean());
            if (target != null && bean != null) {
                Object proxyBean=new AopBeanProxy().bind(target,bean,aspectElement);
                BeanContainer.put(aspectElement.getTarget(),proxyBean);
            }
        }
    }

    private static List<AspectElement> getAspectList(Document document) {
        List<AspectElement> aspectElementList=new ArrayList<AspectElement>();
        NodeList nodeList=document.getElementsByTagName("aop:aspect");
        for(int i=0;i<nodeList.getLength();i++){
            Node node=nodeList.item(i);
            String xmlNodeString=XmlUtils.nodeToString(node);
            AspectElement element=XmlUtils.toBean(xmlNodeString,AspectElement.class);
            aspectElementList.add(element);
        }
        return aspectElementList;
    }
}
