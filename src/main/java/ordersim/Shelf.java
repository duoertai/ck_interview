package ordersim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shelf {
    private int capacity;
    private List<Order> orders;

    private int decayModifier;

    public Shelf(int capacity, double decayRate) {
        this.capacity = capacity;
        this.orders = new ArrayList<>();
    }

    public static Shelf createShelf(int capacity, int decayModifier) {
        return new Shelf(capacity, decayModifier);
    }

    public void printOrders() {
        for (Order order : orders) {
            System.out.println(order.getId());
        }
    }

    // if the shelf is full, return false
    public boolean addOrder(Order order) {
        if (orders.size() < capacity) {
            orders.add(order);
            order.setShelfTime(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public Order fetchOrder(String id) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                orders.remove(order);
                return order;
            }
        }
        return null;
    }

    public Order discardOrder() {
        Random random = new Random();
        int index = random.nextInt(orders.size());
        return orders.remove(index);
    }

    public List<Order> cleanup() {
        List<Order> discardedOrders = new ArrayList<>();
        for (Order order : orders) {
            if (getValue(order) <= 0) {
                discardedOrders.add(order);
            }
        }
        orders.removeAll(discardedOrders);
        return discardedOrders;
    }

    public double getValue(Order order) {
        long currentTime = System.currentTimeMillis();
        long shelfTime = order.getShelfTime();
        long createdTime = order.getCreatedTime();
        long shelfLife = order.getShelfLife();
        double decayRate = order.getDecayRate();
        double value = (1.0 * shelfLife - (currentTime - createdTime) - decayRate * (currentTime - shelfTime) * decayModifier) / (1.0 * shelfLife);

        System.out.println("Order id: " + order.getId() + ", Value: " + value);

        return value;
    }
}
