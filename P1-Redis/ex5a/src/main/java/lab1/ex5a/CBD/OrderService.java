package lab1.ex5a.CBD;

import redis.clients.jedis.Jedis;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class OrderService {

    private static final int LIMIT = 30;
    private static final int TIMESLOT = 3600; 

    public static String placeOrder(Jedis jedis, String username, String productId) {
        String userKey = "user:" + username;

        long currentOrders = jedis.llen(userKey);

        if (currentOrders + 1 > LIMIT) {
            return "Error: Order limit exceeded!";
        }

        String productKey = "product:" + productId;
        if (!jedis.exists(productKey)) {
            return "Error: Product not found!";
        }

        int availableStock;
        try {
            availableStock = Integer.parseInt(jedis.hget(productKey, "quantity"));
        } catch (NumberFormatException e) {
            return "Error: Invalid product!";
        }

        if (availableStock < 1) {
            return "Error: Not enough stock!";
        }

        jedis.hincrBy(productKey, "quantity", -1);

        jedis.rpush(userKey, productId);

        if (jedis.ttl(userKey) == -1) {
            jedis.expire(userKey, TIMESLOT);
        }

        jedis.sadd("registered_users", username);

        return "Order placed successfully!";
    }

    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379); Scanner scanner = new Scanner(System.in)) {
            resetData(jedis);
            initializeProducts(jedis);

            while (true) {
                System.out.print("Enter your username (or type 'list' to see registered users, 'stop' to quit): ");
                String username = scanner.nextLine().trim();

                if (username.isEmpty()) {
                    System.out.println("Username cannot be empty. Please try again.");
                    continue;
                }

                if (username.equalsIgnoreCase("stop")) {
                    break;
                }

                if (username.equalsIgnoreCase("list")) {
                    listRegisteredUsers(jedis);
                    continue;
                }

                System.out.println("Available products:");
                Map<String, String> productIds = jedis.hgetAll("products");
                for (Map.Entry<String, String> entry : productIds.entrySet()) {
                    String productKey = "product:" + entry.getKey();
                    String productName = jedis.hget(productKey, "name");
                    String productQuantity = jedis.hget(productKey, "quantity");
                    System.out.println(entry.getKey() + ". " + productName + " (Stock: " + productQuantity + ")");
                }

                String productId;
                while (true) {
                    System.out.print("Choose the product number: ");
                    productId = scanner.nextLine().trim();

                    if (productId.isEmpty() || !productIds.containsKey(productId)) {
                        System.out.println("Invalid product ID. Please try again.");
                    } else {
                        break;
                    }
                }

                String result = placeOrder(jedis, username, productId);
                System.out.println(result);
            }
        }
    }

    public static void initializeProducts(Jedis jedis) {
        for (int i = 1; i <= 30; i++) {
            String productId = String.valueOf(i);
            String productKey = "product:" + productId;
            String productName = "Product " + i;

            if (!jedis.exists(productKey)) {
                jedis.hset(productKey, "name", productName);
                jedis.hset(productKey, "quantity", "30");
            }

            jedis.hset("products", productId, productName);
        }
    }

    public static void listRegisteredUsers(Jedis jedis) {
        Set<String> registeredUsers = jedis.smembers("registered_users");

        if (registeredUsers.isEmpty()) {
            System.out.println("No users have registered yet.");
            return;
        }

        System.out.println("Registered users:");
        for (String username : registeredUsers) {
            String userKey = "user:" + username;
            long orderCount = jedis.llen(userKey);
            long timeRemaining = jedis.ttl(userKey);

            System.out.println("User: " + username);
            System.out.println("Orders made: " + orderCount);

            if (orderCount >= LIMIT) {
                if (timeRemaining > 0) {
                    System.out.println("Time remaining to place a new order: " + timeRemaining + " seconds");
                } else {
                    System.out.println("You can place a new order now.");
                }
            }

            System.out.println();
        }
    }

    public static void resetData(Jedis jedis) {
        jedis.flushAll();
        System.out.println("Redis data has been reset.");
    }
}
