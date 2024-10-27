package com.gabriel.cbd.lab2.ex4;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class OrderServiceA {
    private static final int LIMIT = 3;           
    private static final int TIMESLOT = 60;        
    private final MongoCollection<Document> ordersCollection;
    private final Map<String, Product> productCatalog;

    public OrderServiceA(MongoCollection<Document> ordersCollection) {
        this.ordersCollection = ordersCollection;
        this.productCatalog = initializeProducts();
    }

    private Map<String, Product> initializeProducts() {
        Map<String, Product> catalog = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            catalog.put(String.valueOf(i), new Product("Product" + i, 50));
        }
        return catalog;
    }

    public void placeOrder(String username, String productId) {
        Product product = productCatalog.get(productId);
        if (product == null) {
            System.out.println("Product with ID " + productId + " doesnt exist.");
            return;
        }

        if (product.getQuantity() <= 0) {
            System.out.println(product.getName() + " is out of stock.");
            return;
        }

        Instant now = Instant.now();
        Instant windowStart = now.minus(TIMESLOT, ChronoUnit.MINUTES);

        int orderCount = countUserOrders(username, Date.from(windowStart), Date.from(now));

        if (orderCount >= LIMIT) {
            System.out.println("Youve hit the order limit, " + username + ".");
            long minutesLeft = Duration.between(now, windowStart.plus(TIMESLOT, ChronoUnit.MINUTES)).toMinutes();
            System.out.println("Wait " + minutesLeft + " more minutes until you can order again.");
        } else {
            insertOrder(username, product.getName(), Date.from(now));
            product.decreaseQuantity();
            System.out.println(product.getName() + " has been ordered successfully for " + username);
        }
    }

    public void listProducts() {
        System.out.println("Products Available:");
        for (Map.Entry<String, Product> entry : productCatalog.entrySet()) {
            Product product = entry.getValue();
            System.out.println("ID: " + entry.getKey() + ", Product: " + product.getName() + ", Stock: " + product.getQuantity());
        }
    }

    public void listUserOrders() {
        Map<String, List<Document>> userOrders = new HashMap<>();
        for (Document order : ordersCollection.find()) {
            String username = order.getString("username");
            userOrders.computeIfAbsent(username, k -> new ArrayList<>()).add(order);
        }

        System.out.println("\nOrders by User:");
        Instant now = Instant.now();
        for (Map.Entry<String, List<Document>> entry : userOrders.entrySet()) {
            String username = entry.getKey();
            List<Document> orders = entry.getValue();
            System.out.println("User: " + username);

            int recentOrders = (int) orders.stream()
                    .filter(order -> {
                        Instant orderTime = order.getDate("timestamp").toInstant();
                        return orderTime.isAfter(now.minus(TIMESLOT, ChronoUnit.MINUTES));
                    })
                    .count();

            if (recentOrders >= LIMIT) {
                Instant lastOrderTime = orders.get(orders.size() - 1).getDate("timestamp").toInstant();
                long minutesLeft = Duration.between(now, lastOrderTime.plus(TIMESLOT, ChronoUnit.MINUTES)).toMinutes();
                System.out.println("Chill! Wait " + minutesLeft + " more minutes to order again.");
            } else {
                System.out.println("All good! You can place more orders.");
            }
        }
    }

    private int countUserOrders(String username, Date windowStart, Date windowEnd) {
        Document filter = new Document("username", username)
                .append("timestamp", new Document("$gte", windowStart).append("$lt", windowEnd));
        return (int) ordersCollection.countDocuments(filter);
    }

    private void insertOrder(String username, String product, Date timestamp) {
        Document order = new Document("username", username)
                .append("product", product)
                .append("timestamp", timestamp);
        ordersCollection.insertOne(order);
    }

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("atendimentoDB");
        MongoCollection<Document> ordersCollection = database.getCollection("orders");

        OrderServiceA orderService = new OrderServiceA(ordersCollection);
        Scanner scanner = new Scanner(System.in);

        boolean run = true;
    
        while (run) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Products list");
            System.out.println("2. Place order");
            System.out.println("3. List user orders");
            System.out.println("0. Exit");
            System.out.print("Option: ");
    
            String option = scanner.next();
    
            switch (option) {
                case "1":
                    orderService.listProducts();
                    break;
                case "2":
                    System.out.print("Username: ");
                    String username = scanner.next();
    
                    System.out.print("Product ID: ");
                    String productId = scanner.next();
    
                    orderService.placeOrder(username, productId);
                    break;
                case "3":
                    orderService.listUserOrders();
                    break;
                case "0":
                    System.out.println("Exiting...");
                    run = false;
                    break;
                default:
                    System.out.println("Not a valid option.");
                    break;
            }
        }
    
        scanner.close();
        mongoClient.close();
    }
}   

class Product {
    private final String name;
    private int quantity;

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        } else {
            System.out.println(name + " is out of stock.");
        }
    }
}
