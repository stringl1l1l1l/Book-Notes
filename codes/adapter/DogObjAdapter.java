// Dog对象适配器
package codes.adapter;

public class DogObjAdapter implements Robot {

    private Dog dog;

    public DogObjAdapter(Dog dog) {
        this.dog = dog;
    }

    @Override
    public void cry() {
        System.out.println("机器人模仿狗叫：");
        dog.wolf();
    }

    @Override
    public void move() {
        System.out.println("机器人模仿狗跑：");
        dog.run();
    }

}
