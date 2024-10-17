package com.gabriel.cbd.lab2.ex3.b;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class MainB {
    
    static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    static MongoDatabase database = mongoClient.getDatabase("cbd");
    static MongoCollection<Document> collection = database.getCollection("restaurants");

    //     Crie índices: um para localidade; outro para gastronomia; e um de texto para o
    // nome. Use pesquisas para testar o funcionamento e o verifique o desempenho
    // (como são poucos documentos, os resultados poderão não melhorar).

    public static void main(String[] args) {

        collection.dropIndexes();

        searchWithoutIndex();

        // Create indexes

        collection.createIndex(new Document().append("localidade", 1 ));
        collection.createIndex(new Document().append("gastronomia", 1 ));
        collection.createIndex(new Document().append("nome", "text" ));

        searchWithIndex();

    }

    private static void searchWithIndex() {
        System.out.println("Searching with index");
        long start = System.currentTimeMillis();

    }

    private static void searchWithoutIndex() {
        System.out.println("Searching without index");
        long start = System.currentTimeMillis();
        // Localidade
        for (Document doc : collection.find(new Document("localidade", "Brooklyn"))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }
        // Gastronomia
        for (Document doc : collection.find(new Document("gastronomia", "Americana"))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }
        // Nome
        for (Document doc : collection.find(new Document("nome", ""))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + "ms");
    }
    


}
