package codes.factory;

public class HisenseTVFactory implements TVFactory{
    @Override
    public TV produceTV() {
        System.out.println("生产海信电视");
        return new HisenseTV();
    }
}
