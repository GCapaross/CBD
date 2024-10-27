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


public class OrderServiceB {
    private static final int ORDER_LIMIT = 20;           
    private static final int ORDER_TIMESLOT = 30;        
    private final MongoCollection<Document> ordersCollection;
    private final Map<String, Product2> productCatalog;

    public OrderServiceB(MongoCollection<Document> ordersCollection) {
        this.ordersCollection = ordersCollection;
        this.productCatalog = initializeProducts();
    }

    private Map<String, Product2> initializeProducts() {
        Map<String, Product2> catalog = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            catalog.put(String.valueOf(i), new Product2("ProductB" + i, 50)); 
        }
        return catalog;
    }

    public void placeOrder(String username, String productId, int quantity) {

        // ! Comment after testing
        Instant startTime = Instant.now();
        Product2 product = productCatalog.get(productId);
        if (product == null) {
            System.out.println("Product with ID " + productId + " doesn’t exist.");
            return;
        }

        if (product.getQuantity() < quantity) {
            System.out.println("Not enough stock for " + product.getName() + ". Available stock: " + product.getQuantity());
            return;
        }

        Instant now = Instant.now();
        Instant windowStart = now.minus(ORDER_TIMESLOT, ChronoUnit.MINUTES);

        int totalUnitsOrdered = countUserOrderedUnits(username, Date.from(windowStart), Date.from(now));

        if (totalUnitsOrdered + quantity > ORDER_LIMIT) {
            System.out.println("Order limit reached for " + username + ". You can only order up to " + (ORDER_LIMIT - totalUnitsOrdered) + " more units in this time window.");
            long minutesLeft = Duration.between(now, windowStart.plus(ORDER_TIMESLOT, ChronoUnit.MINUTES)).toMinutes();
            System.out.println("Please wait " + minutesLeft + " more minutes before ordering more units.");
        } else {
            insertOrder(username, product.getName(), quantity, Date.from(now));
            product.decreaseQuantity(quantity);
            System.out.println("Order for " + quantity + " units of " + product.getName() + " placed successfully for " + username);
        }

        // ! Comment after testing
        long duration = Duration.between(startTime, Instant.now()).toMillis(); // Duração da operação
    System.out.println("Time taken to place order in MongoDB: " + duration + " ms");
    }

    public void listProducts() {
        System.out.println("Products Available:");
        for (Map.Entry<String, Product2> entry : productCatalog.entrySet()) {
            Product2 product = entry.getValue();
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

            int recentUnitsOrdered = orders.stream()
                    .filter(order -> order.getDate("timestamp").toInstant().isAfter(now.minus(ORDER_TIMESLOT, ChronoUnit.MINUTES)))
                    .mapToInt(order -> order.getInteger("quantity"))
                    .sum();

            if (recentUnitsOrdered >= ORDER_LIMIT) {
                Instant lastOrderTime = orders.get(orders.size() - 1).getDate("timestamp").toInstant();
                long minutesLeft = Duration.between(now, lastOrderTime.plus(ORDER_TIMESLOT, ChronoUnit.MINUTES)).toMinutes();
                System.out.println("Hold up! You’ve ordered the max units. Wait " + minutesLeft + " more minutes to order again.");
            } else {
                System.out.println("All clear! You can still order " + (ORDER_LIMIT - recentUnitsOrdered) + " units in this time window.");
            }
        }
    }

    private int countUserOrderedUnits(String username, Date windowStart, Date windowEnd) {
        Document filter = new Document("username", username)
                .append("timestamp", new Document("$gte", windowStart).append("$lt", windowEnd));
        return ordersCollection.find(filter).into(new ArrayList<>()).stream()
                .mapToInt(order -> order.getInteger("quantity", 0))
                .sum();
    }

    private void insertOrder(String username, String product, int quantity, Date timestamp) {
        Document order = new Document("username", username)
                .append("product", product)
                .append("quantity", quantity)
                .append("timestamp", timestamp);
        ordersCollection.insertOne(order);
    }
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("atendimentoDB_B"); 
        MongoCollection<Document> ordersCollection = database.getCollection("ordersB"); 
    
        OrderServiceB orderService = new OrderServiceB(ordersCollection);
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
    
                    int quantity = -1;
                    while (true) {
                        System.out.print("Quantity: ");
                        try {
                            quantity = scanner.nextInt();
                            if (quantity < 1) {
                                System.out.println("Quantity must be a positive integer.");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid quantity. Please enter a positive integer.");
                            scanner.next(); 
                        }
                    }
    
                    orderService.placeOrder(username, productId, quantity);
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

class Product2 {
    private final String name;
    private int quantity;

    public Product2(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decreaseQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        } else {
            System.out.println("Error: Not enough stock for " + name);
        }
    }
}
