package mse.advDB;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import java.io.File;
import java.io.IOException;

public class Example {
    private static final long MAX_FILE_SIZE = (long) 2 * 1024 * 1024 * 1024;

    public static void main(String[] args) throws IOException, InterruptedException {

        // JSONCreator.parse("./dblpExample.json", "big", MAX_FILE_SIZE);

        String jsonPath;
        int nbArticles;
        String neo4jIP;
        String filesFolder;

        if (args.length > 0 && args[0].equals("local")) {
            jsonPath = "dblpv13.json";
            System.out.println("Path to JSON file is " + jsonPath);
            nbArticles = 5167;
            System.out.println("Number of articles to consider is " + nbArticles);
            neo4jIP = "172.24.0.10";
            System.out.println("IP addresss of neo4j server is " + neo4jIP);
            filesFolder = "files";
        } else {
            jsonPath = System.getenv("JSON_FILE");
            System.out.println("Path to JSON file is " + jsonPath);
            nbArticles = Integer.max(1000, Integer.parseInt(System.getenv("MAX_NODES")));
            System.out.println("Number of articles to consider is " + nbArticles);
            neo4jIP = System.getenv("NEO4J_IP");
            System.out.println("IP addresss of neo4j server is " + neo4jIP);
            filesFolder = System.getenv("FILES_FOLDER");
        }

        JSONParser.parse(jsonPath, filesFolder + "/db", MAX_FILE_SIZE, nbArticles);

        Driver driver = GraphDatabase.driver("bolt://" + neo4jIP + ":7687", AuthTokens.basic("neo4j", "test"));
        boolean connected = false;
        do {
            try {
                System.out.println("Sleeping a bit waiting for the db");
                Thread.yield();
                Thread.sleep(5000); // let some time for the neo4j container to be up and
                driver.verifyConnectivity();
                connected = true;
            } catch (Exception e) {
            }
        } while (!connected);
        System.out.println("Connected");

        driver.session().run("MATCH (n) DETACH DELETE n");// delete all nodes

        driver.session().run("CALL apoc.load.directory()");

        for (File file : new File(filesFolder).listFiles()) {

            driver.session().run("CALL apoc.load.json('" + file.getName() + "')\n" +
                    " YIELD value\n" +
                    " UNWIND value AS book\n" +
                    " MERGE (b:Book { id: book._id })\n" +
                    " ON CREATE SET \n" +
                    " b.title = book.title, \n" +
                    " b.year = book.year, \n" +
                    " b.n_citation = book.n_citation, \n" +
                    " b.lang = book.lang, \n" +
                    " b.page_start = book.page_start, \n" +
                    " b.page_end = book.page_end, \n" +
                    " b.volume = book.volume, \n" +
                    " b.issue = book.issue, \n" +
                    " b.issn = book.issn, \n" +
                    " b.isbn = book.isbn, \n" +
                    " b.doi = book.doi, \n" +
                    " b.pdf = book.pdf, \n" +
                    " b.abstract = book.abstract \n" +

                    " WITH book, b\n" +
                    " UNWIND book.authors AS author\n" +
                    " MERGE (a:Author { \n" +
                    " id:author._id, \n" +
                    " name:author.name \n" +
                    "}) \n" +
                    " ON CREATE SET \n" +
                    " a.org = author.org, \n" +
                    " a.orgid = author.orgid \n" +

                    " MERGE (a)-[:WRITED]->(b) \n" +

                    " MERGE (v:Venue {" +
                    " id:book.venue._id," +
                    " type:book.venue.type," +
                    " raw:book.venue.raw" +
                    "})" +
                    " ON CREATE SET \n" +
                    " v.raw_zh = book.venue.raw_zh \n" +
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
