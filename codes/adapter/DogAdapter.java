// Dog类适配器
package codes.adapter;

public class DogAdapter extends Dog implements Robot {

    @Override
    public void cry() {
        System.out.println("机器人模仿狗叫：");
        super.wolf();
    }

    @Override
    public void move() {
        System.out.println("机器人模仿狗跑：");
        super.run();
    }

}
