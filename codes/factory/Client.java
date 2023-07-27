package codes.factory;

public class Client {
    public static void main(String[] args) {
        TVFactory factory = new HisenseTVFactory();
        TV tv = factory.produceTV();
        tv.play();
    }
}
