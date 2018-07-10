package com.wl.aop.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wl.aop.model.AspectElement;
import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class XmlUtils {
    public static String nodeToString(Node node) {
        Transformer transformer = null;
        String result = null;
        if (node == null) {
            throw new IllegalArgumentException();
        }
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (transformer != null) {
            try {
                StringWriter sw = new StringWriter();
                transformer.transform(new DOMSource(node), new StreamResult(sw));
                return sw.toString();
            } catch (TransformerException te) {
                throw new RuntimeException(te.getMessage());
            }
        }
        return result;
    }

    public static <T> T  toBean(String xmlStr, Class<AspectElement> cls) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        @SuppressWarnings("unchecked")
        T t = (T) xstream.fromXML(xmlStr);
        return t;
    }
}
