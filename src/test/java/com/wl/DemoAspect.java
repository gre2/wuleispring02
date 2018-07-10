package com.wl;

public class DemoAspect {


	public void beforeMethod() {
		System.out.println("---aop log-" + " before---");
	}
	
	public void afterMethod() {
		 System.out.println("---aop log-" + " after---");
	}
}
