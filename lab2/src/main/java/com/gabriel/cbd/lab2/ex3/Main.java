package com.gabriel.cbd.lab2.ex3;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.gabriel.cbd.lab2.ex3.a.RestaurantA;
import java.util.Arrays;
import java.util.List;

// import java.util.List;
// Dont forget to start the docker and the mongodb server

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

        MongoDatabase database = mongoClient.getDatabase("cbd");
        MongoCollection<Document> collection = database.getCollection("restaurantes");

        System.out.println("Database: " + database.getName());
        System.out.println("Collection: " + collection.getNamespace().getCollectionName());

        RestaurantA restaurantService = new RestaurantA(collection);

        Document address = new Document("building", "123")
                .append("coord", Arrays.asList(-73.732315, 40.720725))
                .append("rua", "Jamaica Avenue")
                .append("zipcode", "11428");

        restaurantService.insertRestaurant("Novo Restaurante", "Brooklyn", "Portuguese", address, 12, 8);

        restaurantService.updateRestaurantName("Novo Restaurante", "Restaurante Atualizado");

        restaurantService.findRestaurantsByLocalidade("Brooklyn");

        mongoClient.close();
    }
}
