package ordersim;

public class Simulator {
    public static void main(String[] args) {
        ShelvesManager shelvesManager = new ShelvesManager(
                Config.HOT_SHELF_CAPACITY,
                Config.COLD_SHELF_CAPACITY,
                Config.FROZEN_SHELF_CAPACITY,
                Config.OVERFLOW_SHELF_CAPACITY
        );
        CourierDispatcher courierDispatcher = new CourierDispatcher(shelvesManager);
        Kitchen kitchen = new Kitchen(shelvesManager);
        OrderGenerator orderGenerator = new OrderGenerator();
        orderGenerator.sendOrderToKitchen(kitchen, courierDispatcher);
    }
}
