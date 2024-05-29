package ordersim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CourierDispatcher {
    private final ShelvesManager shelvesManager;
    private final List<Thread> threads;

    public CourierDispatcher(ShelvesManager shelvesManager) {
        this.shelvesManager = shelvesManager;
        this.threads = new ArrayList<>();
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void dispatchOrder(String id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);
        System.out.println(currentTime + "[CourierDispatcher] Dispatched courier for order: " + id);

        Random random = new Random();
        int timeToArrive = random.nextInt(Config.MAX_TIME_TO_ARRIVE_SECONDS * 1000 - Config.MIN_TIME_TO_ARRIVE_SECONDS * 1000 + 1) +Config.MIN_TIME_TO_ARRIVE_SECONDS * 1000;
        FetchOrderTask fetchOrderTask = new FetchOrderTask(shelvesManager, id, timeToArrive);
        Thread thread = new Thread(fetchOrderTask);
        thread.start();
        threads.add(thread);
    }

}
