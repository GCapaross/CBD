package pt.ua.cbd.lab3.ex3.ex33;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class CassandraDriver {

    public static void main(String[] args) {

        System.out.println("Iniciando conexão com o banco de dados Cassandra...");
        CassandraDriver driver = new CassandraDriver();

        System.out.println("To run the driver press 1: ");
        
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if (option == 1) {
            driver.run();
        }
        else {
            System.out.println("Exiting...");
        }
        scanner.close();
    }

    public void run() {
        System.out.println("Iniciando conexão com o banco de dados Cassandra...");
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1") // Instead of with keyspace 
                .withKeyspace("SistemaPartilhaVideos")
                .build()) {

            System.out.println("Conexão estabelecida com sucesso!");
            // insertTables(session);  // Tables should be created before hand
            insertData(session);
            updateData(session);
            fetchData(session);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // private void insertTables(CqlSession session) {
    //     Scanner scanner = new Scanner(System.in);
    //     System.out.println("Type 1 to create tables: ");
    //     int option = scanner.nextInt();
    //     if (option != 1) {
    //         System.out.println("Exiting...");
    //         scanner.close();
    //         return;
    //     }
    //     String query = "DROP KEYSPACE IF EXISTS SistemaPartilhaVideos; " +
    //             "CREATE KEYSPACE SistemaPartilhaVideos WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3}; " +
    //             "USE SistemaPartilhaVideos; " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.User( " +
    //             "id UUID PRIMARY KEY, " +
    //             "username TEXT, " +
    //             "nome TEXT, " +
    //             "email TEXT, " +
    //             "resgist_date TIMESTAMP); " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.Videos ( " +
    //             "video_id UUID, " +
    //             "author_id UUID, " +
    //             "nome_video TEXT, " +
    //             "descricao TEXT, " +
    //             "tags LIST<TEXT>, " +
    //             "upload_date TIMESTAMP, " +
    //             "PRIMARY KEY (author_id, upload_date, video_id) " +
    //             ") WITH CLUSTERING ORDER BY (upload_date DESC); " +
    //             "CREATE INDEX IF NOT EXISTS ON SistemaPartilhaVideos.Videos (tags); " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.Comments( " +
    //             "video_id UUID, " +
    //             "comment_id UUID, " +
    //             "user_id UUID, " +
    //             "comment_date TIMESTAMP, " +
    //             "comment_content TEXT, " +
    //             "PRIMARY KEY (video_id, comment_date, user_id) " +
    //             ") WITH CLUSTERING ORDER BY (comment_date DESC, user_id DESC); " +
    //             "CREATE INDEX IF NOT EXISTS ON SistemaPartilhaVideos.Comments (user_id); " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.VideoFollowers( " +
    //             "video_id UUID, " +
    //             "follower_id UUID, " +
    //             "follow_date LIST<TIMESTAMP>, " +
    //             "PRIMARY KEY (video_id, follower_id)); " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.RegistEvents( " +
    //             "video_id UUID, " +
    //             "user_id UUID, " +
    //             "event_type TEXT, " +
    //             "event_timestamp TIMESTAMP, " +
    //             "video_time_seconds INT, " +
    //             "PRIMARY KEY (video_id, user_id, event_timestamp)); " +
    //             "CREATE INDEX IF NOT EXISTS ON SistemaPartilhaVideos.RegistEvents (user_id); " +
    //             "CREATE INDEX IF NOT EXISTS ON SistemaPartilhaVideos.RegistEvents (event_type); " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.VideoRatings ( " +
    //             "video_id UUID, " +
    //             "rating_value INT, " +
    //             "user_id UUID, " +
    //             "PRIMARY KEY (video_id, rating_value)); " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.VideoNotifications ( " +
    //             "notification_id TIMEUUID, " +
    //             "video_id UUID, " +
    //             "follower_id UUID, " +
    //             "comment_id TIMEUUID, " +
    //             "notification_date TIMESTAMP, " +
    //             "PRIMARY KEY (follower_id, notification_id)) " +
    //             "WITH CLUSTERING ORDER BY (notification_id DESC); " +
    //             "CREATE TABLE IF NOT EXISTS SistemaPartilhaVideos.VideoRatingsByVideo ( " +
    //             "video_id UUID, " +
    //             "rating_value INT, " +
    //             "user_id UUID, " +
    //             "PRIMARY KEY (video_id, user_id));";

    //     try {
    //         session.execute(query);
    //         System.out.println("Tables created or already exist in the SistemaPartilhaVideos keyspace.");
    //     } catch (Exception e) {
    //         System.err.println("Error executing table creation query: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    //     scanner.close();
    // }

    private void insertData(CqlSession session) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 1 to insert data: ");
        int option = scanner.nextInt();
        if (option != 1) {
            System.out.println("Exiting...");
            scanner.close();
            return;
        }
        String query = "INSERT INTO Videos (author_id, upload_date, video_id, nome_video, descricao, tags) " +
                       "VALUES (uuid(), toTimestamp(now()), uuid(), 'Video Teste', 'Descrição simples', ['tag1', 'tag2'])";
        session.execute(query);
        System.out.println("Dados inseridos com sucesso!");
        scanner.close();
    }

    private void updateData(CqlSession session) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 1 to update data: ");
        int option = scanner.nextInt();
        if (option != 1) {
            System.out.println("Exiting...");
            scanner.close();
            return;
        }
        String query = "UPDATE Videos SET descricao = 'Descrição atualizada' " +
                       "WHERE author_id = 20f8114d-191f-40e5-9a80-c113e9638d3e " +
                       "AND upload_date = '2024-11-24 00:00:00' " +
                       "AND video_id = 3b34c07d-3dc8-48bc-9b32-3c98f431c5aa";
        session.execute(query);
        System.out.println("Dados atualizados com sucesso!");
        scanner.close();
    }

    private void fetchData(CqlSession session) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 1 to fetch data: ");
        int option = scanner.nextInt();
        if (option != 1) {
            System.out.println("Exiting...");
            scanner.close();
            return;
        }
        String query = "SELECT video_id, nome_video, descricao FROM Videos";
        ResultSet resultSet = session.execute(query);

        for (Row row : resultSet) {
            System.out.println("Video ID: " + row.getUuid("video_id"));
            System.out.println("Nome: " + row.getString("nome_video"));
            System.out.println("Descrição: " + row.getString("descricao"));
            System.out.println("---------------");
        }
        scanner.close();
    }
}
