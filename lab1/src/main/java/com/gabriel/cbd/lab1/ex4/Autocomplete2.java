package com.gabriel.cbd.lab1;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ZParams;

import java.io.File;
import java.util.Scanner;

public class Autocomplete2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String prefix = ""; 
        
        Jedis jedis = new Jedis();
        jedis.del("namesByPopularity");
        jedis.del("namesByAlphabet");
        
        try {
            Scanner fileScanner = new Scanner(new File("../nomes-pt-2021.csv"));
            String personName;
            Integer popularityCount;
            String[] parts;

            while (fileScanner.hasNextLine()) {
                parts = fileScanner.nextLine().trim().split(";");
                personName = parts[0];
                popularityCount = Integer.parseInt(parts[1]);
                jedis.zadd("namesByPopularity", popularityCount, personName.toLowerCase()); 
                jedis.zadd("namesByAlphabet", 0, personName.toLowerCase()); 
            }
            fileScanner.close();
            System.out.println("Loading successful");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!prefix.equals("Enter")) {
            System.out.print("Search for ('Enter' for quit): ");
            prefix = scanner.nextLine();
            if (prefix.equals("Enter")) {
                break;
            }

            String min = "[" + prefix.toLowerCase(); 
            String max = min + '\uffff';

            jedis.zrangeByLex("namesByAlphabet", min, max)
                .forEach((match) -> {
                    jedis.zadd("matchingNames", 0, match);
                });

            jedis.zinter(new ZParams(), "namesByPopularity", "matchingNames")
                .reversed()
                .forEach(System.out::println);

            jedis.del("matchingNames");
        }

        jedis.close();
        scanner.close();
    }
}
