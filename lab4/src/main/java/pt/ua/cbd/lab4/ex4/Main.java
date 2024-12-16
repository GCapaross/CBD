package pt.ua.cbd.lab4.ex4;

import org.neo4j.driver.*;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final String URI = "bolt://localhost:7689"; 
    private static final String USER = "neo4j";
    private static final String PASSWORD = "exercicio44";  

    public static void main(String[] args) {
        try (Driver driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
             Session session = driver.session()) {

            loadCSVData(session);

            try (FileWriter writer = new FileWriter("CBD_L44c_output.txt")) {
                executeQueries(session, writer);
            }

            System.out.println("Program completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadCSVData(Session session) {
        session.run("LOAD CSV WITH HEADERS FROM 'file:///home/Gabriel/Desktop/3%20Ano/CBD/lab4/resources/USCandyDistributor/Candy_Sales.csv' AS row "
                    + "MERGE (c:Customer {id: row.`Customer ID`, "
                    + "                   name: row.`Country/Region`, "
                    + "                   city: row.City, "
                    + "                   state: row.`State/Province`, "
                    + "                   postalCode: row.`Postal Code`, "
                    + "                   division: row.Division, "
                    + "                   region: row.Region}) "
                    + "MERGE (o:Order {id: row.`Order ID`, "
                    + "               orderDate: row.`Order Date`, "
                    + "               shipDate: row.`Ship Date`, "
                    + "               shipMode: row.`Ship Mode`}) "
                    + "MERGE (p:Product {id: row.`Product ID`, "
                    + "                 name: row.`Product Name`, "
                    + "                 sales: row.Sales, "
                    + "                 units: row.Units, "
                    + "                 grossProfit: row.`Gross Profit`, "
                    + "                 cost: row.Cost}) "
                    + "MERGE (s:Sales {grossProfit: row.`Gross Profit`, "
                    + "               cost: row.Cost, "
                    + "               sales: row.Sales, "
                    + "               units: row.Units}) "
                    + "MERGE (c)-[:ORDERED]->(o) "
                    + "MERGE (o)-[:CONTAINS]->(p) "
                    + "MERGE (o)-[:SOLD_FOR]->(s)");
        
        System.out.println("CSV data loaded successfully!");
    }

    private static void executeQueries(Session session, FileWriter writer) throws IOException {
        // 1. Number of customers per region
        writer.write("Query 1: Number of customers per region:\n");
        Result result1 = session.run("MATCH (c:Customer) "
                                    + "RETURN c.region AS region, COUNT(c) AS customerCount "
                                    + "ORDER BY customerCount DESC;");
        while (result1.hasNext()) {
            var record = result1.next();
            writer.write(record.get("region").asString() + ": " + record.get("customerCount").asInt() + "\n");
        }

        // 2. Most bought product per city and its quantity
        writer.write("\nQuery 2: Most bought product per city and its quantity:\n");
        Result result2 = session.run("MATCH (c:Customer)-[:ORDERED]->(o:Order)-[:CONTAINS]->(p:Product) "
                                    + "WITH c.city AS city, p.id AS productId, COUNT(p) AS productCount "
                                    + "WITH city, productId, productCount "
                                    + "ORDER BY city, productCount DESC "
                                    + "WITH city, COLLECT({productId: productId, productCount: productCount}) AS products "
                                    + "RETURN city, products[0].productId AS mostBoughtProductId, products[0].productCount AS mostBoughtProductCount;");
        while (result2.hasNext()) {
            var record = result2.next();
            writer.write(record.get("city").asString() + ": " 
                         + record.get("mostBoughtProductId").asString() + " - " 
                         + record.get("mostBoughtProductCount").asInt() + " units\n");
        }

        // 3. Customer with the most amount of orders
        writer.write("\nQuery 3: Customer with the most amount of orders:\n");
        Result result3 = session.run("MATCH (c:Customer)-[:ORDERED]->(o:Order) "
                                    + "WITH c.id AS customerId, COUNT(o) AS orderCount "
                                    + "ORDER BY orderCount DESC "
                                    + "LIMIT 1 "
                                    + "RETURN customerId, orderCount;");
        while (result3.hasNext()) {
            var record = result3.next();
            writer.write("Customer ID: " + record.get("customerId").asString() + ", Orders: " + record.get("orderCount").asInt() + "\n");
        }

        // 4. Most units sold per product
        writer.write("\nQuery 4: Most units sold per product:\n");
        Result result4 = session.run("MATCH (o:Order)-[:CONTAINS]->(p:Product) "
                                    + "RETURN p.name AS product, SUM(toInteger(p.units)) AS totalUnitsSold "
                                    + "ORDER BY totalUnitsSold DESC;");
        while (result4.hasNext()) {
            var record = result4.next();
            writer.write(record.get("product").asString() + ": " 
                         + record.get("totalUnitsSold").asInt() + " units\n");
        }

        // 5. Total sales per year
        writer.write("\nQuery 5: Total sales per year:\n");
        Result result5 = session.run("MATCH (o:Order)-[:SOLD_FOR]->(s:Sales) "
                                    + "WITH SUBSTRING(o.orderDate, 0, 4) AS year, SUM(toFloat(s.sales)) AS totalSales "
                                    + "RETURN year, totalSales "
                                    + "ORDER BY year ASC;");
        while (result5.hasNext()) {
            var record = result5.next();
            writer.write("Year: " + record.get("year").asString() + ", Total Sales: " 
                         + record.get("totalSales").asDouble() + "\n");
        }

        // 6. Customer that bought the most amount of products
        writer.write("\nQuery 6: Customer that bought the most amount of products:\n");
        Result result6 = session.run("MATCH (c:Customer)-[:ORDERED]->(o:Order)-[:CONTAINS]->(p:Product) "
                                    + "WITH c.id AS customerId, SUM(toInteger(p.units)) AS totalProducts "
                                    + "ORDER BY totalProducts DESC "
                                    + "LIMIT 1 "
                                    + "RETURN customerId, totalProducts;");
        while (result6.hasNext()) {
            var record = result6.next();
            writer.write("Customer ID: " + record.get("customerId").asString() + ", Total Products: " 
                         + record.get("totalProducts").asInt() + "\n");
        }

        // 7. Revenue per customer
        writer.write("\nQuery 7: Revenue per customer:\n");
        Result result7 = session.run("MATCH (c:Customer)-[:ORDERED]->(o:Order)-[:SOLD_FOR]->(s:Sales) "
                                    + "RETURN c.id AS customerId, SUM(toFloat(s.sales)) AS totalRevenue "
                                    + "ORDER BY totalRevenue DESC;");
        while (result7.hasNext()) {
            var record = result7.next();
            writer.write("Customer ID: " + record.get("customerId").asString() + ", Total Revenue: " 
                         + record.get("totalRevenue").asDouble() + "\n");
        }

        // 8. Orders by ship mode
        writer.write("\nQuery 8: Orders by ship mode:\n");
        Result result8 = session.run("MATCH (o:Order) "
                                    + "RETURN o.shipMode AS shipMode, COUNT(o) AS orderCount "
                                    + "ORDER BY orderCount DESC;");
        while (result8.hasNext()) {
            var record = result8.next();
            writer.write("Ship Mode: " + record.get("shipMode").asString() + ", Orders: " 
                         + record.get("orderCount").asInt() + "\n");
        }

        // 9. Number of orders in between two dates
        writer.write("\nQuery 9: Number of orders between two dates (2023):\n");
        Result result9 = session.run("MATCH (o:Order) "
                                    + "WHERE date(o.orderDate) >= date('2023-01-01') AND date(o.orderDate) <= date('2023-12-31') "
                                    + "RETURN COUNT(o) AS orderCount;");
        while (result9.hasNext()) {
            var record = result9.next();
            writer.write("Order Count in 2023: " + record.get("orderCount").asInt() + "\n");
        }

        // 10. Month with the most amount of sales per year
        writer.write("\nQuery 10: Month with the most amount of sales per year:\n");
        Result result10 = session.run("MATCH (o:Order)-[:SOLD_FOR]->(s:Sales) "
                                     + "WITH SUBSTRING(o.orderDate, 0, 4) AS year, SUBSTRING(o.orderDate, 5, 2) AS month, SUM(toFloat(s.sales)) AS totalSales "
                                     + "WITH year, month, totalSales "
                                     + "ORDER BY year, totalSales DESC "
                                     + "WITH year, COLLECT({month: month, totalSales: totalSales}) AS monthlySales "
                                     + "RETURN year, monthlySales[0].month AS topMonth, monthlySales[0].totalSales AS highestSales;");
        while (result10.hasNext()) {
            var record = result10.next();
            writer.write("Year: " + record.get("year").asString() + ", Top Month: " 
                         + record.get("topMonth").asString() + ", Highest Sales: " 
                         + record.get("highestSales").asDouble() + "\n");
        }

        System.out.println("Queries executed and results written to file!");
    }
}
