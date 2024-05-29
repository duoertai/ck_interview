package ordersim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderGenerator {
    private static String filePath = "src/main/resources/orders.txt";
    // read file from /resources/orders.txt
    // return a list of orders
    public static List<Order> generateOrders(String filePath) {
        List<Order> orders = new ArrayList<>();
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String id = parts[0];
                String name = parts[1];
                Temperature temperature = Temperature.fromString(parts[2]);
                int shelfLife = Integer.parseInt(parts[3]);
                double decayRate = Double.parseDouble(parts[4]);
                Order order = new Order(id, name, temperature, shelfLife, decayRate);
                orders.add(order);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return orders;
    }
}
