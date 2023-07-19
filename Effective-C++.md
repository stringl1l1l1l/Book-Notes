# Effective C++ (3rd Edition)
1. **视C++为一个语言联邦**   
- Cpp = C + Object-Orient Cpp + Template Cpp + STL
2. **尽量以*const*,*enum*,*inline*替换 *#define***  
- *#define*替换在预处理阶段执行，因此不会出现在符号表中，对于重构与调试非常不便
- *#define*没有作用域的概念
- *#define*被原封不动地替换为对应的值，因此定义的宏可能出现意想不到的错误：
```
#define CALL_WITH_MAX(a, b) f((a) > (b) ? (a) : (b))
...
int a = 5, b = 0;
CALL_WITH_MAX(++a, b);       // a被累加2次 
CALL_WITH_MAX(++a, b + 10);  // a被累加1次
```
- **enum hack:** 一个枚举类型的数值可以充当*int*类型使用
```
class GamePlayer {
private:
  enum { NumTurns = 5 };

  int scores[NumTurns];
  ...
}
```
3. **Use *const* whenever possible 尽可能使用 *const***
