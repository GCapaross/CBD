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

        Scanner scanner = new Scanner(System.in);
        System.out.println("To run the driver press 1: ");
        int option = scanner.nextInt();
        
        if (option == 1) {
            driver.run(scanner);
        } else {
            System.out.println("Exiting...");
        }

        scanner.close();  // Close the scanner after all operations are done
    }

    public void run(Scanner scanner) {
        System.out.println("Iniciando conexão com o banco de dados Cassandra...");
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1") // Instead of with keyspace 
                .withKeyspace("SistemaPartilhaVideos")
                .build()) {

            System.out.println("Conexão estabelecida com sucesso!");
            insertData(session, scanner); 
            updateData(session, scanner);  
            fetchData(session, scanner);  

            System.out.println("Type 1 to do Query 5: Fetch videos by user and date range");
            if (scanner.nextInt() == 1) {
                fetchVideosByUserAndDateRange(session, scanner); // Query 5
            }

            System.out.println("Type 1 to do Query 4.b: Fetch events for a user");
            if (scanner.nextInt() == 1) {
                fetchUserEvents(session, scanner); // Query 4.b
            }

            System.out.println("Type 1 to do Query 4.a: Fetch last 5 events of a video by user");
            if (scanner.nextInt() == 1) {
                fetchLastFiveUserEventsForVideo(session, scanner); // Query 4.a
            }

            System.out.println("Type 1 to do Query 3: Fetch videos by tag");
            if (scanner.nextInt() == 1) {
                fetchVideosByTag(session, scanner); // Query 3
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Query 5 - Fetch videos by user and date range
    private void fetchVideosByUserAndDateRange(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to fetch videos by user and date range: ");
        if (scanner.nextInt() != 1) {
            System.out.println("Exiting...");
            return;
        }

        String query = "SELECT video_id, nome_video, descricao, upload_date " +
                       "FROM SistemaPartilhaVideos.Videos " +
                       "WHERE author_id = 24dc7f54-ec4a-4d84-9521-f2a03ac435e8 " +
                       "AND upload_date >= '2016-08-01' AND upload_date <= '2024-08-31'";

        ResultSet resultSet = session.execute(query);
        System.out.println("################################################################");
        for (Row row : resultSet) {
            System.out.println("Video ID: " + row.getUuid("video_id"));
            System.out.println("Nome: " + row.getString("nome_video"));
            System.out.println("Descrição: " + row.getString("descricao"));
            System.out.println("Upload Date: " + row.getInstant("upload_date"));
            System.out.println("---------------");
        }
    }

    // Query 4.b - Fetch events for a user
    private void fetchUserEvents(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to fetch events for a user: ");
        if (scanner.nextInt() != 1) {
            System.out.println("Exiting...");
            return;
        }

        String query = "SELECT video_id, event_type, event_timestamp, video_time_seconds " +
                       "FROM SistemaPartilhaVideos.RegistEvents " +
                       "WHERE user_id = b3a2cb05-bddc-4c18-89db-f1f34b4dc03b";

        ResultSet resultSet = session.execute(query);
        for (Row row : resultSet) {
            System.out.println("Video ID: " + row.getUuid("video_id"));
            System.out.println("Event Type: " + row.getString("event_type"));
            System.out.println("Event Timestamp: " + row.getInstant("event_timestamp"));
            System.out.println("Video Time (seconds): " + row.getInt("video_time_seconds"));
            System.out.println("---------------");
        }
    }

    // Query 4.a - Fetch last 5 events of a video by user
    private void fetchLastFiveUserEventsForVideo(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to fetch the last 5 events of a video by user: ");
        if (scanner.nextInt() != 1) {
            System.out.println("Exiting...");
            return;
        }

        String query = "SELECT video_id, user_id, event_type, event_timestamp, video_time_seconds " +
                       "FROM SistemaPartilhaVideos.RegistEvents " +
                       "WHERE video_id = c8f85ebf-2ddc-4c30-947c-c714f08c0f16 " +
                       "AND user_id = b3a2cb05-bddc-4c18-89db-f1f34b4dc03b " +
                       "ORDER BY event_timestamp DESC " +
                       "LIMIT 5";

        ResultSet resultSet = session.execute(query);
        for (Row row : resultSet) {
            System.out.println("Video ID: " + row.getUuid("video_id"));
            System.out.println("User ID: " + row.getUuid("user_id"));
            System.out.println("Event Type: " + row.getString("event_type"));
            System.out.println("Event Timestamp: " + row.getInstant("event_timestamp"));
            System.out.println("Video Time (seconds): " + row.getInt("video_time_seconds"));
            System.out.println("---------------");
        }
    }

    // Query 3 - Fetch videos by tag
    private void fetchVideosByTag(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to fetch videos by tag: ");
        if (scanner.nextInt() != 1) {
            System.out.println("Exiting...");
            return;
        }

        String query = "SELECT video_id, nome_video, tags " +
                       "FROM SistemaPartilhaVideos.Videos " +
                       "WHERE tags CONTAINS 'tag8'";

        ResultSet resultSet = session.execute(query);
        for (Row row : resultSet) {
            System.out.println("Video ID: " + row.getUuid("video_id"));
            System.out.println("Nome: " + row.getString("nome_video"));
            System.out.println("Tags: " + row.getList("tags", String.class));
            System.out.println("---------------");
        }
    }

    private void insertData(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to insert data: ");
        if (scanner.nextInt() != 1) {
            System.out.println("Exiting...");
            return;
        }
        String query = "INSERT INTO Videos (author_id, upload_date, video_id, nome_video, descricao, tags) " +
                       "VALUES (uuid(), toTimestamp(now()), uuid(), 'Video Teste', 'Descrição simples', ['tag1', 'tag2'])";
        session.execute(query);
        System.out.println("Dados inseridos com sucesso!");
    }

    private void updateData(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to update data: ");
        if (scanner.nextInt() != 1) {
            System.out.println("Exiting...");
            return;
        }
        String query = "UPDATE Videos SET descricao = 'Descrição atualizada' " +
                       "WHERE author_id = 20f8114d-191f-40e5-9a80-c113e9638d3e " +
                       "AND upload_date = '2024-11-24 00:00:00' " +
                       "AND video_id = 3b34c07d-3dc8-48bc-9b32-3c98f431c5aa";
        session.execute(query);
        System.out.println("Dados atualizados com sucesso!");
    }

    private void fetchData(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to fetch data: ");
        if (scanner.nextInt() != 1) {
            System.out.println("Exiting...");
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
    }
}
