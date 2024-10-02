package com.gabriel.cbd.lab1;

import redis.clients.jedis.Jedis;
import java.util.List;

public class SimplePostList {
    public static String USERS_LIST_KEY = "users_list"; 
    
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };

        jedis.del(USERS_LIST_KEY);

        for (String user : users)
            jedis.lpush(USERS_LIST_KEY, user);

        List<String> userList = jedis.lrange(USERS_LIST_KEY, 0, -1);
        userList.forEach(System.out::println);

        jedis.close();
    }
}
