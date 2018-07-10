package com.wl.beans.container;

import com.wl.beans.model.BeanElement;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanGenerator {
    public static Object newBean(BeanElement beanElement) {
        try{
            Class clazz=Class.forName(beanElement.getClassName());
            Object  bean=clazz.newInstance();
            inject(bean,beanElement.getProperties());
            return bean;
        }catch (Exception e){

        }
        return null;
    }

    private static void inject(Object bean, Map<String,String> properties) {
        Map<String, String> methodMap = new HashMap<String, String>();
        for(Map.Entry<String,String> entry:properties.entrySet()){
            String configName = entry.getKey();
            String configValue = entry.getValue();
            String configMethodName="set"+String.valueOf(configName.charAt(0)).toUpperCase()+
                    configName.substring(1);
            methodMap.put(configMethodName,configValue);
        }

        Class clazz=bean.getClass();
        for(Method method:clazz.getMethods()){
            String methodName=method.getName();
            if(methodName.startsWith("set") && methodMap.containsKey(methodName)){
                try {
                    method.invoke(bean, methodMap.get(methodName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
