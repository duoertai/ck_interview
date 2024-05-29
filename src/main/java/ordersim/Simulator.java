package ordersim;

public class Simulator {
    public static void main(String[] args) {
        ShelvesManager shelvesManager = new ShelvesManager(10, 10, 10, 15);
        CourierDispatcher courierDispatcher = new CourierDispatcher(shelvesManager);
        Kitchen kitchen = new Kitchen(shelvesManager);
        OrderGenerator orderGenerator = new OrderGenerator();
        orderGenerator.sendOrderToKitchen(kitchen, courierDispatcher);
    }
}
