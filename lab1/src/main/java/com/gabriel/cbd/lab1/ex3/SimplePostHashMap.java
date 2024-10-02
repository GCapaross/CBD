package com.gabriel.cbd.lab1;

import redis.clients.jedis.Jedis;
import java.util.Map;

public class SimplePostHashMap {
    public static String USERS_HASH_KEY = "users_hash"; 
    
    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        Map<String, String> users = Map.of(
            "1", "Ana",
            "2", "Pedro",
            "3", "Maria",
            "4", "Luis"
        );

        jedis.del(USERS_HASH_KEY);

        users.forEach((id, name) -> jedis.hset(USERS_HASH_KEY, id, name));

        Map<String, String> usersMap = jedis.hgetAll(USERS_HASH_KEY);
        usersMap.forEach((id, name) -> System.out.println("ID: " + id + ", Name: " + name));

        jedis.close();
    }
}
