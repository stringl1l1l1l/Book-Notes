package codes.adapter;

public class Client {
    public static void main(String[] args) {
        BirdAdapter adapter = new BirdAdapter();
        adapter.cry();
        adapter.move();

        System.out.println();

        DogAdapter adapter2 = new DogAdapter();
        adapter2.cry();
        adapter2.move();

        System.out.println();

        DogObjAdapter adapter3 = new DogObjAdapter(new Dog());
        adapter3.cry();
        adapter3.move();
    }
}
