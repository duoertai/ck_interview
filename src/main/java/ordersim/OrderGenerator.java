package ordersim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderGenerator {
    // TODO: move to config file
    private static String filePath = "/Users/ertaiduo/Projects/interview/src/main/resources/orders.json";
    private static int ingestionRate = 2;

    public static List<Order> generateOrders(String filePath) {
        List<Order> orders = new ArrayList<>();
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            StringBuilder jsonStr = new StringBuilder();

            while (scanner.hasNextLine()) {
                jsonStr.append(scanner.nextLine());
            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Temperature.class, (JsonDeserializer<Temperature>) (json, typeOfT, context) -> Temperature.fromString(json.getAsString()))
                    .create();
            Type orderListType = new TypeToken<ArrayList<Order>>(){}.getType();
            orders = gson.fromJson(jsonStr.toString(), orderListType);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public void sendOrderToKitchen(Kitchen kitchen, CourierDispatcher courierDispatcher) {
        List<Order> orders = generateOrders(filePath);
        double interval = 1000 / ingestionRate;

        try {
            for (Order order : orders) {
                Thread.sleep((long) interval);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTime = LocalDateTime.now().format(formatter);

                System.out.println(currentTime + "[OrderGenerator] Sending order: " + order.getId() + " to kitchen");
                kitchen.receiveOrder(order);
                courierDispatcher.dispatchOrder(order.getId());
            }

            List<Thread> threads = courierDispatcher.getThreads();
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Order> orders = generateOrders(filePath);
        for (Order order : orders) {
            System.out.println(order.getId());
            System.out.println(order.getName());
            System.out.println(order.getTemperature());
            System.out.println(order.getShelfLife());
            System.out.println(order.getDecayRate());
            System.out.println();
        }
    }
}
