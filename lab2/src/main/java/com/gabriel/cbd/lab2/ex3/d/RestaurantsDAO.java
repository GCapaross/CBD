package com.gabriel.cbd.lab2.ex3.d;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.Arrays;

public class RestaurantsDAO {
    private final MongoCollection<Document> mongoCollection;

    public RestaurantsDAO(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    public int countLocalidades() {
        // Although I can use distinct, it requires that you call more than once the document
        // int numLocalidades = mongoCollection.distinct("localidade", Document.class).size();
        int count = mongoCollection.aggregate(Arrays.asList(
            new Document("$group", new Document("_id", "$localidade")),
            new Document("$count", "numLocalidades")
        )).first().getInteger("numLocalidades");
        return count;
    }

    public Map<String, Integer> countRestByLocalidade() {
        // <Localidade, NumRest>
        // Trying to do this, in only 1 query
        Map<String, Integer> count = mongoCollection.aggregate(Arrays.asList(
            new Document("$group", new Document("_id", "$localidade").append("count", new Document("$sum", 1)))
        )).;
    }

    public List<String> getRestWithNameCloserTo(String name) {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("cbd");
        MongoCollection<Document> collection = database.getCollection("restaurants");


        RestaurantsDAO restaurantsDAO = new RestaurantsDAO(collection);

        System.out.println("\n");
        System.out.println("Alinea d...");
        System.out.println("Number of different localidades: " + restaurantsDAO.countLocalidades());
        System.out.println("Number of restaurants by localidade: " + restaurantsDAO.countRestByLocalidade());
        System.out.println("Restaurants with name closer to 'name': " + restaurantsDAO.getRestWithNameCloserTo("name"));


    }
}
