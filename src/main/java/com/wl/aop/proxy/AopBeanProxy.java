package com.wl.aop.proxy;

import com.wl.aop.model.AspectElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopBeanProxy implements InvocationHandler {

    private Object bdlObject;//被代理对象

    private Object dlObject;//代理对象

    private AspectElement aspectElement;

    public Object bind(Object target, Object bean, AspectElement aspectElement) {
        //定义谁的什么方法被谁代理，之后返回一个proxy生成的对象
        this.bdlObject=target;
        this.dlObject=bean;
        this.aspectElement=aspectElement;
        return Proxy.newProxyInstance(bdlObject.getClass().getClassLoader(),bdlObject.getClass().getInterfaces(),this);
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //aop
        Class clazz=dlObject.getClass();
        Method before=clazz.getDeclaredMethod(this.aspectElement.getBefore(),null);
        before.invoke(this.dlObject,null);
        method.invoke(this.bdlObject,args);
        Method after=clazz.getDeclaredMethod(this.aspectElement.getAfter(),null);
        after.invoke(this.dlObject,null);
        return null;
    }
}
