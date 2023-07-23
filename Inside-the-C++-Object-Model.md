# 深度探索C++对象模型  
## 一、关于对象  
#### C++对象模型  
```
class Point {
public:
    Point(float xval);
    virtual ~Point();
    float x() const;
    static int PointCount();

protected:
    virtual ostream& print(ostream& os) const;
    
    float _x;
    static int _point_count;
};
```
![图1.1 C++对象模型](/images/InCpp_1-1.jpg)   
可见，相比C，C++在布局以及存取时间上的主要额外开销是由virtual引起。 
该模型的缺点：如果调用object的应用程序代码不改变，但是object本身的非静态成员变量有所修改，应用程序代码也要重新编译。  

#### 多态的实现
1）隐式转换  
下面的代码可以实现多态，是因为不同类型的指针（引用本质上也是指针）只会影响指针的解释方式，不会影响指针在内存中的状况。
```
shape *ps = new circle();
shape &s = c;
```  
注意，对于如下代码，不会产生任何多态效果，只会导致截断:
```
shape s = c;
```
2）虚函数机制  
```
ps->rotate();
```
3）使用*dynamic_cast*  
```
if ( circle *pc = dynamic_cast< circle * >(ps) )...
```
## 二、构造函数
#### 拷贝构造函数
调用拷贝构造函数的3种情况：  
1）使用对象实例初始化一个对象  
```
class X { ... };
X x;
...
X xx = x;
```
2）对象作为函数参数  
```
extern void foo(X x);
...
X xx;
foo(xx);
```
3）对象作为return的值  
```
X foo() {
    X xx;
    ...
    return xx;
}
```
## 三、成员变量  
- **继承会保证基类在派生类中的原样性**，因此会继承基类的Padding。具体来说，对于如下代码：
```
class Concrete1 {
private:
    int val;
    char bit1;
    // Padding = 3 Bytes
};

class Concrete2 : public Concrete1 {
private:
    char bit2;
    // Padding = 3 Bytes
};

class Concrete3 : public Concrete2 {
private:
    char bit3;
    // Padding = 3 Bytes
};
// sizeof(Concrete1) = 8
// sizeof(Concrete2) = 12
// sizeof(Concrete3) = 16
```
## 四、成员函数
#### non-static成员函数
non-static成员函数扩张为非成员函数的步骤： 
```
float Point3d::magnitude3d() const 
{ 
   return sqrt(_x * _x + _y * _y + _z * _z); 
}
```
1）改写函数原型
```
// non-const member function
float Point3d::magnitude(Point3d *const this)

// const member function
float Point3d::magnitude(const Point3d *const this)
```
2）将对non-static成员变量的存取操作改为使用形参this指针操作
```
float Point3d::magnitude(const Point3d *const this)
{
    return sqrt(
        this->_x * this->_x +
        this->_y * this->_y +
        this->_z * this->_z
    );
}
```
3）将该函数写成外部函数；为确保函数符号唯一性，进行name mangling处理
```
extern float magnitude__7Point3dFv(register Point3d *const this);
```
4）修改所有调用操作
```
obj.magnitude();
        ↓
magnitude__7Point3dFv(&obj);

ptr->magnitude();
        ↓
magnitude__7Point3dFv(ptr);
```
#### virtual成员函数