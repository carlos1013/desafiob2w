package com.starwars.desafiob2w.persistence;

import com.mongodb.client.MongoCollection;
import com.starwars.desafiob2w.model.Planet;
import com.starwars.desafiob2w.utils.PlanetUtils;
import mockit.Mocked;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.*;

public class MongoDBConnectionTests {
    @Test
    public void getMongoCollectionWithMongoClientNull(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        mongoDBConnection.setMongoClient(null);

        try {
            mongoDBConnection.getMongoCollection("planets");
            fail("Expected Exception to be thrown");
        }
        catch (Exception expected){
            assertEquals("Mongo client is null, cannot get database",
                    expected.getMessage());
        }
    }

    @Test
    public void getMongoCollectionHappyPath(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        try {
            MongoCollection<Document> expectedCollection =
                    mongoDBConnection.getMongoCollection("planets");
            assertNotNull(expectedCollection);
        }
        catch (Exception expected){
            fail("Expected Exception to be thrown");
        }
    }

    @Test
    public void convertDocumentToPlanetNullDocument(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        Planet response = mongoDBConnection.convertDocumentToPlanet(null);
        assertNull(response);
    }

    @Test
    public void compareDocumentsEmptyDocuments(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        Document d1 = new Document();
        Document d2 = new Document();
        assertTrue(mongoDBConnection.compareDocuments(d1, d2));
    }

    @Test
    public void compareDocumentsWithDifferentIds(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();

        Document d1 = new Document();
        d1.put("_id", "ID240");
        d1.put("name", "Planet");
        d1.put("terrain", "Jungle");
        d1.put("climate", "Hot");
        d1.put("numberofappearances", 2);

        Document d2 = new Document();
        d2.put("_id", "ID245");
        d2.put("name", "Planet");
        d2.put("terrain", "Jungle");
        d2.put("climate", "Hot");
        d2.put("numberofappearances", 2);

        assertFalse(mongoDBConnection.compareDocuments(d1, d2));
    }

    @Test
    public void compareDocumentsEqualDocuments(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();

        Document d1 = new Document();
        d1.put("_id", "ID240");
        d1.put("name", "Planet");
        d1.put("terrain", "Jungle");
        d1.put("climate", "Hot");
        d1.put("numberofappearances", 2);

        Document d2 = new Document();
        d2.put("_id", "ID240");
        d2.put("name", "Planet");
        d2.put("terrain", "Jungle");
        d2.put("climate", "Hot");
        d2.put("numberofappearances", 2);

        assertTrue(mongoDBConnection.compareDocuments(d1, d2));
    }

    @Test
    public void convertDocumentToPlanetEmptyDocument(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();

        Document document = new Document();
        Planet expected = new Planet();
        Planet response = mongoDBConnection.convertDocumentToPlanet(document);
        assertTrue(PlanetUtils.comparePlanets(expected, response));
    }

    @Test
    public void convertDocumentToPlanetHappyPath(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();

        Document document = new Document();
        document.put("_id", new ObjectId("601713268fac7273a9c79323"));
        document.put("name", "Planet Xablau");
        document.put("climate", "Cold");
        document.put("terrain", "Desert");
        document.put("numberofappearances", 2);

        Planet expected = new Planet();
        expected.setId("601713268fac7273a9c79323");
        expected.setName("Planet Xablau");
        expected.setClimate("Cold");
        expected.setTerrain("Desert");
        expected.setNumberofappearances(2);

        Planet response = mongoDBConnection.convertDocumentToPlanet(document);
        assertTrue(PlanetUtils.comparePlanets(expected, response));
    }

    @Test
    public void createDocumentFromPlanetNullPlanet(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        Document response = mongoDBConnection.createDocumentFromPlanet(null);
        assertNull(response);
    }

    @Test
    public void createDocumentFromPlanetEmptyPlanet(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        Planet planet = new Planet();
        Document expected = new Document();
        Document response = mongoDBConnection.createDocumentFromPlanet(planet);
        assertTrue(mongoDBConnection.compareDocuments(expected, response));
    }

    @Test
    public void createDocumentFromPlanetHappyPath(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        Planet planet = new Planet();
        planet.setName("Planet Xablau");
        planet.setClimate("Cold");
        planet.setTerrain("Desert");
        planet.setNumberofappearances(2);

        Document expected = new Document();
        expected.put("name", "Planet Xablau");
        expected.put("climate", "Cold");
        expected.put("terrain", "Desert");
        expected.put("numberofappearances", 2);

        Document response = mongoDBConnection.createDocumentFromPlanet(planet);
        assertTrue(mongoDBConnection.compareDocuments(expected, response));
    }

    @Test
    public void getPlanetFromParametersMongoCollectionException(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        mongoDBConnection.setMongoClient(null);

        try {
            mongoDBConnection.getPlanetFromParameters(null, null);
            fail("Expected Exception to be thrown");
        } catch (Exception expected) {
            assertEquals("Mongo client is null, cannot get database",
                    expected.getMessage());
        }
    }

    @Test
    public void getPlanetFromParametersBothParametersNull(){
        MongoDBConnection mongoDBConnection = new MongoDBConnection();

        try {
            mongoDBConnection.getPlanetFromParameters(null, null);
            fail("Expected Exception to be thrown");
        } catch (Exception expected) {
            assertEquals("None of the parameters are valid",
                    expected.getMessage());
        }
    }
}
