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