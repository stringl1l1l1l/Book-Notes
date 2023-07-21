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
