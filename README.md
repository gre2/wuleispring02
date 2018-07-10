### containerx学习

1.fileName获取document对象

2.document对象分析bean，构建bean对象

```
1.NodeList beanList = doc.getElementsByTagName("bean");
2.获取node：Node beanNode = beanList.item(beanIndex);
3.取每个beanNode的属性值：NamedNodeMap attrs = beanNode.getAttributes();
4.取bean的id,class
  String attrName = attrs.item(attrIndex).getNodeName();
  String attrValue = attrs.item(attrIndex).getNodeValue();
  if ("id".equals(attrName)) {
      beanElement.setId(attrValue);
  } else if ("class".equals(attrName)){
      beanElement.setClassName(attrValue);
  }
5.NodeList propertyList = beanNode.getChildNodes();
6.NamedNodeMap propertyData = propertyList.item(propIndex).getAttributes();
7.取bean下面的property
  String attrName0 = propertyData.item(i0).getNodeName();
  String attrValue0 = propertyData.item(i0).getNodeValue();
  if ("name".equals(attrName0)) {
      propertyElement.setName(attrValue0);
  } else if ("value".equals(attrName0)) {
      propertyElement.setValue(attrValue0);
  }
8.构建单个bean的实例对象BeanElement
  private String id;
  private String className;
  private Map<String, String> properties;	
9.构建所有bean的实例对象
  Map<String, BeanElement> beanDefinitionMap = new HashMap<String, BeanElement>();
  beanDefinitionMap.put(beanElement.getId(), beanElement);
```

3.把beanDefinitionMap注册到ioc容器中

```
1.循环map
2.反射把BeanElement的className类实例化出来，之后反射此类用BeanElement的property属性去给类成员赋值，返回bean
3.返回处理的bean作为value，key是beanDefinitionMap的id
```

4.使用aop

```
<aop:aspect-list>
	<aop:aspect id="aop1" bean="demoAspect" target="demoService">
		<before>beforeMethod</before>
		<after>afterMethod</after>
	</aop:aspect>
</aop:aspect-list>
```

```
1.NodeList beanList = doc.getElementsByTagName("aop:aspect-list");
2.把每组aop标签转换成string
	<?xml version="1.0" encoding="UTF-8"?>
	<bean id="demoService" class="io.github.flylib.containerx.demo.service.DemoServiceImpl"/>
	<bean id="demoAspect" class="io.github.flylib.containerx.demo.aspect.DemoAspect"/>
	<aop:aspect-list>
		<aop:aspect bean="demoAspect" id="aop1" target="demoService">
			<before>beforeMethod</before>
			<after>afterMethod</after>
		</aop:aspect>
	</aop:aspect-list>
3.xtream转换string变成类对象，返回List<AspectElement>
  @XStreamAlias("aop:aspect")
  public class AspectElement {
      @XStreamAsAttribute()
      @XStreamAlias("id")
      private String id;

  	  @XStreamAsAttribute()
  	  @XStreamAlias("bean")
      private String bean;

      @XStreamAsAttribute()
      @XStreamAlias("target")
      private String target;

      @XStreamAlias("before")
      private String before;

      @XStreamAlias("after")
      private String after;
  }
4.循环list,代理模式返回demoService被代理的对象，invoke根据反射实现具体的方法
5.aop只是在method.invoke方法前后通过反射实现了before，after方法
	//proxy是被代理对象（切面对象），不是重写invoke方法的第一个参数
	Class clazz = this.proxy.getClass();
	// 反射得到操作者的Start方法
	Method onBefore = clazz.getDeclaredMethod(aspectDefinition.getBefore(), null);
	// 反射执行start方法
	onBefore.invoke(this.proxy, null );
	// 执行要处理对象的原本方法
	method.invoke(this.target, args);
	// 反射得到操作者的end方法
	Method onAfter = clazz.getDeclaredMethod(aspectDefinition.getAfter(), null);
	// 反射执行end方法
	onAfter.invoke(this.proxy, null);
6.ioc容器中重新对bean.targert(demoServie)赋值，value是代理对象	
```

