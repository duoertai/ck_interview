package ordersim;

public class AddOrderTask implements Runnable {
    private ShelvesManager shelvesManager;
    private Order order;

    public AddOrderTask(ShelvesManager shelvesManager, Order order) {
        this.shelvesManager = shelvesManager;
        this.order = order;
    }

    @Override
    public void run() {
        shelvesManager.addOrderToShelf(order);
    }
}
