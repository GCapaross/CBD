package com.gabriel.cbd.lab1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import redis.clients.jedis.Jedis;
import java.util.List;

// This version is not being used because the filter should be done through redis aswell

public class Autocomplete3 {

    public static final String NAMES_KEY = "autocomplete:names:popularity";

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        
        jedis.del(NAMES_KEY);
      

        File file = new File("../nomes-pt-2021.csv");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");  
                String name = parts[0].trim();
                double popularity = Double.parseDouble(parts[1].trim());

                jedis.zadd(NAMES_KEY, popularity, name);
            }
            System.out.println("Loading successful");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Search for ('Enter' for quit): ");
            String incomplete = input.nextLine();
            
            if (incomplete.matches("Enter")) {
                System.out.println("Exiting...");
                break;
            }

            String start = incomplete.toLowerCase();
           // String end = start + Character.MAX_VALUE;

            List<String> results = jedis.zrangeByScore(NAMES_KEY, 0, Double.MAX_VALUE);

           
            results.stream()
                .filter(name -> name.toLowerCase().startsWith(start)) 
                .sorted((a, b) -> Double.compare(
                    jedis.zscore(NAMES_KEY, b),
                    jedis.zscore(NAMES_KEY, a)
                ))
                .forEach(System.out::println);
        }

        jedis.close();
        input.close();
    }
}