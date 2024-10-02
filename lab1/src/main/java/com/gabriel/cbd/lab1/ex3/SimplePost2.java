package com.gabriel.cbd.lab1;

import redis.clients.jedis.Jedis;
public class SimplePost2 {
    public static String USERS_KEY = "users"; 
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
// some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };
// jedis.del(USERS_KEY); // remove if exists to avoid wrong type
        for (String user : users)
        jedis.sadd(USERS_KEY, user);
        jedis.smembers(USERS_KEY).forEach(System.out::println);
        jedis.close();
}
}