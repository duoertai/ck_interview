package ordersim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FetchOrderTask implements Runnable {
    private ShelvesManager shelvesManager;
    private String id;
    private int timeToArrive;

    public FetchOrderTask(ShelvesManager shelvesManager, String id, int timeToArrive) {
        this.shelvesManager = shelvesManager;
        this.id = id;
        this.timeToArrive = timeToArrive;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeToArrive);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);

        System.out.println(currentTime + "[CourierDispatcher] Courier arrived for order: " + id);
        Order order = shelvesManager.fetchOrderFromShelf(id);

        currentTime = LocalDateTime.now().format(formatter);
        if(order == null) {
            System.out.println(currentTime + "[FetchOrderTask] Order not found: " + id);
        } else {
            System.out.println(currentTime + "[FetchOrderTask] Fetched order: " + order.getId());
            System.out.println(currentTime + "[FetchOrderTask] Delivered order: " + order.getId());
        }
    }
}
