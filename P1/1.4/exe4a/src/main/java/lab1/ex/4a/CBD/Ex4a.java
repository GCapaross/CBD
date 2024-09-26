import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Ex4a {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final String ZSET_KEY = "autocomplete:names"; 

    private static void LoadNames(Jedis jedis, String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                jedis.zadd(ZSET_KEY, 0, word);
            }

            scanner.close();
            System.out.println("Names loaded into Redis successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }

    private static List<String> GetAutocomplete(Jedis jedis, String prefix) {
        String min = prefix;           
        String max = prefix + "\uFFFF"; 
        
        List<String> suggestions = jedis.zrangeByLex(ZSET_KEY, "[" + min, "[" + max);

        return suggestions;
    }

    public static void main(String[] args) {

        try (JedisPool pool = new JedisPool(REDIS_HOST, REDIS_PORT);
             Jedis jedis = pool.getResource()) {

            LoadNames(jedis, "names.txt");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Search for ('Enter' for quit): ");
            String prefix = scanner.nextLine();
            scanner.close();        

            List<String> suggestions = GetAutocomplete(jedis, prefix);

            if (!suggestions.isEmpty()) {
                System.out.println("Suggestions for '" + prefix + "':");
                for (String suggestion : suggestions) {
                    System.out.println(suggestion);
                }
            } else {
                System.out.println("No suggestions found for '" + prefix + "'.");
            }

        } catch (Exception e) {
            System.err.println("Error while interacting with Redis: " + e.getMessage());
        }
    }
}
