package com.wl.beans.container;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BeanContainer {

    public static final ConcurrentMap<String,Object> beans=new ConcurrentHashMap<String, Object>();

    public static Object get(String key){
        return beans.get(key);
    }

    public static Object putIfAbsent(String key,Object value){
        return beans.putIfAbsent(key,value);
    }

    public static Object put(String key,Object value){
        return beans.put(key,value);
    }

}
