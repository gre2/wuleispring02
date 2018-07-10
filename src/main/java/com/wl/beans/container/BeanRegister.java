package com.wl.beans.container;

import com.wl.beans.model.BeanElement;

import java.util.Map;

public class BeanRegister {


    public static void register(Map<String,BeanElement> map) {
        for(Map.Entry<String,BeanElement> entry:map.entrySet()){
            BeanElement beanElement=entry.getValue();
            Object bean=BeanGenerator.newBean(beanElement);
            BeanContainer.putIfAbsent(entry.getKey(),bean);
        }
    }
}
