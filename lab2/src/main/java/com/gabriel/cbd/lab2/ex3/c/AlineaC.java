package com.gabriel.cbd.lab2.ex3.c;


// 10. Liste o restaurant_id, o nome, a localidade e gastronomia dos restaurantes cujo nome começam por "Wil".
// db.restaurants.find({ nome: { $regex: /^Wil/ } }, { restaurant_id: 1, nome: 1, localidade: 1, gastronomia: 1 })
// -------------------------------------------------------------------------------------------------------
// 15. Liste o restaurant_id, o nome e os score dos restaurantes nos quais a segunda avaliação foi grade "A" e ocorreu em ISODATE "2014-08-11T00: 00: 00Z".
// db.restaurants.find({ "grades.1.grade": "A", "grades.1.date": ISODate("2014-08-11T00:00:00.000Z") }, { restaurant_id: 1, nome: 1, "grades.1.score": 1 })
// -------------------------------------------------------------------------------------------------------
// 18. Liste nome, localidade, grade e gastronomia de todos os restaurantes localizados em Brooklyn que não incluem gastronomia "American" e obtiveram uma classificação (grade) "A". Deve apresentá-los por ordem decrescente de gastronomia.
// db.restaurants.find({ localidade: "Brooklyn", gastronomia: { $ne: "American" }, "grades.grade": "A" }, { nome: 1, localidade: 1, "grades.grade": 1, gastronomia: 1 }).sort({ gastronomia: -1 })
// -------------------------------------------------------------------------------------------------------
// 20. Apresente o nome e número de avaliações (numGrades) dos 3 restaurante com mais avaliações.
// db.restaurants.aggregate([{ $project: { nome: 1, numGrades: { $size: "$grades" } } }, { $sort: { numGrades: -1 } }, { $limit: 3 }])
// -------------------------------------------------------------------------------------------------------
// 24. Apresente o número de gastronomias diferentes na rua "Fifth Avenue"
// db.restaurants.aggregate([{ $match: { "address.rua": "Fifth Avenue" } }, { $group: { _id: "$gastronomia" } }, { $count: "numGastronomias" }])

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.*;

import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.text.ParseException;
import java.util.Arrays;

public class AlineaC {

    private final MongoCollection<Document> collection;

    public AlineaC(MongoCollection<Document> collection) {
        this.collection = collection;
    }


    // Could've been done with "while" or an iterator, because you need that to use .find
    // With a foreach it's simpler

    public void listRestaurantsStartingWithWil() {
        System.out.println("\n");
        System.out.println("1 - ");
        System.out.println("Restaurants whose name starts with 'Wil':");
        for (Document doc : collection.find(Filters.regex("nome", "^Wil"))) {
            System.out.println(doc.toJson());
        }
    }


    // !!! NOT WORKING 
    public void listRestaurantsWithGradeAOnDate() {
        System.out.println("\n");
        System.out.println("2 - ");
        System.out.println("Restaurants with the second evaluation grade 'A' on 2014-08-11:");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date targetDate = null;
        try {
            targetDate = dateFormat.parse("2014-08-11T00:00:00.000Z");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Bson filter = Filters.and(
                Filters.eq("grades.1.grade", "A"),
                Filters.eq("grades.1.date", targetDate)
        );
        // Probably could've done it like exercise 1, but did this to separate the steps better
        for (Document doc : collection.find(filter)) {
            System.out.println(doc.toJson());
        }
    }
       
    
    // !!! NOT WORKING
    public void listBrooklynRestaurants() {
        System.out.println("\n");
        System.out.println("3 - ");
        System.out.println("Brooklyn restaurants with grade 'A' not American cuisine:");
        Bson filter = Filters.and(
        Filters.eq("localidade", "Brooklyn"),
        Filters.ne("gastronomia", "American"),
        Filters.eq("grades.grade", "A"),
        //! Added name starting with C, because query was too long
        Filters.eq("nome", "^C")
    );
    
    for (Document doc : collection.find(filter).sort(Sorts.descending("gastronomia"))) {
        System.out.println(doc.toJson());
    }
    }

    public void topThreeRestaurantsByReviews() {
        System.out.println("\n");
        System.out.println("4 - ");
        System.out.println("Top 3 restaurants by number of reviews:");
        Bson projection = Projections.fields(
        Projections.include("nome"),
        Projections.computed("numGrades", new Document("$size", "$grades"))
    );

    for (Document doc : collection.aggregate(Arrays.asList(
        Aggregates.project(projection),
        Aggregates.sort(Sorts.descending("numGrades")),
        Aggregates.limit(3)
    ))) {
        System.out.println(doc.toJson());
    }
    }

    public void countDifferentCuisinesOnFifthAvenue() {
        System.out.println("\n");
        System.out.println("5 - ");
        System.out.println("Number of different cuisines on 'Fifth Avenue':");
        long count = collection.aggregate(Arrays.asList(
        Aggregates.match(Filters.eq("address.rua", "Fifth Avenue")),
        Aggregates.group("$gastronomia"),
        Aggregates.count("numGastronomias")
    )).first().getLong("numGastronomias");

    System.out.println("Number of different cuisines: " + count);
    }

    public static void main(String[] args) {

        
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("cbd");
        MongoCollection<Document> collection = database.getCollection("restaurants");

        
        AlineaC alineaC = new AlineaC(collection);
        
        System.out.println("\n");
        System.out.println("Alinea c...");
        alineaC.listRestaurantsStartingWithWil();
        alineaC.listRestaurantsWithGradeAOnDate();
        alineaC.listBrooklynRestaurants();
        alineaC.topThreeRestaurantsByReviews();
        alineaC.countDifferentCuisinesOnFifthAvenue();

        mongoClient.close();
    }
}
