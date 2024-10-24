package com.gabriel.cbd.lab2.ex3.b;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class AlineaB {

    static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    static MongoDatabase database = mongoClient.getDatabase("cbd");
    static MongoCollection<Document> collection = database.getCollection("restaurants");

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n");
        System.out.println("Alinea b...");

        collection.dropIndexes();

        searchWithoutIndex(); 

        // Criação dos índices
        collection.createIndex(new Document().append("localidade", 1)); // Índice para localidade
        collection.createIndex(new Document().append("gastronomia", 1)); // Índice para gastronomia
        collection.createIndex(new Document().append("nome", "text")); // Índice de texto para o nome
        Thread.sleep(1000);

        searchWithIndex(); 

        mongoClient.close();
    }

    private static void searchWithIndex() {
        System.out.println("Searching with index");
        long start = System.currentTimeMillis();

        for (Document doc : collection.find(new Document("localidade", "Brooklyn"))) {
            System.out.println("Restaurante encontrado (com índice - localidade): " + doc.toJson());
        }

        for (Document doc : collection.find(new Document("gastronomia", "Americana"))) {
            System.out.println("Restaurante encontrado (com índice - gastronomia): " + doc.toJson());
        }

        for (Document doc : collection.find(new Document("$text", new Document("$search", "McDonald's")))) {
            System.out.println("Restaurante encontrado (com índice - nome): " + doc.toJson());
        }

        long end = System.currentTimeMillis();
        System.out.println("Tempo com índice: " + (end - start) + "ms");
    }

    private static void searchWithoutIndex() {
        System.out.println("Searching without index");
        long start = System.currentTimeMillis();

        for (Document doc : collection.find(new Document("localidade", "Brooklyn"))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }

        for (Document doc : collection.find(new Document("gastronomia", "Americana"))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }

        for (Document doc : collection.find(new Document("nome", ""))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }

        long end = System.currentTimeMillis();
        System.out.println("Tempo sem índice: " + (end - start) + "ms");
    }

}
