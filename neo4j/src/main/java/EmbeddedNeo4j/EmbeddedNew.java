package EmbeddedNeo4j;

import java.io.File;
import java.io.IOException;
import org.neo4j.graphdb.Direction;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class EmbeddedNew {

    private static final String DB_PATH = "target/neo4j-hello-db";
    public String greeting;

    GraphDatabaseService graphDB;

    Node firstNode;
    Node secondNode;
    Relationship relationship;

    public static void main(final String[] args) throws IOException {
        EmbeddedNew hello = new EmbeddedNew();
        hello.createDB();
        hello.removeData();
        hello.shutDown();

    }

    private static enum RelTypes implements RelationshipType {
        KNOWS
    }

    void createDB() throws IOException {
        FileUtils.deleteRecursively(new File(DB_PATH));

        //startdb
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerDB(graphDB);

        
        try (Transaction tx = graphDB.beginTx()) {
            firstNode = graphDB.createNode();
            firstNode.setProperty("message", "//hello FirstNode//");
            firstNode.setProperty("nombre", "Santiago");
            
            
            secondNode = graphDB.createNode();
            secondNode.setProperty("message", "hello SecondNode//");
            secondNode.setProperty("apellido", "suarez");
            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty("message", "relacion FisrtNode con SecondNode");

            System.out.print(firstNode.getProperty("message"));
            System.out.print(relationship.getProperty("message"));
            System.out.print(secondNode.getProperty("apellido"));

//            concatenar(firstNode, relationship, secondNode);

            tx.success();

        }

    }

    void shutDown() {
        System.out.println("shutdown database Neo4j");
        graphDB.shutdown();
    }

    void removeData() {
        try (Transaction tx = graphDB.beginTx()) {
            firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
            firstNode.delete();
            secondNode.delete();
            tx.success();
        }
    }

    public static void concatenar(Node n1, Relationship r, Node n2) {
        System.out.println("CONCATENANDO NODOS: " + n1.getProperty("message"));

    }

    public static void registerDB(final GraphDatabaseService graphDB) {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                graphDB.shutdown();
            }
        });
    }
}
