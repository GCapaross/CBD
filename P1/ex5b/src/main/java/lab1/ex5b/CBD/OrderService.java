package lab1.ex5b.CBD;

import redis.clients.jedis.Jedis;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class OrderService {

    private static final int LIMIT = 30;
    private static final int TIMESLOT = 3600; // 60 minutes in seconds

    public static String placeOrder(Jedis jedis, String username, String productId, int quantity) {
        String userKey = "user:" + username;

        long currentOrders = jedis.llen(userKey);

        if (currentOrders + quantity > LIMIT) {
            return "Error: Order limit exceded!";
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

        if (availableStock < quantity) {
            return "Error: Not enough stock!";
        }

        jedis.hincrBy(productKey, "quantity", -quantity);

        for (int i = 0; i < quantity; i++) {
            jedis.rpush(userKey, productId);
        }

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

                int quantity = 0;
                while (true) {
                    System.out.print("Enter the quantity: ");
                    String quantityInput = scanner.nextLine().trim();

                    try {
                        quantity = Integer.parseInt(quantityInput);
                        if (quantity <= 0) {
                            System.out.println("Quantity must be a positive number. Please try again.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity. Please enter a valid number.");
                    }
                }

                String result = placeOrder(jedis, username, productId, quantity);
                System.out.println(result);
            }
        }
    }

    public static void initializeProducts(Jedis jedis) {
        // Decided to use a map for the products
        Map<String, String> products = Map.of(
                "1", "Laptop",
                "2", "Smartphone",
                "3", "Tablet",
                "4", "Headphones",
                "5", "Smartwatch"
        );

        for (Map.Entry<String, String> entry : products.entrySet()) {
            String productId = entry.getKey();
            String productKey = "product:" + productId;
            // It will add in case they don't exist, but since I'm flushing the data they will always add
            if (!jedis.exists(productKey)) {
                jedis.hset(productKey, "name", entry.getValue());
                jedis.hset(productKey, "quantity", "30");
            }

            jedis.hset("products", productId, entry.getValue());
        }
    }

    public static void listRegisteredUsers(Jedis jedis) {
        // I created a list of the users, with input to follow it so it would be more interactive
        // And easier to follow
        // We can always change the time in order to test if you can make more orders after the time is up
        // Also everytime the order timer resets the orders placed that we can see when we type "list" as input
        // Resets, because I thought it would be better to follow and know how many orders you had left to place until time was up
        // than an overall order count, which wouldn't be hard to add anyways
        Set<String> registeredUsers = jedis.smembers("registered_users");

        if (registeredUsers.isEmpty()) {
            System.out.println("No users have registered yet.");
            return;
        }

        System.out.println("Registered users:");
        for (String username : registeredUsers) {
            String userKey = "user:" + username;
            long orderCount = jedis.llen(userKey);
            // ttl -> time to live -> time remaining until key is deleted from redis
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
    // I'll flush the data to reset each time I run mvn exec:java
    public static void resetData(Jedis jedis) {
        jedis.flushAll(); 
        System.out.println("Redis data has been reset.");
    }
}
