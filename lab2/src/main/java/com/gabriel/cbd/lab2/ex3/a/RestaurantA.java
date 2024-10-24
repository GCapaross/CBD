package com.gabriel.cbd.lab2.ex3.a;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import com.mongodb.client.result.*;
import java.util.Scanner;


import java.util.Arrays;

public class RestaurantA {
    // First we need to create this class to be able to interact with the database
    private final MongoCollection<Document> mongoCollection;

    public RestaurantA(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }


    // !! We could also create an object for the grades.
    
    public void insertRestaurant(String nome, String localidade, String gastronomia, Document address, int score1, int score2) {
        Document newRestaurant = new Document("nome", nome)
                .append("localidade", localidade)
                .append("gastronomia", gastronomia)
                .append("grades", Arrays.asList(
                        new Document("date", "2023-10-01").append("grade", "A").append("score", score1),
                        new Document("date", "2023-08-01").append("grade", "B").append("score", score2)
                ))
                .append("address", address);

        InsertOneResult insert = mongoCollection.insertOne(newRestaurant);
        if (insert.wasAcknowledged()) {
            System.out.println("Restaurante inserido com sucesso!");

        }
        else {
            System.out.println("Restaurante não inserido");
        }

    }

    public void updateRestaurantName(String oldName, String newName) {
        UpdateResult update_result = mongoCollection.updateOne(
                Filters.eq("nome", oldName),
                Updates.set("nome", newName)
        );
        if (update_result.getModifiedCount() > 0)  {
            System.out.println("Nome do restaurante atualizado com sucesso!");
        }
        else {
            System.out.println("Nada modificado");
        }
    }

    public void findRestaurantsByLocalidade(String localidade) {
        // We use the regular commands from mongo but apply .Filter as if it was {} with the condition inside
        for (Document doc : mongoCollection.find(Filters.eq("localidade", localidade))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }
    }

    // TODO: Autocomplete do search restaurant
    public void findRestaurantsByName(String name)  {
        for (Document doc : mongoCollection.find(Filters.eq("nome", name))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }
    }

        public static void main(String[] args) {

            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            
            MongoDatabase database = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");
            
            System.out.println("Database: " + database.getName());
            System.out.println("Collection: " + collection.getNamespace().getCollectionName());
            
            RestaurantA restaurantService = new RestaurantA(collection);
            
            System.out.println("\n");
            System.out.println("Alinea a...");
        Document address = new Document("building", "123")
                .append("coord", Arrays.asList(-73.732315, 40.720725))
                .append("rua", "Jamaica Avenue")
                .append("zipcode", "11428");

        // restaurantService.insertRestaurant("Novo Restaurante", "Brooklyn", "Portuguese", address, 12, 8);

        // restaurantService.updateRestaurantName("Novo Restaurante", "Restaurante Atualizado");

        // restaurantService.findRestaurantsByLocalidade("Brooklyn");
        Scanner sc = new Scanner(System.in);

        boolean run = true;
        while(run) {
        System.out.println("Choose an option:");
        System.out.println("1 - Insert Restaurant");
        System.out.println("2 - Update Restaurant Name");
        System.out.println("3 - Find Restaurants by Localidade");
        System.out.println("4 - Find Restaurants by name");
        System.out.println("5 - Exit");
        switch(sc.nextInt()) {
            case 1:
                System.out.println("Inserting Restaurant...");
                System.out.println("Name:");
                String name = sc.next();
                System.out.println("Localidade:");
                String localidade = sc.next();
                System.out.println("Gastronomia:");
                String gastronomia = sc.next();
                System.out.println("Address:");
                System.out.println("Building:");
                String building = sc.next();
                System.out.println("Coord:");
                double coord1 = sc.nextDouble();    
                double coord2 = sc.nextDouble();
                System.out.println("Rua:");
                String rua = sc.next();
                System.out.println("Zipcode:");
                String zipcode = sc.next();
                System.out.println("Score 1:");
                int score1 = sc.nextInt();
                System.out.println("Score 2:");
                int score2 = sc.nextInt();
                Document newAddress = new Document("building", building)
                        .append("coord", Arrays.asList(coord1, coord2))
                        .append("rua", rua)
                        .append("zipcode", zipcode);
                restaurantService.insertRestaurant(name, localidade, gastronomia, newAddress, score1, score2);
                break;
            case 2:
                System.out.println("Updating Restaurant Name...");
                System.out.println("Old Name:");
                String oldName = sc.next();
                System.out.println("New Name:");
                String newName = sc.next();
                restaurantService.updateRestaurantName(oldName, newName);
                break;
            case 3:
                System.out.println("Finding Restaurants by Localidade...");
                System.out.println("Localidade:");
                String localidadeFind = sc.next();
                restaurantService.findRestaurantsByLocalidade(localidadeFind);
                break;
            case 4:
                System.out.println("Finding Restaurants by Name...");
                System.out.println("Name");
                String nameFind = sc.next();
                restaurantService.findRestaurantsByName(nameFind);
                break;
            case 5:
                System.out.println("Exiting...");
                run = false;
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }
        sc.close();
        mongoClient.close();
        System.exit(0);
    }
}
