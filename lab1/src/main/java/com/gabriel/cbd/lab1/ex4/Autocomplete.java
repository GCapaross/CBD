package com.gabriel.cbd.lab1;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import redis.clients.jedis.Jedis;
import java.util.List;

public class Autocomplete {

    public static final String NAMES_KEY = "autocomplete:names";

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        
        // Limpar as chaves do redis para caso já existam
        jedis.del(NAMES_KEY);

        File file = new File("../names.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();
                jedis.zadd(NAMES_KEY, 0, name);
            }
            System.out.println("Names loaded into redis with success!");
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

            String start = incomplete;
            String end = incomplete + Character.MAX_VALUE;

            List<String> results = jedis.zrangeByLex(NAMES_KEY, "[" + start, "[" + end);
            // It was weird to write for example ah2332 and not getting suggestions without any message
            if (results.isEmpty()) {
                System.out.println("No suggestions found.\n");
            } else {    
                System.out.println("Suggestions: \n");
                results.forEach(System.out::println);
            }
        }

        jedis.close();
        input.close();

        jedis.close();
        input.close();
    }
}
