package mse.advDB;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Result;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Example {


    public static void main(String[] args) throws IOException, InterruptedException {
        String jsonPath = System.getenv("JSON_FILE");
        System.out.println("Path to JSON file is " + jsonPath);
        int nbArticles = Integer.max(1000, Integer.parseInt(System.getenv("MAX_NODES")));
        System.out.println("Number of articles to consider is " + nbArticles);
        String neo4jIP = System.getenv("NEO4J_IP");
        System.out.println("IP addresss of neo4j server is " + neo4jIP);

        Driver driver = GraphDatabase.driver("bolt://" + neo4jIP + ":7687", AuthTokens.basic("neo4j", "test"));
        boolean connected = false;
        do {
            try {
                System.out.println("Sleeping a bit waiting for the db");
                Thread.yield();
                Thread.sleep(5000); // let some time for the neo4j container to be up and running

                driver.verifyConnectivity();
                connected = true;
            } catch (Exception e) {
            }
        } while (!connected);
        FileReader fr = new FileReader(jsonPath);
        BufferedReader br = new BufferedReader(fr);
        System.out.println("Reading first lines of the json file :");

        // Les requêtes !!!
        driver.session().run("MATCH (n) DETACH DELETE n");// delete all nodes
        driver.session().run("CREATE (n:Node)");// example of query

        System.out.println("jsonPath : " + jsonPath);


        // 2 versions un peu différentes, mais qui ne fonctionnent pas (vient peut-être d'ailleurs)

        // Result test = driver.session().run("CALL apoc.load.json('file://" + jsonPath + "')" + " YIELD value RETURN value;"); //load json
        Result test = driver.session().run("CALL apoc.load.json('" + jsonPath + "')"); //load json


        System.out.println("test : " + test);

        // view on http://localhost:7474/browser/
        // neo4j/test
        // MATCH (n) RETURN n 

        // base
        for (int i = 0; i < 100; i++) {
            System.out.println(br.readLine());
        }
        br.close();
        fr.close();

        driver.close();
    }
}
