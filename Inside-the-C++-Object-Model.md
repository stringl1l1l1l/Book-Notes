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
可见，相比C，C++在布局以及存取时间上的主要额外开销是由Virtual table引起。
该模型的缺点：如果调用object的应用程序代码不改变，但是object本身的非静态成员变量有所修改，应用程序代码也要重新编译。  