package com.gabriel.cbd.lab2.ex3;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.gabriel.cbd.lab2.ex3.a.RestaurantA;
import java.util.Arrays;
// import java.util.List;
import java.util.Scanner;

// import java.util.List;
// Dont forget to start the docker and the mongodb server

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

        MongoDatabase database = mongoClient.getDatabase("cbd");
        MongoCollection<Document> collection = database.getCollection("restaurants");

        System.out.println("Database: " + database.getName());
        System.out.println("Collection: " + collection.getNamespace().getCollectionName());

        RestaurantA restaurantService = new RestaurantA(collection);

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
        System.out.println("4 - Exit");
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
