# Rust程序设计语言
## cargo基础
- **新建cargo项目**
```
cargo new <project name>  
```
该命令生成的项目目录结构：  
src 源代码目录  
Cargo.toml 记录项目包依赖  
.gitignore  

- **构建cargo项目**
```
cargo build             // debug模式
cargo build −−release   // release模式，速度更快
```
该命令额外生成的文件和目录：  
target  目标文件目录  
Cargo.lock 记录依赖包版本，确保可重现构建  

- **构建并运行cargo项目**
```
cargo run
```

- **检查cargo项目是否有编译错误，不生成可执行文件**
```
cargo check
```

## rust基本语法  
标注🧐代表rust的特性。  
### 变量和可变性
- **变量 variables**  
rust中变量使用*let*关键字创建，默认是不可变的（immutable）。若希望创建可变变量，需添加*mut*关键字。
```
let mut x = 5;
println!("The value of x is: {x}");
x = 6;
println!("The value of x is: {x}");
——————————————————————————————————————
The value of x is: 5
The value of x is: 6
```

- **常量 constants**  
rust中常量使用*const*关键字创建，必须注明值的类型，必须赋值，一定是不可变的，无法添加*mut*关键字。  
```
const THREE_HOURS_IN_SECONDS: u32 = 60 * 60 * 3;
```
- **🧐隐藏 shadowing**  
在rust中，重新对同名变量进行重复声明，新声明的变量会覆盖旧变量，称为隐藏（shadowing）。可以对同一个变量隐藏多次，并且可以使用不同类型进行隐藏。需要注意隐藏是有作用域的。    
```
fn main() {
    let x = 5;
    let x = x + 1;
    {
        let x = x * 2;
        println!("The value of x in the inner scope is: {x}");
    }
    println!("The value of x is: {x}");
}
________________________________________________________________________
The value of x in the inner scope is: 12
The value of x is: 6
```
隐藏和mut的区别：  
```
隐藏：可通过编译
let spaces = "   ";           // string类型
let spaces = spaces.len();    // usize类型

mut：编译错误
let mut spaces = "   ";
let spaces = spaces.len();
```

### 数据类型  
rust的数据类型分为两类：标量（scalar）和复合（compound）。  
#### 标量类型  
四种基本标量类型：整型、浮点型、布尔类型和字符类型。
- **整型**  
i32是整型数字默认类型  
<table>
    <tr>
        <td>长度</td> <td>有符号</td> <td>无符号</td>
    </tr>
     <tr>
        <td>8-bit</td> <td>i8</td> <td>u8</td>
    </tr>
     <tr>
        <td>16-bit</td> <td>i16</td> <td>u16</td>
    </tr>
     <tr>
        <td>32-bit</td> <td>i32</td> <td>u32</td>
    </tr>
     <tr>
        <td>64-bit</td> <td>i64</td> <td>u64</td>
    </tr>
    <tr>
        <td>128-bit</td> <td>i128</td> <td>u128</td>
    </tr>
    <tr>
        <td>arch</td> <td>isize</td> <td>usize</td>
    </tr>
</table>  

<table>
<tr>
     <td>Decimal (十进制)</td>    <td>98_222</td>
</tr>
<tr>
 <td>Hex (十六进制)</td>    <td>0xff</td>
</tr>
<tr>
 <td>Octal (八进制)</td>	    <td>0o77</td>
</tr>
<tr>
 <td>Binary (二进制)</td>    <td>0b1111_0000</td>
</tr>
<tr>
 <td>Byte (单字节字符)(仅限于u8)</td>	 <td>b'A'</td>
</tr>
</table>

注：数字字面量可以添加下划线（_）分割方便阅读，如1000_0000等同于10000000  

- **浮点型**  
IEEE-754标准，f32和f64，默认为f64。

- **布尔型**  
bool类型大小为1个字节。

- **字符型**  
🧐char类型大小为4个字节，代表一个Unicode标量值。
```
let c = 'z';
let z: char = 'ℤ'; // with explicit type annotation
let heart_eyed_cat = '😻';
```
#### 复合类型  
复合类型可以将多个值组合成一个类型。rust有两个原生的复合类型：元组（tuple）和数组（array）。

- **元组类型**  
元组是一个将多个其他类型的值组合进一个复合类型的主要方式。元组长度固定，一旦声明，其长度不会增大或缩小。
```
let tup: (i32, f64, u8) = (500, 6.4, 1);
```
为了从元组中获取单个值，可以使用模式匹配（pattern matching）来解构（destructure）元组值：
```
let tup = (500, 6.4, 1);
let (x, y, z) = tup;
```
我们也可以使用点号（.）后跟值的索引来直接访问它们。例如：
```
let x: (i32, f64, u8) = (500, 6.4, 1);
let five_hundred = x.0;
let six_point_four = x.1;
let one = x.2;
```
不带任何值的元组有个特殊的名称，叫做**单元（unit）元组**。这种值以及对应的类型都写作 **()**，表示空值或空的返回类型。如果表达式不返回任何其他值，则会隐式返回单元值。

- **数组类型**  
与元组不同，数组中的每个元素的类型必须相同。Rust中的数组长度是固定的。
```
let a = [1, 2, 3, 4, 5];
```
可以像这样编写数组的类型：在方括号中包含每个元素的类型，后跟分号，再后跟数组元素的数量：
```
let a: [i32; 5] = [1, 2, 3, 4, 5];
```
可以通过在方括号中指定初始值加分号再加元素个数的方式来创建一个每个元素都为相同值的数组：
```
let a = [3; 5]; // [3, 3, 3, 3, 3]
```
访问数组元素：
```
let a = [1, 2, 3, 4, 5];

let first = a[0];
let second = a[1];
```
🧐在数组中越界访问元素，会导致一个运行时错误，程序立即在错误处退出。在Rust中，这种情况称为*panic*，指程序因为错误而退出的情况。






