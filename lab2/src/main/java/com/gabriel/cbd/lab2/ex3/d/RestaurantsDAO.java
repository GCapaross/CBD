package com.gabriel.cbd.lab2.ex3.d;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;

import org.bson.Document;

import java.util.*;
import java.util.regex.Pattern;

public class RestaurantsDAO {
    private final MongoCollection<Document> mongoCollection;

    public RestaurantsDAO(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    public int countLocalidades() {
        // we could be using distinct but its not efficient
        int count = mongoCollection.aggregate(Arrays.asList(
            new Document("$group", new Document("_id", "$localidade")),
            new Document("$count", "numLocalidades")
        )).first().getInteger("numLocalidades");
        return count;
    }

    public Map<String, Integer> countRestByLocalidade() {
        Map<String, Integer> count = new HashMap<>();
        AggregateIterable<Document> results = mongoCollection.aggregate(Arrays.asList(
            new Document("$group", new Document("_id", "$localidade").append("count", new Document("$sum", 1)))
        ));
        for (Document doc : results) {
            count.put(doc.getString("_id"), doc.getInteger("count"));
        }
        return count;
    }
    public List<String> getRestWithNameCloserTo(String name) {
    List<String> restaurants = new ArrayList<>();
    Pattern regex = Pattern.compile(".*" + Pattern.quote(name) + ".*", Pattern.CASE_INSENSITIVE);
    mongoCollection.find(new Document("name", regex)).forEach(doc -> restaurants.add(doc.getString("name")));
    return restaurants;
}


    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("cbd");
        MongoCollection<Document> collection = database.getCollection("restaurants");

        RestaurantsDAO restaurantsDAO = new RestaurantsDAO(collection);

        System.out.println("\nAlinea d...");
        System.out.println("#######################################################################################");

        System.out.println("Number of different localidades: \n" + restaurantsDAO.countLocalidades());
        System.out.println("#######################################################################################\n");
        System.out.println("Number of restaurants by localidade: \n" + restaurantsDAO.countRestByLocalidade());
        System.out.println("#######################################################################################\n");


        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name to search for restaurants containing it: ");
        String inputName = scanner.nextLine();
        List<String> matchingRestaurants = restaurantsDAO.getRestWithNameCloserTo(inputName);

        System.out.println("Nome de restaurantes contendo '" + inputName + "' no nome:");
        for (String restaurant : matchingRestaurants) {
            System.out.println("-> " + restaurant);
        }

        scanner.close();
    }
}