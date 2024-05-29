package ordersim;

public class Order {
    private String id;
    private String name;
    private Temperature temp;
    private int shelfLife;
    private double decayRate;
    private long createdTime;
    private long shelfTime;

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

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getShelfTime() {
        return shelfTime;
    }

    public void setShelfTime(long shelfTime) {
        this.shelfTime = shelfTime;
    }
}
