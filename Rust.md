# Rust程序设计语言
### cargo基础
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

### rust基本语法  
标注🧐代表rust的特色语法  
#### 变量和可变性
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


