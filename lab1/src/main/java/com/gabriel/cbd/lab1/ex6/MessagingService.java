package com.gabriel.cbd.lab1;

import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.List;

public class MessagingService {

    private static final Jedis jedis = new Jedis("localhost", 6379);

    public static String addUser(String username) {
        if (jedis.sismember("users", username)) {
            return "User already exists.";
        }
        jedis.sadd("users", username);
        return "User added successfully.";
    }

    public static String followUser(String follower, String followed) {
        if (!jedis.sismember("users", follower) || !jedis.sismember("users", followed)) {
            return "One or both users do not exist.";
        }
        jedis.sadd("followers:" + followed, follower);
        return follower + " is now following " + followed;
    }

    public static String sendMessage(String sender, String message) {
        if (!jedis.sismember("users", sender)) {
            return "User does not exist.";
        }
        String messageKey = "messages:" + sender;
        jedis.rpush(messageKey, message);

        Set<String> followers = jedis.smembers("followers:" + sender);
        for (String follower : followers) {
            jedis.rpush("followers_messages:" + follower, message);
        }

        return "Message sent successfully!";
    }

    public static List<String> readMessages(String username) {
        if (!jedis.sismember("users", username)) {
            throw new IllegalArgumentException("User does not exist.");
        }
        return jedis.lrange("messages:" + username, 0, -1);
    }

    public static List<String> readMessagesForFollower(String follower) {
        if (!jedis.sismember("users", follower)) {
            throw new IllegalArgumentException("Follower does not exist.");
        }
        return jedis.lrange("followers_messages:" + follower, 0, -1);
    }

    public static void main(String[] args) {
        try {
            System.out.println(addUser("userA"));
            System.out.println(addUser("userB"));
            System.out.println(addUser("userC"));

            System.out.println(followUser("userA", "userB"));
            System.out.println(followUser("userA", "userC"));

            System.out.println(sendMessage("userB", "Hello, world!"));
            System.out.println(sendMessage("userC", "Hi everyone!"));

            System.out.println("Messages from userB: " + readMessages("userB"));
            System.out.println("Messages from userC: " + readMessages("userC"));

            System.out.println("Messages for userA: " + readMessagesForFollower("userA"));
        } finally {
            jedis.close();
        }
    }
}
