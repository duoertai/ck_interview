package ordersim;

public class Order {
    private String id;
    private String name;
    private Temperature temp;
    private int shelfLife;
    private double decayRate;

    public Order(String id, String name, Temperature temperature, int shelfLife, double decayRate) {
        this.id = id;
        this.name = name;
        this.temp = temperature;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Temperature getTemperature() {
        return temp;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public double getDecayRate() {
        return decayRate;
    }
}
