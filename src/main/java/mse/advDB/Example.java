package mse.advDB;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Example {
    // private static final long MAX_FILE_SIZE = (long)2 * 1024 * 1024 * 1024; // 3
    // Go
    private static final long MAX_FILE_SIZE = 1024; // 3 Go

    public static void main(String[] args) throws IOException, InterruptedException {

        String jsonPath;
        int nbArticles;
        String neo4jIP;

        if (args.length > 0 && args[0].equals("local")) {
            System.out.println("POUET");
            jsonPath = "./dblpExample.json";
            System.out.println("Path to JSON file is " + jsonPath);
            nbArticles = 10000;
            System.out.println("Number of articles to consider is " + nbArticles);
            neo4jIP = "172.24.0.10";
            System.out.println("IP addresss of neo4j server is " + neo4jIP);
        } else {
            System.out.println("POUIT");
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
                Thread.sleep(5000); // let some time for the neo4j container to be up and
                // running

                driver.verifyConnectivity();
                connected = true;
            } catch (Exception e) {
            }
        } while (!connected);

        for (File file : new File("files").listFiles()) {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while (br.ready()) {

                String line = br.readLine();
                System.out.println(line);

                ///////////////////////
                // Do the DB stuff here
                ///////////////////////

            }
            fr.close();
            br.close();
        }

        driver.close();
    }
}
