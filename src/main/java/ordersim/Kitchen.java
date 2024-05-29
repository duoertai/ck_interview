package ordersim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Kitchen {
    private ShelvesManager shelvesManager;

    public Kitchen(ShelvesManager shelvesManager) {
        this.shelvesManager = shelvesManager;
    }

    public void receiveOrder(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);

        System.out.println(currentTime + "[Kitchen] Received order: " + order.getId());
        System.out.println(currentTime + "[Kitchen] Finished cooking order: " + order.getId());
        order.setCreatedTime(System.currentTimeMillis());


        // send order to shelf
        AddOrderTask addOrderTask = new AddOrderTask(shelvesManager, order);
        Thread thread = new Thread(addOrderTask);
        thread.start();
    }
}
