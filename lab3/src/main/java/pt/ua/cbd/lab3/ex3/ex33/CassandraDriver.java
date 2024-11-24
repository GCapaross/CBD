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
        
        scanner.close();  
    }

    public void run(Scanner scanner) {
        System.out.println("Iniciando conexão com o banco de dados Cassandra...");
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1") 
                .withKeyspace("SistemaPartilhaVideos")
                .build()) {

            System.out.println("Conexão estabelecida com sucesso!");
            insertData(session, scanner); 
            updateData(session, scanner);  
            fetchData(session, scanner);  

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(CqlSession session, Scanner scanner) {
        System.out.println("Type 1 to insert data: ");
        int option = scanner.nextInt();
        if (option != 1) {
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
        int option = scanner.nextInt();
        if (option != 1) {
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
        int option = scanner.nextInt();
        if (option != 1) {
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
