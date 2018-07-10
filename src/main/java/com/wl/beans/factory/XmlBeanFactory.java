package com.wl.beans.factory;

import com.wl.aop.parser.AopConfigParser;
import com.wl.beans.container.BeanContainer;
import com.wl.beans.container.BeanRegister;
import com.wl.beans.model.BeanElement;
import com.wl.beans.parser.DefaultDocumentLoader;

import org.w3c.dom.Document;

import java.util.Map;

public class XmlBeanFactory implements BeanFactory{

    public XmlBeanFactory(String fileName){
        try{
            Document document= DefaultDocumentLoader.getXmlDocument(fileName);
            Map<String,BeanElement> map=DefaultDocumentLoader.readDefinition(document);
            BeanRegister.register(map);

            //aop
            AopConfigParser.getAopConfig(document);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public Object getBean(String beanName) {
        return BeanContainer.get(beanName);
    }


}
