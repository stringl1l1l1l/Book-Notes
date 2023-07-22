# 深入理解Java虚拟机（第2版） 
## 一、JVM运行时内存格局  

![图1.1 JVM运行时内存格局](/images/JVM_1-1.png)  

## 二、Class文件结构  
```
ClassFile {
    u4             magic;                                //魔数，Class文件标志，一般为0xCAFEBABE
    u2             minor_version;                        //JDK次版本号
    u2             major_version;                        //JDK主版本号
    u2             constant_pool_count;                  //常量的数量
    cp_info        constant_pool[constant_pool_count-1]; //常量池
    u2             access_flags;                         //类的访问标志
    u2             this_class;                           //当前类索引
    u2             super_class;                          //父类索引
    u2             interfaces_count;                     //实现接口数量
    u2             interfaces[interfaces_count];         //实现接口的索引集合
    u2             fields_count;                         //类字段数量
    field_info     fields[fields_count];                 //类字段表集合
    u2             methods_count;                        //类方法数量
    method_info    methods[methods_count];               //类方法表集合
    u2             attributes_count;                     //属性数量
    attribute_info attributes[attributes_count];         //属性表集合
}
// 注：因为一个属性不足以描述一个方法，因此一个方法表就对应一个方法，其他同理
```  
#### JVM规范预定义的属性  
![图2.1 JVM规范预定义的属性](/images/JVM_2-1.jpg)  
#### Code属性表  
Code属性是Class文件最重要的属性，存储了编译生成的字节码指令。   
![图2.2 Code属性表结构](/images/JVM_2-2.jpg)  
*tips：注意区分异常表（exception_info）和异常属性（Exceptions）。前者是用于实现try-catch-finally语句的异常处理，后者用于描述throws关键字后列举的可能出现的异常。*   

## 三、JVM类加载机制  
类加载：JVM将Class文件载入内存，并对数据进行校验、解析、初始化，最终形成可被JVM直接使用的Java类型。  
![图3.1 Java类的生命周期](/images/JVM_3-1.jpg)  
- **加载**  
1）通过类的全限定名获取定义此类的二进制字节流  
2）将字节流的静态数据转为方法区的运行时数据  
3）在方法区中生成该类的java.lang.Class对象，作为访问该类的入口  
- **初始化**  
进行初始化的唯5种场景：  
1）遇到new、getstatic、putstatic、invokestatic字节码指令时  
2）使用java.lang.reflect包的方法进行反射调用时  
3）当初始化一个类时，若父类没有初始化，则先初始化父类  
4）JVM启动时，初始化包含main方法的主类  
5）使用JDK1.7动态语言支持时...  
