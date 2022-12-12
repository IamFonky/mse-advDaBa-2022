package mse.advDB;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Query;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class DoDaJob {


    private final Driver driver;

    public DoDaJob(String uri, String user, String password) {        
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void printGreeting(final String message) {
        try (Session session = driver.session()) {
            Object greeting = session.executeWrite(tx -> {
                Query query = new Query("CREATE (a:Greeting) SET a.message = $message RETURN a.message + ', from node ' + id(a)", parameters("message", message));
                Result result = tx.run(query);
                return result.single().get(0).asString();
            });
            System.out.println(greeting);
        }
    }
    

}
