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
> 标注🧐代表相比于C/C++，rust的比较明显的不同之处。  
>
> 标注🤔代表我自己探索的一些坑

### 变量和可变性
#### 变量
🧐rust中变量使用*let*关键字创建，默认是不可变的（immutable）。若希望创建可变变量，需添加*mut*关键字。
```rust
let mut x = 5;
println!("The value of x is: {x}");
x = 6;
println!("The value of x is: {x}");
——————————————————————————————————————
The value of x is: 5
The value of x is: 6
```

#### 常量
rust中常量使用*const*关键字创建，必须注明值的类型，必须赋值，一定是不可变的，无法添加*mut*关键字。  
```rust
const THREE_HOURS_IN_SECONDS: u32 = 60 * 60 * 3;
```
#### 隐藏 shadowing🧐
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
> 数字字面量可以添加下划线（_）分割方便阅读，如1000_0000等同于10000000  

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
🧐不带任何值的元组有个特殊的名称，叫做**单元（unit）元组**。这种值以及对应的类型都写作 `()`，表示空值或空的返回类型。**如果表达式不返回任何其他值，则会隐式返回单元值**。

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

#### 参数  
🧐在函数签名中，**必须** 声明每个参数的类型。这是 Rust 设计中一个经过慎重考虑的决定：要求在函数定义中提供类型注解。  

```rust
fn main() {
    print_labeled_measurement(5, 'h');
}

fn print_labeled_measurement(value: i32, unit_label: char) {
    println!("The measurement is: {value}{unit_label}");
}
```

#### 语句和表达式
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

>🧐注意 `x+1` 这一行在结尾没有分号，与见过的大部分代码行不同。**表达式的结尾没有分号。如果在表达式的结尾加上分号，它就变成了语句，而语句不会返回值。**

#### 具有返回值的函数  
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

### 控制流

#### if表达式

🧐`if`后的条件 **必须** 是 `bool` 值。如果条件不是 `bool` 值，我们将得到一个错误。

- **在let语句中使用if**  
  因为 `if` 是一个表达式，我们可以在 `let` 语句的右侧使用它。

  ```rust
  fn main() {
      let condition = true;
      let number = if condition { 5 } else { 6 };
  
      println!("The value of number is: {number}");
  }
  ```
  记住，代码块的值是其最后一个表达式的值，而数字本身就是一个表达式。在这个例子中，整个 `if` 表达式的值取决于哪个代码块被执行。**这意味着 `if` 的每个分支的可能的返回值都必须是相同类型。**如果它们的类型不匹配，如下面这个例子，则会出现一个错误：  

    ```rust
    fn main() {
        let condition = true;
    
        let number = if condition { 5 } else { "six" };
    
        println!("The value of number is: {number}");
    }
    ```

#### 循环  

- **loop循环（无限循环）**  

  ```rust
  fn main() {
      loop {
          println!("again!");
      }
  }
  ```
  如果将返回值加入你用来停止循环的 `break` 表达式，它会被停止的循环返回：

  ```rust
  fn main() {
      let mut counter = 0;
  
      let result = loop {
          counter += 1;
  
          if counter == 10 {
              break counter * 2;
          }
      };
  
      println!("The result is {result}");
  }
  ___________________________________________
  The result is 20
  ```

- **🧐循环标签**  
  如果存在嵌套循环，`break` 和 `continue` 应用于此时最内层的循环。你可以选择在一个循环上指定一个 **循环标签**（*loop label*），然后将标签与 `break` 或 `continue` 一起使用，使这些关键字应用于已标记的循环而不是最内层的循环。下面是一个包含两个嵌套循环的示例：

  ```rust
  fn main() {
      let mut count = 0;
      'counting_up: loop {
          println!("count = {count}");
          let mut remaining = 10;
  
          loop {
              println!("remaining = {remaining}");
              if remaining == 9 {
                  break;
              }
              if count == 2 {
                  break 'counting_up;
              }
              remaining -= 1;
          }
  
          count += 1;
      }
      println!("End count = {count}");
  }
  ____________________________________________________________
  count = 0
  remaining = 10
  remaining = 9
  count = 1
  remaining = 10
  remaining = 9
  count = 2
  remaining = 10
  End count = 2
  ```
  
- **while循环**  

  ```rust
  fn main() {
      let mut number = 3;
  
      while number != 0 {
          println!("{number}!");
  
          number -= 1;
      }
  
      println!("LIFTOFF!!!");
  }
  __________________________________
  3!
  2!
  1!
  LIFTOFF!!!
  ```
- **for循环**  

  ```rust
  fn main() {
      let a = [10, 20, 30, 40, 50];
  
      for element in a {
          println!("the value is: {element}");
      }
  }
  __________________________________________________
  the value is: 10
  the value is: 20
  the value is: 30
  the value is: 40
  the value is: 50
  ```
  通过使用 range，它是标准库提供的类型，用来生成从一个数字开始到另一个数字之前结束的所有数字的序列，并使用`rev`反转 range：
  ```rust
  fn main() {
      for number in (1..4).rev() {
          println!("{number}!");
      }
      println!("LIFTOFF!!!");
  }
  _______________________________
  3!
  2!
  1!
  LIFTOFF!!!
  ```
  
- ***Range类型***

1. **Range（..）左闭右开**

   ```rust
   fn main() {
       for i in 1..5 {
           println!("{}", i); // 输出1, 2, 3, 4
       }
   }
   ```

2. **RangeInclusive（..=）左闭右闭**

   ```rust
   fn main() {
       for i in 1..=5 {
           println!("{}", i); // 输出1, 2, 3, 4, 5
       }
   }
   ```

3. **RangeTo（..end）0到某个结束值（不包括）**

   ```rust
   fn main() {
       for i in ..5 {
           println!("{}", i); // 输出0, 1, 2, 3, 4
       }
   }
   ```

4. **RangeToInclusive（..=end））0到某个结束值（包括）**

   ```rust
   fn main() {
       for i in ..=5 {
           println!("{}", i); // 输出0, 1, 2, 3, 4, 5
       }
   }
   ```
### 所有权🧐

所有权（*ownership*）是 Rust 最为与众不同的特性，对语言的其他部分有着深刻含义。它让 Rust 无需垃圾回收即可保障内存安全，因此理解 Rust 中所有权如何工作是十分重要的。  
跟踪哪部分代码正在使用堆上的哪些数据，最大限度的减少堆上的重复数据的数量，以及清理堆上不再使用的数据确保不会耗尽空间，这些问题正是所有权系统要处理的。一旦理解了所有权，你就不需要经常考虑栈和堆了，不过明白了**所有权的主要目的就是为了管理堆数据**，能够帮助解释为什么所有权要以这种方式工作。

#### 所有权规则

1. Rust 中的每一个值都有一个 **所有者**（*owner*）。
2. 值在任一时刻有且只有一个所有者。
3. 当所有者（变量）离开作用域，这个值将被丢弃。

#### 内存与分配

内存在拥有它的变量离开作用域后就被自动释放：

```rust
fn main() {
    {
        let s = String::from("hello"); // 从此处起，s 是有效的
        // 使用 s
    }                                  // 此作用域已结束，
                                       // s 不再有效
}
```

当 `s` 离开作用域的时候。当变量离开作用域，Rust 为我们调用一个特殊的函数。这个函数叫做 [`drop`](https://doc.rust-lang.org/std/ops/trait.Drop.html#tymethod.drop)，在这里 `String` 的作者可以放置释放内存的代码。Rust 在结尾的 `}` 处自动调用 `drop`。

这个模式对编写 Rust 代码的方式有着深远的影响。现在它看起来很简单，不过在更复杂的场景下代码的行为可能是不可预测的，比如当有多个变量使用在堆上分配的内存时。现在让我们探索一些这样的场景。
- **变量与数据交互的方式（一）：移动**    
  
  ```rust
  fn main() {
      let s1 = String::from("hello");
      let s2 = s1;
  
      println!("{}, world!", s1);
  
  }
  _______________________________________
  compile error !
  ```
  <img src=".\images\Rust_4-1.png" alt="s1和s2的内存分布" style="zoom: 67%;" />
  `s1` `s2`指向同一块堆内存，当它们离开作用域时，它们都会尝试释放相同的内存。这是一个叫做**二次释放（double free)**的错误。两次释放相同内存会导致内存污染，它可能会导致潜在的安全漏洞。
  为了确保内存安全，在 `let s2 = s1;` 之后，Rust 认为 `s1` 不再有效，因此 Rust 不需要在 `s1` 离开作用域后清理任何东西。这样就解决了我们的问题！因为只有 `s2` 是有效的，当其离开作用域，它就释放自己的内存。

  另外，这里还隐含了一个设计选择：**Rust 永远也不会自动创建数据的 “深拷贝”**。因此，任何 **自动** 的复制可以被认为对运行时性能影响较小。
  
- **变量与数据交互的方式（二）：克隆**  

  ```rust
  fn main() {
      let s1 = String::from("hello");
      let s2 = s1.clone();
  
      println!("s1 = {}, s2 = {}", s1, s2);
  }
  ```

  这段代码能正常运行，这里堆上的数据确实被复制了。当出现 `clone` 调用时，会进行深拷贝。

- **只在栈上的拷贝**  

  ```rust
  fn main() {
      let x = 5;
      let y = x;
  
      println!("x = {}, y = {}", x, y);
  }
  ```

  这段代码似乎与我们刚刚学到的内容相矛盾：没有调用 `clone`，不过 `x` 依然有效且没有被移动到 `y` 中。

  原因是像整型这样的在编译时已知大小的类型被整个存储在栈上，这里没有深浅拷贝的区别，所以这里调用 `clone` 并不会与通常的浅拷贝有什么不同。

  符合该特征的数据类型有：

  - 所有整数类型，比如 `u32`。
  - 布尔类型，`bool`，它的值是 `true` 和 `false`。
  - 所有浮点数类型，比如 `f64`。
  - 字符类型，`char`。
  - 元组，当且仅当其包含的类型也都实现 `Copy` 的时候。比如，`(i32, i32)` 实现了 `Copy`，但 `(i32, String)` 就没有。

#### 隐式移动发生的时机🤔

- match 模式匹配

  ```rust
  fn main() {
      let v: String = String::from("hello");
      let r = &v;
  
      match v {
          value => println!("{v}") // 此处v被移动到value，因此无法打印v
      } // 释放value的堆空间
      println!("{v}") // 此处v内存被释放，同样无法打印
  }
  ```

  ```rust
  fn main() {
      let v: String = String::from("hello");
      let r = &v;
  
      match r { // 替换为引用即可正常运行
          value => println!("{v}") 
      } 
      println!("{v}") 
  }
  ```

  

#### 所有权与函数

将值传递给函数与给变量赋值的原理相似。向函数传递值可能会移动或者复制，就像赋值语句一样。下面的注释展示变量何时进入和离开作用域：

```rust
fn main() {
    let s = String::from("hello");  // s 进入作用域

    takes_ownership(s);             // s 的值移动到函数里 ...
                                    // ... 所以到这里不再有效

    let x = 5;                      // x 进入作用域

    makes_copy(x);                  // x 应该移动函数里，
                                    // 但 i32 是 Copy 的，
                                    // 所以在后面可继续使用 x

} // 这里，x 先移出了作用域，然后是 s。但因为 s 的值已被移走，
  // 没有特殊之处

fn takes_ownership(some_string: String) { // some_string 进入作用域
    println!("{}", some_string);
} // 这里，some_string 移出作用域并调用 `drop` 方法。
  // 占用的内存被释放

fn makes_copy(some_integer: i32) { // some_integer 进入作用域
    println!("{}", some_integer);
} // 这里，some_integer 移出作用域。没有特殊之处
```

#### 引用和借用

**引用**（*reference*）像一个指针，因为它是一个地址，我们可以由此访问储存于该地址的属于其他变量的数据。 与指针不同，引用确保指向某个特定类型的有效值。**引用允许你使用值但不获取其所有权**。

```rust
fn main() {
    let s1 = String::from("hello");

    let len = calculate_length(&s1);

    println!("The length of '{}' is {}.", s1, len);
}

fn calculate_length(s: &String) -> usize {
    s.len()
}
```

<img src=".\images\Rust_4-2.png" alt="引用" style="zoom:67%;" />

同理，函数签名使用 `&` 来表明参数 `s`的类型是一个引用。让我们增加一些解释性的注释：

```rust
fn main() {
    let s1 = String::from("hello");

    let len = calculate_length(&s1);

    println!("The length of '{}' is {}.", s1, len);
}

fn calculate_length(s: &String) -> usize { // s 是 String 的引用
    s.len()
} // 这里，s 离开了作用域。但因为它并不拥有引用值的所有权，
  // 所以什么也不会发生
```

我们将创建一个引用的行为称为 **借用**（*borrowing*）。正如现实生活中，如果一个人拥有某样东西，你可以从他那里借来。当你使用完毕，必须还回去。我们并不拥有它，因此我们无法修改引用的变量：

```rust
fn main() {
    let s = String::from("hello");
    change(&s);
}

fn change(some_string: &String) {
    some_string.push_str(", world");
}
________________________________________
compile error !
```

#### 可变引用

我们通过一个小调整就能修复示例 4-6 代码中的错误，允许我们修改一个借用的值，这就是 **可变引用**（*mutable reference*）：

```rust
fn main() {
    let mut s = String::from("hello");

    change(&mut s);
}

fn change(some_string: &mut String) {
    some_string.push_str(", world");
}
```

可变引用有一个很大的限制：如果你有一个对该变量的可变引用，你就不能使用该变量的另一个可变引用。同时使用两个 `s` 的可变引用的代码会失败：

```rust
fn main() {
    let mut s = String::from("hello");

    let r1 = &mut s;
    let r2 = &mut s;

    println!("{}, {}", r1, r2); // compile error !
    
    println!("{}", r1); // compile error !
    println!("{}", r2); // 可以
}
```

这一限制以一种非常小心谨慎的方式允许可变性，防止同一时间对同一数据存在多个可变引用。新 Rustacean 们经常难以适应这一点，因为大部分语言中变量任何时候都是可变的。这个限制的好处是 Rust 可以在编译时就避免数据竞争。**数据竞争**（*data race*）类似于竞态条件，它可由这三个行为造成：

- 两个或更多指针同时访问同一数据。
- 至少有一个指针被用来写入数据。
- 没有同步数据访问的机制。

数据竞争会导致未定义行为，难以在运行时追踪，并且难以诊断和修复；Rust 避免了这种情况的发生，因为它甚至不会编译存在数据竞争的代码！

一如既往，可以使用大括号来创建一个新的作用域，以允许拥有多个可变引用，只是不能 **同时** 拥有：（写写冲突）

```rust
    let mut s = String::from("hello");

    {
        let r1 = &mut s;
    } // r1 在这里离开了作用域，所以我们完全可以创建一个新的引用

    let r2 = &mut s;
```

我们也不能在拥有不可变引用的同时拥有可变引用。Rust 在同时使用可变与不可变引用时也采用的类似的规则。这些代码会导致一个错误：（读写冲突）

```rust
    let mut s = String::from("hello");

    let r1 = &s; // 没问题
    let r2 = &s; // 没问题
    let r3 = &mut s; // 大问题

    println!("{}, {}, and {}", r1, r2, r3);
____________________________________________________
compile error !
```

#### 悬空引用

在具有指针的语言中，很容易通过释放内存时保留指向它的指针而错误地生成一个 **悬垂指针**（*dangling pointer*），所谓悬垂指针是其指向的内存可能已经被分配给其它持有者。相比之下，在 Rust 中编译器确保引用永远也不会变成悬垂状态：当你拥有一些数据的引用，编译器确保数据不会在其引用之前离开作用域。

让我们尝试创建一个悬垂引用，Rust 会通过一个编译时错误来避免：

文件名：src/main.rs

```rust
fn main() {
    let reference_to_nothing = dangle();
}

fn dangle() -> &String { // dangle 返回一个字符串的引用

    let s = String::from("hello"); // s 是一个新字符串

    &s // 返回字符串 s 的引用
} // 这里 s 离开作用域并被丢弃，其内存被释放，成为悬空引用
________________________________________________________
compile error !
```

这里的解决方法是直接返回 `String`：

```rust
fn no_dangle() -> String {
    let s = String::from("hello");

    s
}
```

#### 切片 Slice🧐

*slice* 允许你引用集合中一段连续的元素序列，而不用引用整个集合。slice 是一类引用，所以它没有所有权。

一个切片引用占用了2个字大小的内存空间( 从现在开始，为了简洁性考虑，如无特殊原因，**我们统一使用切片来特指切片引用** )。 该切片的第一个字是指向数据的指针，第二个字是切片的长度。字的大小取决于处理器架构，例如在 `x86-64` 上，字的大小是 64 位也就是 8 个字节，那么一个切片引用就是 16 个字节大小。

- **字符串slice**   
  **字符串 slice**（*string slice*）是 `String` 中一部分值的引用，它看起来像这样：

  ```rust
      let s = String::from("hello world");
  
      let hello = &s[0..5];
      let world = &s[6..11];
  ```

  对于 Rust 的 `..` range 语法，如果想要从索引 0 开始，可以不写两个点号之前的值。换句话说，如下两个语句是相同的：
  
  ```rust
  let s = String::from("hello");
  
  let slice = &s[0..2];
  let slice = &s[..2];
  ```
  
  依此类推，如果 slice 包含 `String` 的最后一个字节，也可以舍弃尾部的数字。这意味着如下也是相同的：
  
  ```rust
  let s = String::from("hello");
  
  let len = s.len();
  
  let slice = &s[3..len];
  let slice = &s[3..];
  ```
  
  也可以同时舍弃这两个值来获取整个字符串的 slice。所以如下亦是相同的：
  
  ```rust
  let s = String::from("hello");
  
  let len = s.len();
  
  let slice = &s[0..len];
  let slice = &s[..];
  ```
- 🧐**字符串字面值就是 slice**
  
  ```rust
  let s = "Hello, world!";
  ```
  
  这里 s 的类型是 &str：它是一个指向二进制程序特定位置的 slice。这也就是为什么字符串字面值是不可变的：&str 是一个不可变引用。

- **其他类型的slice**

  字符串 slice，正如你想象的那样，是针对字符串的。不过也有更通用的 slice 类型。考虑一下这个数组：

  ```rust
  let a = [1, 2, 3, 4, 5];
  ```

  就跟我们想要获取字符串的一部分那样，我们也会想要引用数组的一部分。我们可以这样做：

  ```rust
  let a = [1, 2, 3, 4, 5];
  
  let slice = &a[1..3];
  
  assert_eq!(slice, &[2, 3]);
  ```

  这个 slice 的类型是 `&[i32]`。它跟字符串 slice 的工作方式一样，通过存储第一个集合元素的引用和一个集合总长度。你可以对其他所有集合使用这类 slice。第八章讲到 vector 时会详细讨论这些集合。
  
  

### 结构体

```rust
struct User {
    active: bool,
    username: String,
    email: String,
    sign_in_count: u64,
}
```
通过为每个字段指定具体值来创建这个结构体的实例。创建一个实例需要以结构体的名字开头，接着在大括号中使用 key: value 键 - 值对的形式提供字段。实例中字段的顺序不需要和它们在结构体中声明的顺序一致。
```rust
fn main() {
    let mut user1 = User {
        active: true,
        username: String::from("someusername123"),
        email: String::from("someone@example.com"),
        sign_in_count: 1,
    };
    
    user1.email = String::from("anotheremail@example.com");
}
```

注意可变性必须作用在整个实例；Rust 并不允许只将某个字段标记为可变。

#### 使用字段初始化

`build_user` 函数获取 email 和用户名并返回 `User` 实例：

```rust
fn build_user(email: String, username: String) -> User {
    User {
        active: true,
        username: username,
        email: email,
        sign_in_count: 1,
    }
}
```

为函数参数起与结构体字段相同的名字是可以理解的，但是不得不重复 `email` 和 `username` 字段名称与变量有些啰嗦。如果结构体有更多字段，重复每个名称就更加烦人了。幸运的是，有一个方便的简写语法， **字段初始化简写语法**：

```rust
fn build_user(email: String, username: String) -> User {
    User {
        active: true,
        username,
        email,
        sign_in_count: 1,
    }
}
```

#### 使用结构体更新语法

使用旧实例的大部分值但改变其部分值来创建一个新的结构体实例通常是很有用的。这可以通过 **结构体更新语法**（*struct update syntax*）实现。

下面演示了如何在 `user2` 中创建一个新 `User` 实例。我们为 `email` 设置了新的值，其他值则使用了实例 5-2 中创建的 `user1` 中的同名值：

```rust
fn main() {
    // --snip--

    let user2 = User {
        active: user1.active,
        username: user1.username,
        email: String::from("another@example.com"),
        sign_in_count: user1.sign_in_count,
    };
}
```

使用结构体更新语法，我们可以通过更少的代码来达到相同的效果，如示例 5-7 所示。`..` 语法指定了剩余未显式设置值的字段应有与给定实例对应字段相同的值。  

```rust
fn main() {
    // --snip--

    let user2 = User {
        email: String::from("another@example.com"),
        ..user1
    };
}
```

🧐注意，结构更新语法就像带有 `=` 的赋值，因为它移动了数据。在这个例子中，总体上说我们在创建 `user2` 后不能就再使用 `user1` 了，因为 `user1` 的 `username` 字段中的 `String` 被移到 `user2` 中。如果我们给 `user2` 的 `email` 和 `username` 都赋予新的 `String` 值，从而只使用 `user1` 的 `active` 和 `sign_in_count` 值，那么 `user1` 在创建 `user2` 后仍然有效。

#### 元组结构体

可以定义与元组类似的结构体，称为 **元组结构体**（*tuple structs*）。元组结构体有着结构体名称提供的含义，但没有具体的字段名，只有字段的类型。当你想给整个元组取一个名字，并使元组成为与其他元组不同的类型时，元组结构体是很有用的，这时像常规结构体那样为每个字段命名就显得多余和形式化了。

要定义元组结构体，以 `struct` 关键字和结构体名开头并后跟元组中的类型。例如，下面是两个分别叫做 `Color` 和 `Point` 元组结构体的定义和用法：

```rust
struct Color(i32, i32, i32);
struct Point(i32, i32, i32);

fn main() {
    let black = Color(0, 0, 0);
    let origin = Point(0, 0, 0);
}
```

#### 类单元结构体

我们也可以定义一个没有任何字段的结构体！它们被称为 **类单元结构体**（*unit-like structs*）类似于 `()`。

```rust
struct AlwaysEqual;

fn main() {
    let subject = AlwaysEqual;
}
```

#### 结构体数据的所有权

在 `User` 结构体的定义中，我们使用了自身拥有所有权的 `String` 类型而不是 `&str` 字符串 slice 类型。这是一个有意而为之的选择，因为我们想要这个结构体拥有它所有的数据，为此只要整个结构体是有效的话其数据也是有效的。

可以使结构体存储被其他对象拥有的数据的引用，不过这么做的话需要用上 **生命周期**（*lifetimes*），这是一个第十章会讨论的 Rust 功能。生命周期确保结构体引用的数据有效性跟结构体本身保持一致。如果你尝试在结构体中存储一个引用而不指定生命周期将是无效的，比如这样：

```rust
struct User {
    active: bool,
    username: &str,
    email: &str,
    sign_in_count: u64,
}

fn main() {
    let user1 = User {
        active: true,
        username: "someusername123",
        email: "someone@example.com",
        sign_in_count: 1,
    };
}
```

编译器会抱怨它需要生命周期标识符：

```console
$ cargo run
   Compiling structs v0.1.0 (file:///projects/structs)
error[E0106]: missing lifetime specifier
 --> src/main.rs:3:15
  |
3 |     username: &str,
  |               ^ expected named lifetime parameter
  |
help: consider introducing a named lifetime parameter
  |
1 ~ struct User<'a> {
2 |     active: bool,
3 ~     username: &'a str,
  |

error[E0106]: missing lifetime specifier
 --> src/main.rs:4:12
  |
4 |     email: &str,
  |            ^ expected named lifetime parameter
  |
help: consider introducing a named lifetime parameter
  |
1 ~ struct User<'a> {
2 |     active: bool,
3 |     username: &str,
4 ~     email: &'a str,
  |

For more information about this error, try `rustc --explain E0106`.
error: could not compile `structs` due to 2 previous errors
```

第十章会讲到如何修复这个问题以便在结构体中存储引用，不过现在，我们会使用像 `String` 这类拥有所有权的类型来替代 `&str` 这样的引用以修正这个错误。

#### 通过派生 trait 增加实用功能🧐

在调试程序时打印出 `Rectangle` 实例来查看其所有字段的值非常有用。示例像前面章节那样尝试使用 [`println!` 宏](https://doc.rust-lang.org/std/macro.println.html)。但这并不行。

```rust
struct Rectangle {
    width: u32,
    height: u32,
}

fn main() {
    let rect1 = Rectangle {
        width: 30,
        height: 50,
    };

    println!("rect1 is {}", rect1);
}
```

当我们运行这个代码时，会出现带有如下核心信息的错误：

```text
error[E0277]: `Rectangle` doesn't implement `std::fmt::Display`
```

`println!` 宏能处理很多类型的格式，**但`{}` 默认告诉 `println!` 使用被称为 `Display` 的格式**，结构体并没有提供一个默认`Display` 实现来使用 `println!` 与 `{}` 占位符。

但是如果我们继续阅读错误，将会发现这个有帮助的信息：

```text
= help: the trait `std::fmt::Display` is not implemented for `Rectangle`
= note: in format strings you may be able to use `{:?}` (or {:#?} for pretty-print) instead
```

**`{:?}` 指示符告诉 `println!` 我们想要使用叫做 `Debug` 的输出格式**。`Debug` 是一个 trait，它允许我们以一种对开发者有帮助的方式打印结构体，以便当我们调试代码时能看到它的值。

这样调整后再次运行程序。见鬼了！仍然能看到一个错误：

```text
error[E0277]: `Rectangle` doesn't implement `Debug`
```

不过编译器又一次给出了一个有帮助的信息：

```text
   = help: the trait `Debug` is not implemented for `Rectangle`
   = note: add `#[derive(Debug)]` to `Rectangle` or manually `impl Debug for Rectangle`
```

Rust确实包含了打印出调试信息的功能，不过我们必须为结构体显式选择这个功能。**为此，在结构体定义之前加上外部属性 `#[derive(Debug)]`**，

文件名：src/main.rs

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}

fn main() {
    let rect1 = Rectangle {
        width: 30,
        height: 50,
    };

    println!("rect1 is {:?}", rect1);
}
```

现在我们再运行这个程序时，就不会有任何错误，并会出现如下输出：

```console
rect1 is Rectangle { width: 30, height: 50 }
```

当我们有一个更大的结构体时，能有更易读一点的输出就好了，**为此可以使用 `{:#?}` 替换 `println!` 字符串中的 `{:?}`增加可读性。**在这个例子中使用 `{:#?}` 风格将会输出如下：

```console
rect1 is Rectangle {
    width: 30,
    height: 50,
}
```

另一种使用 `Debug` 格式打印数值的方法是使用 [`dbg!` 宏](https://doc.rust-lang.org/std/macro.dbg.html)。`dbg!` 宏接收一个表达式的所有权（与 `println!` 宏相反，后者接收的是引用），打印出代码中调用 dbg! 宏时所在的文件和行号，以及该表达式的结果值，**并返回该值的所有权**。

> 注意：调用 `dbg!` 宏会打印到标准错误控制台流（`stderr`），与 `println!` 不同，后者会打印到标准输出控制台流（`stdout`）。

下面是一个例子，我们对分配给 `width` 字段的值以及 `rect1` 中整个结构的值感兴趣。

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}

fn main() {
    let scale = 2;
    let rect1 = Rectangle {
        width: dbg!(30 * scale),
        height: 50,
    };

    dbg!(&rect1);
}
```

我们可以把 `dbg!` 放在表达式 `30 * scale` 周围，因为 `dbg!` 返回表达式的值的所有权，所以 `width` 字段将获得相同的值，就像我们在那里没有 `dbg!` 调用一样。我们不希望 `dbg!` 拥有 `rect1` 的所有权，所以我们在下一次调用 `dbg!` 时传递一个引用。下面是这个例子的输出结果：

```console
[src/main.rs:10] 30 * scale = 60
[src/main.rs:14] &rect1 = Rectangle {
    width: 60,
    height: 50,
}
```

#### 方法

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}

impl Rectangle {
    fn area(&self) -> u32 {
        self.width * self.height
    }
    
    fn can_hold(&self, other: &Rectangle) -> bool {
        self.width > other.width && self.height > other.height
    }
}

fn main() {
    let rect1 = Rectangle {
        width: 30,
        height: 50,
    };

    println!(
        "The area of the rectangle is {} square pixels.",
        rect1.area()
    );
}
```

为了使函数定义于 `Rectangle` 的上下文中，我们开始了一个 `impl` 块，这个 `impl` 块中的所有内容都将与 `Rectangle` 类型相关联。接着将 `area` 函数移动到 `impl` 大括号中，并将签名中的第一个参数和函数体中其他地方的对应参数改成 `self`。

在 `area` 的签名中，使用 `&self` 来替代 `rectangle: &Rectangle`，`&self` 实际上是 `self: &Self` 的缩写。在一个 `impl` 块中，`Self` 类型是 `impl` 块的类型的别名。方法的第一个参数必须有一个名为 `self` 的`Self` 类型的参数，所以 Rust 让你在第一个参数位置上只用 `self` 这个名字来缩写。注意，我们仍然需要在 `self` 前面使用 `&` 来表示这个方法借用了 `Self` 实例。方法可以选择获得 `self` 的所有权，或者像我们这里一样不可变地借用 `self`，或者可变地借用 `self`，就跟其他参数一样。

#### 自动引用和解引用🧐

在 C/C++ 语言中，有两个不同的运算符来调用方法：`.` 直接在对象上调用方法，而 `->` 在一个对象的指针上调用方法，这时需要先解引用（dereference）指针。换句话说，如果 `object` 是一个指针，那么 `object->something()` 就像 `(*object).something()` 一样。

Rust 并没有一个与 `->` 等效的运算符；相反，Rust 有一个叫 **自动引用和解引用**（*automatic referencing and dereferencing*）的功能。方法调用是 Rust 中少数几个拥有这种行为的地方。

它是这样工作的：当使用 `object.something()` 调用方法时，Rust 会自动为 `object` 添加 `&`、`&mut` 或 `*` 以便使 `object` 与方法签名匹配。也就是说，这些代码是等价的：

```rust
p1.distance(&p2);
(&p1).distance(&p2);
```

第一行看起来简洁的多。这种自动引用的行为之所以有效，是因为方法有一个明确的接收者———— `self` 的类型。在给出接收者和方法名的前提下，Rust 可以明确地计算出方法是仅仅读取（`&self`），做出修改（`&mut self`）或者是获取所有权（`self`）。事实上，Rust 对方法接收者的隐式借用让所有权在实践中更友好。

#### 构造函数

所有在 `impl` 块中定义的函数被称为 **关联函数**（*associated functions*），因为它们与 `impl` 后面命名的类型相关。我们可以定义不以 `self` 为第一参数的关联函数（因此不是方法），因为它们并不作用于一个结构体的实例。我们已经使用了一个这样的函数：在 `String` 类型上定义的 `String::from` 函数。

不是方法的关联函数经常被用作返回一个结构体新实例的构造函数。这些函数的名称通常为 `new` ，但 `new` 并不是一个关键字。例如我们可以提供一个叫做 `square` 关联函数，它接受一个维度参数并且同时作为宽和高，这样可以更轻松的创建一个正方形 `Rectangle` 而不必指定两次同样的值：

文件名：src/main.rs

```rust
impl Rectangle {
    fn square(size: u32) -> Self {
        Self {
            width: size,
            height: size,
        }
    }
}
```

关键字 `Self` 在函数的返回类型中代指在 `impl` 关键字后出现的类型，在这里是 `Rectangle`

使用结构体名和 `::` 语法来调用这个关联函数：比如 `let sq = Rectangle::square(3);`。这个函数位于结构体的命名空间中：`::` 语法用于关联函数和模块创建的命名空间。

### 枚举

#### enum枚举

可以通过在代码中定义一个 `IpAddrKind` 枚举来表现这个概念并列出可能的 IP 地址类型，`V4` 和 `V6`。这被称为枚举的 **成员**（*variants*）：

```rust
enum IpAddrKind {
    V4,
    V6,
}
```

现在 `IpAddrKind` 就是一个可以在代码中使用的自定义数据类型了。

可以像这样创建 `IpAddrKind` 两个不同成员的实例：

```rust
    let four = IpAddrKind::V4;
    let six = IpAddrKind::V6;
```

我们还可以直接将数据附加到枚举的每个成员上，这样就不需要一个额外的结构体了。这里也很容易看出枚举工作的另一个细节：每一个我们定义的枚举成员的名字也变成了一个构建枚举的实例的函数。也就是说，`IpAddr::V4()` 是一个获取 `String` 参数并返回 `IpAddr` 类型实例的函数调用。作为定义枚举的结果，这些构造函数会自动被定义。

`IpAddr` 枚举的新定义表明了 `V4` 和 `V6` 成员都关联了 `String` 值：

```rust
    enum IpAddr {
        V4(String),
        V6(String),
    }

    let home = IpAddr::V4(String::from("127.0.0.1"));

    let loopback = IpAddr::V6(String::from("::1"));
```

用枚举替代结构体还有另一个优势：每个成员可以处理不同类型和数量的数据。IPv4 版本的 IP 地址总是含有四个值在 0 和 255 之间的数字部分。如果我们想要将 `V4` 地址存储为四个 `u8` 值而 `V6` 地址仍然表现为一个 `String`，这就不能使用结构体了。枚举则可以轻易的处理这个情况：

```rust
    enum IpAddr {
        V4(u8, u8, u8, u8),
        V6(String),
    }

    let home = IpAddr::V4(127, 0, 0, 1);

    let loopback = IpAddr::V6(String::from("::1"));
```

我们可以将任意类型的数据放入枚举成员中：例如字符串、数字类型或者结构体。甚至可以包含另一个枚举！

```rust
enum Message {
    Quit,
    Move { x: i32, y: i32 },
    Write(String),
    ChangeColor(i32, i32, i32),
}
```

结构体和枚举还有另一个相似点：就像可以使用 `impl` 来为结构体定义方法那样，也可以在枚举上定义方法。这是一个定义于我们 `Message` 枚举上的叫做 `call` 的方法：

```rust
enum Message {
    Quit,
    Move { x: i32, y: i32 },
    Write(String),
    ChangeColor(i32, i32, i32),
}    

impl Message {
    fn call(&self) {
       // 在这里定义方法体
    }
}

let m = Message::Write(String::from("hello"));
m.call();
```

方法体使用了 `self` 来获取调用方法的值。这个例子中，创建了一个值为 `Message::Write(String::from("hello"))` 的变量 `m`，而且这就是当 `m.call()` 运行时 `call` 方法中的 `self` 的值。

#### Option枚举🧐

`Option` 是标准库定义的另一个枚举。`Option` 类型应用广泛因为它编码了一个非常普遍的场景，即一个值要么有值要么没值。

🧐**Rust 并没有很多其他语言中有的空值功能**。**空值**（*Null* ）是一个值，它代表没有值。在有空值的语言中，变量总是这两种状态之一：空值和非空值。Rust 没有空值，不过它确实拥有一个可以编码存在或不存在概念的枚举。这个枚举是 `Option<T>`，而且它[定义于标准库中](https://doc.rust-lang.org/std/option/enum.Option.html)，如下：

```rust
enum Option<T> {
    None,
    Some(T),
}
```

`Option<T>` 枚举是如此有用以至于它甚至被包含在了 prelude 之中，你不需要将其显式引入作用域。另外，它的成员也是如此，可以不需要 `Option::` 前缀来直接使用 `Some` 和 `None`。即便如此 `Option<T>` 也仍是常规的枚举，`Some(T)` 和 `None` 仍是 `Option<T>` 的成员。

`Option` 值的例子：

```rust
    let some_number = Some(5);
    let some_char = Some('e');

    let absent_number: Option<i32> = None;
```

对于 `absent_number`，Rust 需要我们指定 `Option` 整体的类型，因为编译器只通过 `None` 值无法推断出 `Some` 成员保存的值的类型。

如果运行下面的代码，将得到错误信息：

```rust
    let x: i8 = 5;
    let y: Option<i8> = Some(5);

    let sum = x + y;
```

```console
$ cargo run
   Compiling enums v0.1.0 (file:///projects/enums)
error[E0277]: cannot add `Option<i8>` to `i8`
 --> src/main.rs:5:17
  |
5 |     let sum = x + y;
  |                 ^ no implementation for `i8 + Option<i8>`
  |
  = help: the trait `Add<Option<i8>>` is not implemented for `i8`
  = help: the following other types implement trait `Add<Rhs>`:
            <&'a f32 as Add<f32>>
            <&'a f64 as Add<f64>>
            <&'a i128 as Add<i128>>
            <&'a i16 as Add<i16>>
            <&'a i32 as Add<i32>>
            <&'a i64 as Add<i64>>
            <&'a i8 as Add<i8>>
            <&'a isize as Add<isize>>
          and 48 others

For more information about this error, try `rustc --explain E0277`.
error: could not compile `enums` due to previous error
```

错误信息意味着 Rust 不知道该如何将 `Option<i8>` 与 `i8` 相加，因为它们的类型不同。当在 Rust 中拥有一个像 `i8` 这样类型的值时，编译器确保它总是有一个有效的值。我们可以自信使用而无需做空值检查。**只有当使用 `Option<T>`的时候需要担心可能没有值**，而编译器会确保我们在使用值之前处理了为空的情况。换句话说，**在对 `Option<T>` 进行运算之前必须将其转换为 `T`**。通常这能帮助我们捕获到空值最常见的问题之一：假设某值不为空但实际上为空的情况。

总的来说，为了使用 `Option<T>` 值，需要编写处理每个成员的代码。你想要一些代码只当拥有 `Some(T)` 值时运行，允许这些代码使用其中的 `T`。也希望一些代码只在值为 `None` 时运行，这些代码并没有一个可用的 `T` 值。`match` 表达式就是这么一个处理枚举的控制流结构：它会根据枚举的成员运行不同的代码，这些代码可以使用匹配到的值中的数据。

#### match控制流🧐

Rust 有一个叫做 `match` 的极为强大的控制流运算符，它允许我们将一个值与一系列的模式相比较，并根据相匹配的模式执行相应代码。模式可由字面值、变量、通配符和许多其他内容构成。

我们编写一个函数来获取一个未知的硬币，并以一种类似验钞机的方式，确定它是何种硬币并返回它的美分值：

```rust
enum Coin {
    Penny,
    Nickel,
    Dime,
    Quarter,
}

fn value_in_cents(coin: Coin) -> u8 {
    match coin {
        Coin::Penny => 1,
        Coin::Nickel => 5,
        Coin::Dime => 10,
        Coin::Quarter => 25,
    }
}
```

我们列出 `match` 关键字后跟一个表达式，在这个例子中是 `coin` 的值。这看起来非常像 `if` 所使用的条件表达式，不过这里有一个非常大的区别：**对于 `if`，表达式必须返回一个布尔值，而这里它可以是任何类型的**。

当 `match` 表达式执行时，它将结果值按顺序与每一个分支的模式相比较。如果模式匹配了这个值，这个模式相关联的代码将被执行。如果模式并不匹配这个值，将继续执行下一个分支。

如果分支代码较短的话通常不使用大括号，正如示例 6-3 中的每个分支都只是返回一个值。如果想要在分支中运行多行代码，可以使用大括号，而分支后的逗号是可选的。例如，如下代码在每次使用`Coin::Penny` 调用时都会打印出 “Lucky penny!”，同时仍然返回代码块最后的值，`1`：

```rust
fn value_in_cents(coin: Coin) -> u8 {
    match coin {
        Coin::Penny => {
            println!("Lucky penny!");
            1
        }
        Coin::Nickel => 5,
        Coin::Dime => 10,
        Coin::Quarter => 25,
    }
}
```

- **绑定值的模式**

  匹配分支的另一个有用的功能是可以绑定匹配的模式的部分值。这也就是如何从枚举成员中提取值的。

  作为一个例子，让我们修改枚举的一个成员来存放数据。1999 年到 2008 年间，美国在 25 美分的硬币的一侧为 50 个州的每一个都印刷了不同的设计。其他的硬币都没有这种区分州的设计，所以只有这些 25 美分硬币有特殊的价值。可以将这些信息加入我们的 `enum`，通过改变 `Quarter` 成员来包含一个 `State` 值，示例 6-4 中完成了这些修改：

  ```rust
  #[derive(Debug)] // 这样可以立刻看到州的名称
  enum UsState {
      Alabama,
      Alaska,
      // --snip--
  }
  
  enum Coin {
      Penny,
      Nickel,
      Dime,
      Quarter(UsState),
  }
  ```

  示例 6-4：`Quarter` 成员也存放了一个 `UsState` 值的 `Coin` 枚举

  想象一下我们的一个朋友尝试收集所有 50 个州的 25 美分硬币。在根据硬币类型分类零钱的同时，也可以报告出每个 25 美分硬币所对应的州名称，这样如果我们的朋友没有的话，他可以将其加入收藏。

  在这些代码的匹配表达式中，我们在匹配 `Coin::Quarter` 成员的分支的模式中增加了一个叫做 `state` 的变量。当匹配到 `Coin::Quarter` 时，变量 `state` 将会绑定 25 美分硬币所对应州的值。接着在那个分支的代码中使用 `state`，如下：

  ```rust
  fn value_in_cents(coin: Coin) -> u8 {
      match coin {
          Coin::Penny => 1,
          Coin::Nickel => 5,
          Coin::Dime => 10,
          Coin::Quarter(state) => {
              println!("State quarter from {:?}!", state);
              25
          }
      }
  }
  ```

  如果调用 `value_in_cents(Coin::Quarter(UsState::Alaska))`，`coin` 将是 `Coin::Quarter(UsState::Alaska)`。当将值与每个分支相比较时，没有分支会匹配，直到遇到 `Coin::Quarter(state)`。这时，`state` 绑定的将会是值 `UsState::Alaska`。接着就可以在 `println!` 表达式中使用这个绑定了，像这样就可以获取 `Coin` 枚举的 `Quarter` 成员中内部的州的值。

- **匹配 `Option<T>`**

```rust
    fn plus_one(x: Option<i32>) -> Option<i32> {
        match x {
            None => None,
            Some(i) => Some(i + 1),
        }
    }

    let five = Some(5);         
    let six = plus_one(five);	// Some(6)
    let none = plus_one(None);  // None
```

- **匹配是穷尽的**

  `match` 还有另一方面需要讨论：这些分支必须覆盖了所有的可能性。考虑一下 `plus_one` 函数的这个版本，它有一个 bug 并不能编译：

```rust
    fn plus_one(x: Option<i32>) -> Option<i32> {
        match x {
            Some(i) => Some(i + 1),
        }
    }
```

我们没有处理 `None` 的情况，所以这些代码会造成一个 bug。

- **通配模式和 `_` 占位符**

  让我们看一个例子，我们希望对一些特定的值采取特殊操作，而对其他的值采取默认操作。想象我们正在玩一个游戏，如果你掷出骰子的值为 3，角色不会移动，而是会得到一顶新奇的帽子。如果你掷出了 7，你的角色将失去新奇的帽子。对于其他的数值，你的角色会在棋盘上移动相应的格子。这是一个实现了上述逻辑的 `match`，骰子的结果是硬编码而不是一个随机值，其他的逻辑部分使用了没有函数体的函数来表示，实现它们超出了本例的范围：

  ```rust
      let dice_roll = 9;
      match dice_roll {
          3 => add_fancy_hat(),
          7 => remove_fancy_hat(),
          other => move_player(other),
      }
  
      fn add_fancy_hat() {}
      fn remove_fancy_hat() {}
      fn move_player(num_spaces: u8) {}
  ```

  对于前两个分支，匹配模式是字面值 `3` 和 `7`，最后一个分支则涵盖了所有其他可能的值，模式是我们命名为 `other` 的一个变量。`other` 分支的代码通过将其传递给 `move_player` 函数来使用这个变量。

  即使我们没有列出 `u8` 所有可能的值，这段代码依然能够编译，因为最后一个模式将匹配所有未被特殊列出的值。这种通配模式满足了 `match` 必须被穷尽的要求。请注意，**我们必须将通配分支放在最后，因为模式是按顺序匹配的**。

  Rust 还提供了一个模式，当我们不想使用通配模式获取的值时，请使用 `_` ，这是一个特殊的模式，可以匹配任意值而不绑定到该值。这告诉 Rust 我们不会使用这个值，所以 Rust 也不会警告我们存在未使用的变量。

  让我们改变游戏规则：现在，当你掷出的值不是 3 或 7 的时候，你必须再次掷出。这种情况下我们不需要使用这个值，所以我们改动代码使用 `_` 来替代变量 `other` ：

  ```rust
      let dice_roll = 9;
      match dice_roll {
          3 => add_fancy_hat(),
          7 => remove_fancy_hat(),
          _ => reroll(),
      }
  
      fn add_fancy_hat() {}
      fn remove_fancy_hat() {}
      fn reroll() {}
  ```

- **`if let` 简洁控制流** 

  考虑下面的程序，它匹配一个 `config_max` 变量中的 `Option<u8>` 值并只希望当值为 `Some` 成员时执行代码：

  ```rust
      let config_max = Some(3u8);
      match config_max {
          Some(max) => println!("The maximum is configured to be {}", max),
          _ => (),
      }
  ```

  我们可以使用 `if let` 这种更短的方式编写。`if let` 是 `match` 的一个语法糖，它当值匹配某一模式时执行代码而忽略所有其他值。如下代码上面的 `match` 行为一致：

  ```rust
  	let config_max = Some(3u8);
  	if let Some(max) = config_max {
     		println!("The maximum is configured to be {}", max);
  	}
  ```

  可以在 `if let` 中包含一个 `else`。`else` 块中的代码与 `match` 表达式中的 `_` 分支块中的代码相同，这样的 `match` 表达式就等同于 `if let` 和 `else`：

  ```rust
      let mut count = 0;
      match coin {
          Coin::Quarter(state) => println!("State quarter from {:?}!", state),
          _ => count += 1,
      }
  ```
  
  等价于：
  
  ```rust
  	let mut count = 0;
  	if let Coin::Quarter(state) = coin {
      	println!("State quarter from {:?}!", state);
  	} else {
      	count += 1;
  	}
  ```

- **引用解构🤔**

  1. 🌟🌟 使用模式 `&mut V` 去匹配一个可变引用时，你需要格外小心，因为匹配出来的 `V` 是一个值，而不是可变引用

  ```rust
  fn main() {
      let mut v = String::from("hello,");
      let r = &mut v;
  
      match r {
       &mut value => value.push_str(" world!") // 此处value被解构为不可变String类型，报错
      }
  }
  ```

### 模块系统

Rust 有许多功能可以让你管理代码的组织，包括哪些内容可以被公开，哪些内容作为私有部分，以及程序每个作用域中的名字。这些功能。这有时被称为 “模块系统（the module system）”，包括：

- **包**（*Packages*）：Cargo 的一个功能，它允许你构建、测试和分享 crate。
- **Crates** ：一个模块的树形结构，它形成了库或二进制项目。
- **模块**（*Modules*）和 **use**：允许你控制作用域和路径的私有性。
- **路径**（*path*）：一个命名例如结构体、函数或模块等项的方式

本章将会涵盖所有这些概念，讨论它们如何交互，并说明如何使用它们来管理作用域。到最后，你会对模块系统有深入的了解，并且能够像专业人士一样使用作用域！

#### 包和Crate

**crate 是 Rust 在编译时最小的代码单位。**如果你用 `rustc` 而不是 `cargo` 来编译一个文件，编译器还是会将那个文件认作一个 crate。crate 可以包含模块，模块可以定义在其他文件，然后和 crate 一起编译。

crate 有两种形式：**二进制项**和**库**。

**二进制项** 可以被编译为可执行程序，比如一个命令行程序或者一个服务器。它们必须有一个 `main` 函数来定义当程序被执行的时候所需要做的事情。目前我们所创建的 crate 都是二进制项。

**库** 并没有 `main` 函数，它们也不会编译为可执行程序，它们提供一些诸如函数之类的东西，使其他项目也能使用这些东西。比如 [第二章](https://kaisery.github.io/trpl-zh-cn/ch02-00-guessing-game-tutorial.html#生成一个随机数) 的 `rand` crate 就提供了生成随机数的东西。大多数时间 `Rustaceans` 说的 crate 指的都是库，这与其他编程语言中 library 概念一致。

**包**（*package*）是提供一系列功能的一个或者多个 crate。**一个包会包含一个 *Cargo.toml* 文件，阐述如何去构建这些 crate**。Cargo 就是一个包含构建你代码的二进制项的包。Cargo 也包含这些二进制项所依赖的库。其他项目也能用 Cargo 库来实现与 Cargo 命令行程序一样的逻辑。

#### 模块

在本节，我们将讨论模块和其它一些关于模块系统的部分，如允许你命名项的 *路径*（*paths*）；用来将路径引入作用域的 `use` 关键字；以及使项变为公有的 `pub` 关键字。我们还将讨论 `as` 关键字、外部包和 glob 运算符。

这里我们提供一个简单的参考，用来解释模块、路径、`use`关键词和`pub`关键词如何在编译器中工作，以及大部分开发者如何组织他们的代码。我们将在本章节中举例说明每条规则，不过这是一个解释模块工作方式的良好参考。

  - **从 crate 根节点开始**: 当编译一个 crate, 编译器首先在 crate 根文件（通常，对于一个库 crate 而言是*src/lib.rs*，对于一个二进制 crate 而言是*src/main.rs*）中寻找需要被编译的代码。

  - **声明模块**: 在 crate 根文件中，你可以声明一个新模块；比如，你用`mod garden`声明了一个叫做`garden`的模块。编译器会在下列路径中寻找模块代码：
    - 内联，在大括号中，当`mod garden`后方不是一个分号而是一个大括号
    - 在文件 *src/garden.rs*
    - 在文件 *src/garden/mod.rs*
    
  - **声明子模块**: 在除了 crate 根节点以外的其他文件中，你可以定义子模块。比如，你可能在src/garden.rs中定义了`mod vegetables;`。编译器会在以父模块命名的目录中寻找子模块代码：
    - 内联，在大括号中，当`mod vegetables`后方不是一个分号而是一个大括号
    - 在文件 *src/garden/vegetables.rs*
    - 在文件 *src/garden/vegetables/mod.rs*
    
  - **模块中的代码路径**: 一旦一个模块是你 crate 的一部分，你可以在隐私规则允许的前提下，从同一个 crate 内的任意地方，通过代码路径引用该模块的代码。举例而言，一个 garden vegetables 模块下的`Asparagus`类型可以在`crate::garden::vegetables::Asparagus`被找到。

  - **私有 vs 公用**: 一个模块里的代码默认对其父模块私有。为了使一个模块公用，应当在声明时使用`pub mod`替代`mod`。为了使一个公用模块内部的成员公用，应当在声明前使用`pub`。

  - **`use` 关键字**: 在一个作用域内，`use`关键字创建了一个成员的快捷方式，用来减少长路径的重复。在任何可以引用`crate::garden::vegetables::Asparagus`的作用域，你可以通过 `use crate::garden::vegetables::Asparagus;`创建一个快捷方式，然后你就可以在作用域中只写`Asparagus`来使用该类型。

#### 在模块中对相关代码进行分组

*模块* 让我们可以将一个 crate 中的代码进行分组，以提高可读性与重用性。因为一个模块中的代码默认是私有的，所以还可以利用模块控制项的 *私有性*。私有项是不可为外部使用的内在详细实现。我们也可以将模块和它其中的项标记为公开的，这样，外部代码就可以使用并依赖与它们。

在餐饮业，餐馆中会有一些地方被称之为 *前台*（*front of house*），还有另外一些地方被称之为 *后台*（*back of house*）。前台是招待顾客的地方，在这里，店主可以为顾客安排座位，服务员接受顾客下单和付款，调酒师会制作饮品。后台则是由厨师工作的厨房，洗碗工的工作地点，以及经理做行政工作的地方组成。

我们可以将函数放置到嵌套的模块中，来使我们的 crate 结构与实际的餐厅结构相同。通过执行 `cargo new --lib restaurant`，来创建一个新的名为 `restaurant` 的库。然后将示例 7-1 中所罗列出来的代码放入 *src/lib.rs* 中，来定义一些模块和函数。

文件名：src/lib.rs

```rust
mod front_of_house {
    mod hosting {
        fn add_to_waitlist() {}

        fn seat_at_table() {}
    }

    mod serving {
        fn take_order() {}

        fn serve_order() {}

        fn take_payment() {}
    }
}
```

示例 7-1：一个包含了其他内置了函数的模块的 `front_of_house` 模块

我们定义一个模块，是以 `mod` 关键字为起始，然后指定模块的名字（本例中叫做 `front_of_house`），并且用花括号包围模块的主体。在模块内，我们还可以定义其他的模块，就像本例中的 `hosting` 和 `serving` 模块。模块还可以保存一些定义的其他项，比如结构体、枚举、常量、特性、或者函数。

下面展示了示例 7-1 中的模块树的结构：

```text
crate
 └── front_of_house
     ├── hosting
     │   ├── add_to_waitlist
     │   └── seat_at_table
     └── serving
         ├── take_order
         ├── serve_order
         └── take_payment
```

#### 引用模块项目的路径

来看一下 Rust 如何在模块树中找到一个项的位置，我们使用路径的方式，就像在文件系统使用路径一样。为了调用一个函数，我们需要知道它的路径。

路径有两种形式：

- **绝对路径**（*absolute path*）是以 crate 根（root）开头的全路径；对于外部 crate 的代码，是以 crate 名开头的绝对路径，对于当前 crate 的代码，则以字面值 `crate` 开头。
- **相对路径**（*relative path*）从当前模块开始，以 `self`、`super` 或当前模块的标识符开头。

绝对路径和相对路径都后跟一个或多个由双冒号（`::`）分割的标识符。

我们在 crate 根定义了一个新函数 `eat_at_restaurant`，并在其中展示调用 `add_to_waitlist` 函数的两种方法。`eat_at_restaurant` 函数是我们 crate 库的一个公共 API，所以我们使用 `pub` 关键字来标记它。注意，这个例子无法编译通过，我们稍后会解释原因。

```rust
mod front_of_house {
    mod hosting {
        fn add_to_waitlist() {}
    }
}

pub fn eat_at_restaurant() {
    // 绝对路径
    crate::front_of_house::hosting::add_to_waitlist();

    // 相对路径
    front_of_house::hosting::add_to_waitlist();
}
```

错误信息说 `hosting` 模块是私有的。换句话说，我们拥有 `hosting` 模块和 `add_to_waitlist` 函数的的正确路径，但是 Rust 不让我们使用，因为它不能访问私有片段。**在 Rust 中，默认所有项（函数、方法、结构体、枚举、模块和常量）对父模块都是私有的。**如果希望创建一个私有函数或结构体，你可以将其放入一个模块。

```rust
mod front_of_house {
    pub mod hosting {
        pub fn add_to_waitlist() {}
    }
}

pub fn eat_at_restaurant() {
    // 绝对路径
    crate::front_of_house::hosting::add_to_waitlist();

    // 相对路径
    front_of_house::hosting::add_to_waitlist();
}
```

现在代码可以编译通过了！模块公有并不使其内容也是公有的。模块上的 `pub` 关键字只允许其父模块引用它，而不允许访问内部代码。因为模块是一个容器，只是将模块变为公有能做的其实并不太多；同时需要更深入地选择将一个或多个项变为公有。



> #### 二进制和库 crate 包的最佳实践
>
> 我们提到过包可以同时包含一个 *src/main.rs* 二进制 crate 根和一个 *src/lib.rs* 库 crate 根，并且这两个 crate 默认以包名来命名。通常，这种包含二进制 crate 和库 crate 的模式的包，在二进制 crate 中只有足够的代码来启动一个可执行文件，可执行文件调用库 crate 的代码。又因为库 crate 可以共享，这使得其它项目从包提供的大部分功能中受益。
>
> 模块树应该定义在 *src/lib.rs* 中。这样通过以包名开头的路径，公有项就可以在二进制 crate 中使用。二进制 crate 就完全变成了同其它 外部 crate 一样的库 crate 的用户：它只能使用公有 API。这有助于你设计一个好的 API；你不仅仅是作者，也是用户！

#### super 开始的相对路径

我们可以通过在路径的开头使用 `super` ，从父模块开始构建相对路径，而不是从当前模块或者 crate 根开始。这类似以 `..` 语法开始一个文件系统路径。

考虑一下下面的代码，它模拟了厨师更正了一个错误订单，并亲自将其提供给客户的情况。`back_of_house` 模块中的定义的 `fix_incorrect_order` 函数通过指定的 `super` 起始的 `serve_order` 路径，来调用父模块中的 `deliver_order` 函数：

```rust
fn deliver_order() {}

mod back_of_house {
    fn fix_incorrect_order() {
        cook_order();
        super::deliver_order();
    }

    fn cook_order() {}
}
```

因为我们创建了名为 `Appetizer` 的公有枚举，所以我们可以在 `eat_at_restaurant` 中使用 `Soup` 和 `Salad` 成员。

#### 创建公有的结构体和枚举

如果枚举成员不是公有的，那么枚举会显得用处不大；给枚举的所有成员挨个添加 `pub` 是很令人恼火的，因此枚举成员默认就是公有的。结构体通常使用时，不必将它们的字段公有化，因此结构体遵循常规，内容全部是私有的，除非使用 `pub` 关键字。

```rust
mod back_of_house {
    pub enum Appetizer {
        Soup,
        Salad,
    }
}

pub fn eat_at_restaurant() {
    let order1 = back_of_house::Appetizer::Soup;
    let order2 = back_of_house::Appetizer::Salad;
}
```

#### 使用 `use` 关键字将路径引入作用域

不得不编写路径来调用函数显得不便且重复。在示例中，无论我们选择 `add_to_waitlist` 函数的绝对路径还是相对路径，每次我们想要调用 `add_to_waitlist` 时，都必须指定`front_of_house` 和 `hosting`。幸运的是，有一种方法可以简化这个过程。我们可以使用 `use` 关键字创建一个短路径，然后就可以在作用域中的任何地方使用这个更短的名字。

在示例中，我们将 `crate::front_of_house::hosting` 模块引入了 `eat_at_restaurant` 函数的作用域，而我们只需要指定 `hosting::add_to_waitlist` 即可在 `eat_at_restaurant` 中调用 `add_to_waitlist` 函数。

```rust
mod front_of_house {
    pub mod hosting {
        pub fn add_to_waitlist() {}
    }
}

use crate::front_of_house::hosting;

pub fn eat_at_restaurant() {
    hosting::add_to_waitlist();
}
```

在作用域中增加 `use` 和路径类似于在文件系统中创建软连接（符号连接，symbolic link）。通过在 crate 根增加 `use crate::front_of_house::hosting`，现在 `hosting` 在作用域中就是有效的名称了，如同 `hosting` 模块被定义于 crate 根一样。通过 `use` 引入作用域的路径也会检查私有性，同其它路径一样。

注意 `use` 只能创建 `use` 所在的特定作用域内的短路径。下面的示例将 `eat_at_restaurant` 函数移动到了一个叫 `customer` 的子模块，这又是一个不同于 `use` 语句的作用域，所以函数体不能编译。

```rust
mod front_of_house {
    pub mod hosting {
        pub fn add_to_waitlist() {}
    }
}

use crate::front_of_house::hosting;

mod customer {
    pub fn eat_at_restaurant() {
        hosting::add_to_waitlist();
    }
}
```

#### 使用 `as` 关键字提供新的名称

使用 `use` 将两个同名类型引入同一作用域这个问题还有另一个解决办法：在这个类型的路径后面，我们使用 `as` 指定一个新的本地名称或者别名。示例展示了另一个编写的方法，通过 `as` 重命名其中一个 `Result` 类型。

```rust
use std::fmt::Result;
use std::io::Result as IoResult;

fn function1() -> Result {
    // --snip--
}

fn function2() -> IoResult<()> {
    // --snip--
}
```

#### 使用 `pub use` 重导出名称

使用 `use` 关键字，将某个名称导入当前作用域后，这个名称在此作用域中就可以使用了，但它对此作用域之外还是私有的。如果想让其他人调用我们的代码时，也能够正常使用这个名称，就好像它本来就在当前作用域一样，那我们可以将 `pub` 和 `use` 合起来使用。这种技术被称为 “**重导出**（*re-exporting*）”：我们不仅将一个名称导入了当前作用域，还允许别人把它导入他们自己的作用域。

示例 7-17 将示例 7-11 根模块中的 `use` 改为 `pub use` 。

文件名：src/lib.rs

```rust
mod front_of_house {
    pub mod hosting {
        pub fn add_to_waitlist() {}
    }
}

pub use crate::front_of_house::hosting;

pub fn eat_at_restaurant() {
    hosting::add_to_waitlist();
}
```

在这个修改之前，外部代码需要使用路径 `restaurant::front_of_house::hosting::add_to_waitlist()` 来调用 `add_to_waitlist` 函数。现在这个 `pub use` 从根模块重导出了 `hosting` 模块，外部代码现在可以使用路径 `restaurant::hosting::add_to_waitlist`。

#### 使用外部包

在第二章中我们编写了一个猜猜看游戏。那个项目使用了一个外部包，`rand`，来生成随机数。为了在项目中使用 `rand`，在 *Cargo.toml* 中加入了如下行：

文件名：Cargo.toml

```toml
rand = "0.8.5"
```

在 *Cargo.toml* 中加入 `rand` 依赖告诉了 Cargo 要从 [crates.io](https://crates.io/) 下载 `rand` 和其依赖，并使其可在项目代码中使用。

接着，为了将 `rand` 定义引入项目包的作用域，我们加入一行 `use` 起始的包名，它以 `rand` 包名开头并列出了需要引入作用域的项。回忆一下第二章的 “生成一个随机数” 部分，我们曾将 `Rng` trait 引入作用域并调用了 `rand::thread_rng` 函数：

```rust
use rand::Rng;

fn main() {
    let secret_number = rand::thread_rng().gen_range(1..=100);
}
```

#### 嵌套路径来消除大量的 `use` 行

当需要引入很多定义于相同包或相同模块的项时，为每一项单独列出一行会占用源码很大的空间。例如猜猜看章节示例 2-4 中有两行 `use` 语句都从 `std` 引入项到作用域：

文件名：src/main.rs

```rust
// --snip--
use std::cmp::Ordering;
use std::io;
// --snip--
```

相反，我们可以使用嵌套路径将相同的项在一行中引入作用域。这么做需要指定路径的相同部分，接着是两个冒号，接着是大括号中的各自不同的路径部分，如示例 7-18 所示。

文件名：src/main.rs

```rust
// --snip--
use std::{cmp::Ordering, io};
// --snip--
```

示例 7-18: 指定嵌套的路径在一行中将多个带有相同前缀的项引入作用域

在较大的程序中，使用嵌套路径从相同包或模块中引入很多项，可以显著减少所需的独立 `use` 语句的数量！

我们可以在路径的任何层级使用嵌套路径，这在组合两个共享子路径的 `use` 语句时非常有用。例如，示例 7-19 中展示了两个 `use` 语句：一个将 `std::io` 引入作用域，另一个将 `std::io::Write` 引入作用域：

```rust
use std::io;
use std::io::Write;
```

示例 7-19: 通过两行 `use` 语句引入两个路径，其中一个是另一个的子路径

两个路径的相同部分是 `std::io`，这正是第一个路径。为了在一行 `use` 语句中引入这两个路径，可以在嵌套路径中使用 `self`，如示例 7-20 所示。

文件名：src/lib.rs

```rust
use std::io::{self, Write};
```

示例 7-20: 将示例 7-19 中部分重复的路径合并为一个 `use` 语句

这一行便将 `std::io` 和 `std::io::Write` 同时引入作用域。

#### 通过`*`运算符将所有的公有定义引入作用域

如果希望将一个路径下 **所有** 公有项引入作用域，可以指定路径后跟 `*`，glob 运算符：

```rust
use std::collections::*;
```

这个 `use` 语句将 `std::collections` 中定义的所有公有项引入当前作用域。

#### 将模块拆分成多个文件

我们从示例 7-17 中包含多个餐厅模块的代码开始。我们会将模块提取到各自的文件中，而不是将所有模块都定义到 crate 根文件中。在这里，crate 根文件是 *src/lib.rs*，不过这个过程也适用于 crate 根文件是 *src/main.rs* 的二进制 crate。

首先将 `front_of_house` 模块提取到其自己的文件中。删除 `front_of_house` 模块的大括号中的代码，只留下 `mod front_of_house;` 声明，这样 *src/lib.rs* 会包含如示例 7-21 所示的代码。注意直到创建示例 7-22 中的 *src/front_of_house.rs* 文件之前代码都不能编译。

文件名：src/lib.rs

```rust
mod front_of_house;

pub use crate::front_of_house::hosting;

pub fn eat_at_restaurant() {
    hosting::add_to_waitlist();
}
```

示例 7-21: 声明 `front_of_house` 模块，其内容将位于 *src/front_of_house.rs*

接下来将之前大括号内的代码放入一个名叫 *src/front_of_house.rs* 的新文件中，如示例 7-22 所示。因为编译器找到了 crate 根中名叫 `front_of_house` 的模块声明，它就知道去搜寻这个文件。

文件名：src/front_of_house.rs

```rust
pub mod hosting {
    pub fn add_to_waitlist() {}
}
```

示例 7-22: 在 *src/front_of_house.rs* 中定义 `front_of_house` 模块

注意你只需在模块树中的某处使用一次 `mod` 声明就可以加载这个文件。一旦编译器知道了这个文件是项目的一部分（并且通过 `mod` 语句的位置知道了代码在模块树中的位置），项目中的其他文件应该使用其所声明的位置的路径来引用那个文件的代码，这在[“引用模块项目的路径”](https://kaisery.github.io/trpl-zh-cn/ch07-03-paths-for-referring-to-an-item-in-the-module-tree.html)部分有讲到。**换句话说，`mod` 不是你可能会在其他编程语言中看到的 "include" 操作。**

> #### 另一种文件路径
>
> 目前为止我们介绍了 Rust 编译器所最常用的文件路径；不过一种更老的文件路径也仍然是支持的。
>
> 对于声明于 crate 根的 `front_of_house` 模块，编译器会在如下位置查找模块代码：
>
> - *src/front_of_house.rs*（我们所介绍的）
> - *src/front_of_house/mod.rs*（老风格，不过仍然支持）
>
> 对于 `front_of_house` 的子模块 `hosting`，编译器会在如下位置查找模块代码：
>
> - *src/front_of_house/hosting.rs*（我们所介绍的）
> - *src/front_of_house/hosting/mod.rs*（老风格，不过仍然支持）
>
> 如果你对同一模块同时使用这两种路径风格，会得到一个编译错误。在同一项目中的不同模块混用不同的路径风格是允许的，不过这会使他人感到疑惑。
>
> 使用 *mod.rs* 这一文件名的风格的主要缺点是会导致项目中出现很多 *mod.rs* 文件，当你在编辑器中同时打开它们时会感到疑惑。