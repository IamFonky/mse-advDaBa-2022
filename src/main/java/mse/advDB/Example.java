package mse.advDB;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import java.io.File;
import java.io.IOException;

public class Example {
    // private static final long MAX_FILE_SIZE = (long)2 * 1024 * 1024 * 1024;
    private static final long MAX_FILE_SIZE = 1024;

    public static void main(String[] args) throws IOException, InterruptedException {

        String jsonPath;
        int nbArticles;
        String neo4jIP;

        if (args.length > 0 && args[0].equals("local")) {
            jsonPath = "./dblpExample.json";
            System.out.println("Path to JSON file is " + jsonPath);
            nbArticles = 10000;
            System.out.println("Number of articles to consider is " + nbArticles);
            neo4jIP = "172.24.0.10";
            System.out.println("IP addresss of neo4j server is " + neo4jIP);
        } else {
            jsonPath = System.getenv("JSON_FILE");
            System.out.println("Path to JSON file is " + jsonPath);
            nbArticles = Integer.max(1000, Integer.parseInt(System.getenv("MAX_NODES")));
            System.out.println("Number of articles to consider is " + nbArticles);
            neo4jIP = System.getenv("NEO4J_IP");
            System.out.println("IP addresss of neo4j server is " + neo4jIP);
        }

        JSONParser.parse(jsonPath, "files/db", MAX_FILE_SIZE);

        Driver driver = GraphDatabase.driver("bolt://" + neo4jIP + ":7687", AuthTokens.basic("neo4j", "test"));
        boolean connected = false;
        do {
            try {
                System.out.println("Sleeping a bit waiting for the db");
                Thread.yield();
                // Thread.sleep(5000); // let some time for the neo4j container to be up and
                // running

                driver.verifyConnectivity();
                connected = true;
            } catch (Exception e) {
            }
        } while (!connected);

        driver.session().run("MATCH (n) DETACH DELETE n");// delete all nodes

        for (File file : new File("files").listFiles()) {

            driver.session().run("CALL apoc.load.json('" + file.getName() + "')\n" +
                    " YIELD value\n" +
                    " UNWIND value AS book\n" +
                    " MERGE (b:Book {\n" +
                    " id: book._id, \n" +
                    " title: book.title, \n" +
                    " year: book.year, \n" +
                    " n_citation: book.n_citation,\n" +
                    // " page_start: book.page_start,\n" +
                    // " page_end: book.page_end,\n" +
                    " lang: book.lang\n" +
                    // " volume: book.volume,\n" +
                    // " issue: book.issue,\n" +
                    // " issn: book.issn,\n" +
                    // " isbn: book.isbn,\n" +
                    // " doi: book.doi,\n" +
                    // " pdf: book.pdf,\n" +
                    // " abstract: book.abstract\n" +
                    "})" +

                    " WITH book, b\n" +
                    " UNWIND book.authors AS author\n" +
                    " MERGE (a:Author {" +
                    " id:author._id," +
                    " name:author.name" +
                    // " org:author.org,"+
                    // " orgid:author.orgid"+
                    "})" +
                    " MERGE (a)-[:WRITED]->(b)" +

                    " MERGE (v:Venue {" +
                    " id:book.venue._id," +
                    " type:book.venue.type," +
                    " raw:book.venue.raw" +
                    // " raw_zh:book.venue.raw_zh"+
                    "})" +
                    " MERGE (v)-[:PUBLISHED]->(b)" +

                    " WITH book, b\n" +
                    " UNWIND book.keywords AS keyword\n" +
                    " MERGE (k:Keyword {value:keyword})" +
                    " MERGE (k)-[:DESCRIBE]->(b)" +

                    " WITH book, b\n" +
                    " UNWIND book.fos AS fos\n" +
                    " MERGE (f:FOS {value:fos})" +
                    " MERGE (b)-[:STUDIES]->(f)" +

                    " WITH book, b\n" +
                    " UNWIND book.url AS url\n" +
                    " MERGE (u:Url {value:url})" +
                    " MERGE (b)-[:LINKED]->(u)");
        }

        driver.close();
    }
}
