package com.gabriel.cbd.lab2.ex3.a;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.Arrays;

public class RestaurantA {
    // First we need to create this class to be able to interact with the database
    private final MongoCollection<Document> mongoCollection;

    public RestaurantA(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    public void insertRestaurant(String nome, String localidade, String gastronomia, Document address, int score1, int score2) {
        Document newRestaurant = new Document("nome", nome)
                .append("localidade", localidade)
                .append("gastronomia", gastronomia)
                .append("grades", Arrays.asList(
                        new Document("date", "2023-10-01").append("grade", "A").append("score", score1),
                        new Document("date", "2023-08-01").append("grade", "B").append("score", score2)
                ))
                .append("address", address);

        mongoCollection.insertOne(newRestaurant);
        System.out.println("Restaurante inserido com sucesso!");
    }

    public void updateRestaurantName(String oldName, String newName) {
        mongoCollection.updateOne(
                Filters.eq("nome", oldName),
                Updates.set("nome", newName)
        );
        System.out.println("Nome do restaurante atualizado com sucesso!");
    }

    public void findRestaurantsByLocalidade(String localidade) {
        // We use the regular commands from mongo but apply .Filter as if it was {} with the condition inside
        for (Document doc : mongoCollection.find(Filters.eq("localidade", localidade))) {
            System.out.println("Restaurante encontrado: " + doc.toJson());
        }
    }
}
