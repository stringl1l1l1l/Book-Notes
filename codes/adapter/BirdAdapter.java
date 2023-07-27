// Bird类适配器
package codes.adapter;

public class BirdAdapter extends Bird implements Robot {

    @Override
    public void cry() {
        System.out.println("机器人模仿鸟叫：");
        super.tweedle();
    }

    @Override
    public void move() {
        System.out.println("机器人模仿鸟跑：");
        super.fly();
    }

}
