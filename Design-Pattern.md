# 设计模式（第2版）
## 面向对象设计原则 
1. **单一职责原则 Single Responsibility Principle, SRP**  ⭐⭐⭐⭐  
一个对象应该只包含单一的职责，该职责被完整地封装在一个类中。

2. **开闭原则 Open-Close Principle, OCP** ⭐⭐⭐⭐⭐  
一个软件实体应当对扩展开放，对修改关闭。即设计一个模块时，应当使模块可以在不修改源代码的情况下被扩展。

3. **里氏替换原则 Liskov Substitution Principle, LSP** ⭐⭐⭐⭐  
所有引用基类的地方都可以使用其子类替代。这是开闭原则实现的重要方式之一。

4. **依赖倒转原则 Dependence Inversion Principle, DIP** ⭐⭐⭐⭐⭐  
针对接口或抽象类编程，而不要针对具体实现编程。它是系统抽象化的具体实现。  
相关概念：依赖注入(DI)。

5. **接口隔离原则 Interface Segregation Principle, ISP** ⭐⭐   
接口仅应提供客户端需要的方法。如果接口太大，则需要将它分割成更细小的接口。

6. **合成复用原则 Composite Reuse Principle, CRP** ⭐⭐⭐⭐  
尽量使用对象组合/聚合(Composition/Aggregation)，而不是继承(Inheritance)来达到复用的目的。

7. **迪米特法则 / 最少知识原则 Law of Demeter, LoD / Least Knowledge Principle, LKP** ⭐⭐⭐  
对象只能与它的直接朋友通信。
一个对象的朋友包括：  
(1) 该对象本身  
(2) 以参数形式传入当前对象方法的对象  
(3) 当前对象的成员对象与成员对象集合中的元素  
(4) 当前对象创建的对象  

## 常见设计模式
#### 创建型模式
创建型模式关注对象的创建过程，将对象的创建和使用分离，在使用时无须知道对象的创建细节。

- 工厂模式
工厂模式旨在遵循开闭原则的情况下创建对象，在系统中加入新类型对象时无需修改代码。
![图2.1 工厂模式类图](/images/DP_2-1.jpg)  
```
// 业务逻辑代码
Tv tv;
TVFactory factory;
factory = (TVFactory)XMLUtil.getBean();
tv = factory.produceTV();
tv.play();
```
- 原型模式
原型模式允许一个对象快速复制或再创建另一个可定制的对象，无须知道创建的任何细节。
![图2.2 原型模式类图](/images/DP_2-2.jpg)  

- 单例模式
单例模式确保某一个类只有一个实例，并且自行实例化并向整个系统提供该实例。
![图2.3 单例模式类图](/images/DP_2-3.jpg)  
```
public class Singleton
{
    private static Singleton instance = null;
    private Singleton() {}
    
    // 静态方法，返回唯一实例
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

Singleton s1 = Singleton.getInstance();
Singleton s2 = Singleton.getInstance();
System.out.println(s1 == s2); //true
```
#### 结构型模式
结构型模式描述如何将类或者对象结合在一起形成更大的结构。

- 适配器（Adapter）模式 
适配器模式通过包装，将一个接口转换为所需接口，使不兼容的两个类可以协同工作。
适配器分为类适配器和对象适配器： 
类适配器：
![图2.4 类适配器模式类图](/images/DP_2-4.jpg) 

对象适配器：
![图2.5 对象适配器模式类图](/images/DP_2-5.jpg) 

- 装饰器（Decorator）模式
装饰器模式可以在不需要创造更多子类的情况下，将对象的功能加以扩展，遵循合成复用原则，避免继承破坏类的封装性。
![图2.6 装饰器模式类图](/images/DP_2-6.jpg) 

- 外观（Facade）模式
外观模式为子系统中的一组接口提供一个统一的入口，例如：源站。
![图2.7 外观模式类图](/images/DP_2-7.jpg) 

- 代理（Proxy）模式
代理模式给某一对象提供一个代理，并由代理对象控制对源对象的引用。
![图2.8 代理模式类图](/images/DP_2-8.jpg) 