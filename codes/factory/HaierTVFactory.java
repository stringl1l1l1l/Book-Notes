package codes.factory;

public class HaierTVFactory implements TVFactory {
    @Override
    public TV produceTV() {
        System.out.println("生产海尔电视");
        return new HaierTV();
    }
}