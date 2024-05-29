package ordersim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shelf {
    private int capacity;
    private List<Order> orders;

    public Shelf(int capacity) {
        this.capacity = capacity;
        this.orders = new ArrayList<>();
    }

    public static Shelf createShelf(int capacity) {
        return new Shelf(capacity);
    }

    // if the shelf is full, return false
    public boolean addOrder(Order order) {
        if (orders.size() < capacity) {
            orders.add(order);
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
}
