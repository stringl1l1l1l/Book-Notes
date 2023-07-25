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
*tips：此阶段通过类加载器完成，用户可以自定义类加载器，灵活地加载字节流文件。*
- **验证**  
1. 文件格式验证
验证字节流是否符合Class文件的规范。
- 是否以魔数0xCAFEBABE开头
- 主、次版本号是否在虚拟机处理范围内
- 各种类型检查
- utf8编码检查
......
2. 元数据验证
对字节码描述的信息进行语义分析，确保符合Java语言规范。
- 是否有父类
- 父类是否继承了不允许继承的final类
- 是否实现所有要求实现的方法
- 类中的字段、方法是否与父类矛盾  
......
3. 字节码验证
通过数据流和控制流分析，确定程序语义的合法性。

4. 符号引用验证  

*tips： 验证阶段是可以跳过的。*

- **准备**  
正式为类变量（static）在方法区分配内存并设置类变量初始值（零值）的阶段。
```
public static int val = 123;        // 准备阶段val为0
public static int final val = 123;  // 准备阶段val为123
```

- **解析**  
JVM将常量池中的符号引用替换为直接引用的阶段。
*tips：符号引用相当于间接访问，直接引用相当于直接访问。*

- **初始化**  
初始化阶段是执行类构造器\<cinit\>()方法的过程，此时才真正开始执行类中定义的Java代码。
\<cinit\>()方法包含static块中的所有语句和所有类变量的赋值操作，执行顺序与代码顺序一致。虚拟机会确保父类的\<cinit\>()方法先调用（接口例外）。
```
static class Parent {
    public static int A = 1;
    static {
        A = 2;    
    }
}

static class Sub extends Parent {
    public static int B = A;
}

public static void main(String[] args) {
    System.out.println(Sub.B); // 由于先执行父类<cinit>方法，输出为2，而不是1
}
```

进行初始化的唯5种场景：  
1）遇到new、getstatic、putstatic、invokestatic字节码指令时  
2）使用java.lang.reflect包的方法进行反射调用时  
3）当初始化一个类时，若父类没有初始化，则先初始化父类  
4）JVM启动时，初始化包含main方法的主类  
5）使用JDK1.7动态语言支持时...  
