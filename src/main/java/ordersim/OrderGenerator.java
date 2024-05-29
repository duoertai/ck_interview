package ordersim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderGenerator {
    private static String filePath = "/Users/ertaiduo/Projects/interview/src/main/resources/orders.json";

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
