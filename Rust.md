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
```rust
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
```rust
const THREE_HOURS_IN_SECONDS: u32 = 60 * 60 * 3;
```
- **🧐隐藏 shadowing**  
在rust中，重新对同名变量进行重复声明，新声明的变量会覆盖旧变量，称为隐藏（shadowing）。可以对同一个变量隐藏多次，并且可以使用不同类型进行隐藏。需要注意隐藏是有作用域的。    
```rust
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
```rust
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
```rust
let c = 'z';
let z: char = 'ℤ'; // with explicit type annotation
let heart_eyed_cat = '😻';
```
#### 复合类型  
复合类型可以将多个值组合成一个类型。rust有两个原生的复合类型：元组（tuple）和数组（array）。

- **元组类型**  
元组是一个将多个其他类型的值组合进一个复合类型的主要方式。元组长度固定，一旦声明，其长度不会增大或缩小。
```rust
let tup: (i32, f64, u8) = (500, 6.4, 1);
```
为了从元组中获取单个值，可以使用模式匹配（pattern matching）来解构（destructure）元组值：
```rust
let tup = (500, 6.4, 1);
let (x, y, z) = tup;
```
我们也可以使用点号（.）后跟值的索引来直接访问它们。例如：
```rust
let x: (i32, f64, u8) = (500, 6.4, 1);
let five_hundred = x.0;
let six_point_four = x.1;
let one = x.2;
```
不带任何值的元组有个特殊的名称，叫做**单元（unit）元组**。这种值以及对应的类型都写作 **()**，表示空值或空的返回类型。如果表达式不返回任何其他值，则会隐式返回单元值。

- **数组类型**  
与元组不同，数组中的每个元素的类型必须相同。Rust中的数组长度是固定的。
```rust
let a = [1, 2, 3, 4, 5];
```
可以像这样编写数组的类型：在方括号中包含每个元素的类型，后跟分号，再后跟数组元素的数量：
```rust
let a: [i32; 5] = [1, 2, 3, 4, 5];
```
可以通过在方括号中指定初始值加分号再加元素个数的方式来创建一个每个元素都为相同值的数组：
```rust
let a = [3; 5]; // [3, 3, 3, 3, 3]
```
访问数组元素：
```rust
let a = [1, 2, 3, 4, 5];

let first = a[0];
let second = a[1];
```
🧐在数组中越界访问元素，会导致一个运行时错误，程序立即在错误处退出。在Rust中，这种情况称为*panic*，指程序因为错误而退出的情况。  
### 函数

在 Rust 中通过输入 `fn` 后面跟着函数名和一对圆括号来定义函数。  
Rust 不关心函数定义所在的位置，只要函数被调用时出现在调用之处可见的作用域内就行。  

- **参数**  
  🧐在函数签名中，**必须** 声明每个参数的类型。这是 Rust 设计中一个经过慎重考虑的决定：要求在函数定义中提供类型注解。  

  ```rust
  fn main() {
      print_labeled_measurement(5, 'h');
  }
  
  fn print_labeled_measurement(value: i32, unit_label: char) {
      println!("The measurement is: {value}{unit_label}");
  }
  ```

- **语句和表达式**  
  🧐Rust 是一门基于表达式（expression-based）的语言，这是一个需要理解的不同于其他语言的重要区别。   
  **语句**（*Statements*）是执行一些操作但不返回值的指令。 **表达式**（*Expressions*）计算并产生一个值。  
  
  ```rust
  fn main() {
      let x = (let y = 6);
  }
  _________________________________
  compile error!
  ```

  🧐`let y = 6` 语句并不返回值，所以没有可以绑定到 `x` 上的值。这与其他语言不同，例如 C 和 Ruby，它们的赋值语句会返回所赋的值。在这些语言中，可以这么写 `x = y = 6`，这样 `x` 和 `y` 的值都是 `6`；Rust 中不能这样写。  
  ```rust
  fn main() {
      let y = {
          let x = 3;
          x + 1
      };
  
      println!("The value of y is: {y}");
  }
  ```
  
  🧐注意 `x+1` 这一行在结尾没有分号，与见过的大部分代码行不同。**表达式的结尾没有分号。如果在表达式的结尾加上分号，它就变成了语句，而语句不会返回值。**

- **具有返回值的函数**  
  我们并不对函数返回值命名，但要在箭头（`->`）后声明它的类型。在 Rust 中，函数的返回值等同于函数体最后一个表达式的值。使用 `return` 关键字和指定值，可从函数中提前返回；但大部分函数隐式的返回最后的表达式。  

  ```rust
  fn main() {
      let x = plus_one(5);
      println!("The value of x is: {x}");
  }
  
  fn plus_one(x: i32) -> i32 {
      x + 1
  }
  ```

