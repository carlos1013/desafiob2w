package com.starwars.desafiob2w.persistence;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.starwars.desafiob2w.model.Planet;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Vector;

public class MongoDBConnection {
    //use this as a static variable for now
    public static String COLLECTION_NAME = "planets";
    public static String DATABASE_NAME = "planets";

    private MongoClient mongoClient;

    public MongoDBConnection(){
        this.mongoClient = MongoClients.create();
    }

    public void setMongoClient(MongoClient mongoClient){
        this.mongoClient = mongoClient;
    }

    public void closeConnection() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
            this.mongoClient = null;
        }
    }

    //could point to a singleton instead of multiple instances
    public MongoCollection<Document> getMongoCollection(String collectionName)
            throws Exception {
        if (this.mongoClient == null) {
            throw new Exception("Mongo client is null, cannot get database");
        }

        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        return database.getCollection(collectionName);
    }

    public Vector<Planet> getPlanets() throws Exception {
        MongoCollection<Document> collection = getMongoCollection(COLLECTION_NAME);
        FindIterable<Document> iter = collection.find();
        MongoCursor<Document> cursor = iter.cursor();
        Vector<Planet> lstPlanets = new Vector<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            lstPlanets.add(convertDocumentToPlanet(document));
        }
        return lstPlanets;
    }

    public void deletePlanet(String id) throws Exception {
        MongoCollection<Document> collection = getMongoCollection(COLLECTION_NAME);
        collection.deleteOne(Filters.eq("_id", id));
    }

    public Planet getPlanetFromParameters(String id, String name) throws Exception {
        //get an instance of mongocollection
        MongoCollection<Document> collection = getMongoCollection(COLLECTION_NAME);

        Bson filter;
        if (id != null && id.length() > 0){
            //convert string to object id first
            ObjectId objectId = new ObjectId(id);
            filter = Filters.eq("_id", objectId);
        }
        else if (name != null && name.length() > 0){
            filter = Filters.eq("name", name);
        }
        else {
            throw new Exception("None of the parameters are valid");
        }

        Document document = collection.find(filter).first();
        //found an object, convert and return the first element found
        if (document != null){
            return convertDocumentToPlanet(document);
        }
        //no match found, return null
        return null;
    }

    public Planet persistPlanet(Planet planet) throws Exception {
        Document planetAsDocument = createDocumentFromPlanet(planet);
        MongoCollection<Document> collection = getMongoCollection(COLLECTION_NAME);
        InsertOneResult iOR = collection.insertOne(planetAsDocument);

        if (iOR.wasAcknowledged() && iOR.getInsertedId() != null){
            BsonValue bsonValue = iOR.getInsertedId();
            planet.setId(bsonValue.asObjectId().getValue().toString());
            return planet;
        }
        throw new Exception("Inserted object could not be acknowledged");
    }

    //all transformation methods must remain inside mongodbconnection
    //method to convert a bson document to a planet object
    private Planet convertDocumentToPlanet(Document document){
        if (document == null){
            return null;
        }

        Planet planet = new Planet();

        planet.setId(document.getObjectId("_id").toString());
        planet.setName(document.getString("name"));
        planet.setClimate(document.getString("climate"));
        planet.setTerrain(document.getString("terrain"));
        planet.setNumberofappearances(document.getInteger("numberofappearances", -1));

        return planet;
    }

    //method to convert a planet object to a bson document
    private Document createDocumentFromPlanet(Planet planet){
        Document document = new Document();
        document.append("name", planet.getName());
        document.append("climate", planet.getClimate());
        document.append("terrain", planet.getTerrain());
        document.append("numberofappearances", planet.getNumberofappearances());
        return document;
    }
}
