package com.starwars.desafiob2w.utils;

import com.starwars.desafiob2w.externalconnection.SWAPIConnection;
import com.starwars.desafiob2w.log.LogMessages;
import com.starwars.desafiob2w.model.Planet;
import com.starwars.desafiob2w.persistence.MongoDBConnection;

import java.util.Vector;

public class PlanetUtils {

    public static String createStringFromStringVector(Vector<String> stringVector){
        StringBuilder sb = new StringBuilder();
        for (String s : stringVector) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static boolean isFieldsFilled(Planet planet, Vector<String> errorMessages) {
        if (planet.getName() == null || planet.getName().length() < 1) {
            errorMessages.add("Missing planet name;");
        }
        if (planet.getClimate() == null || planet.getClimate().length() < 1) {
            errorMessages.add("Missing planet climate;");
        }
        if (planet.getTerrain() == null || planet.getTerrain().length() < 1) {
            errorMessages.add("Missing planet terrain;");
        }
        return errorMessages.size() <= 0;
    }

    public static Planet insertPlanet(Planet planet) throws Exception {
        if (planet == null){
            LogMessages.logError("insertPlanet called with null value for planet");
            throw new Exception("Planet was not present in request, please try again");
        }

        //first validate object fields, in this case we consider name,
        // terrain and climate as not null
        Vector<String> errorMessages = new Vector<>();
        if (!isFieldsFilled(planet, errorMessages)){
            throw new Exception(createStringFromStringVector(errorMessages));
        }

        //call to api to fill number of appearences
        //errors in swapi connection should not invalidate post request!!!
        Integer nAppearences = SWAPIConnection.getNumberOfAppearances(planet.getName());
        planet.setNumberofappearances(nAppearences);

        //call persist and return new object
        MongoDBConnection connection = null;
        Planet newPlanet;
        try {
            connection = new MongoDBConnection();
            newPlanet = connection.persistPlanet(planet);
        }
        catch(Exception exp){
            LogMessages.logError(exp.getMessage());
            throw new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
        }
        finally {
            if (connection != null) {
                connection.closeConnection();
            }
        }

        return newPlanet;
    }

    //we don't use parameters, so just open a connection and retrieve all planets
    public static Vector<Planet> getPlanets() throws Exception {
        MongoDBConnection connection = null;
        Vector<Planet> lstPlanets;
        try {
            connection = new MongoDBConnection();
            lstPlanets = connection.getPlanets();
        }
        catch(Exception exp){
            LogMessages.logError(exp.getMessage());
            throw new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
        }
        finally {
            if (connection != null) {
                connection.closeConnection();
            }
        }

        return lstPlanets;
    }

    public static void deletePlanetById(String id) throws Exception {
        if (id == null || id.length() < 1){
            LogMessages.logError("deletePlanetById called with no valid id - unable to process");
            throw new Exception("No valid id passed to request, please try again");
        }

        MongoDBConnection connection = null;
        try {
            connection = new MongoDBConnection();
            connection.deletePlanet(id);
        }
        catch(Exception exp){
            LogMessages.logError(exp.getMessage());
            throw new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
        }
        finally {
            if (connection != null) {
                connection.closeConnection();
            }
        }
    }

    public static Planet getPlanetFromParameters(String id, String name) throws Exception {
        //we don't have any valid parameter
        if ((id == null || id.length() < 1) && (name == null || name.length() < 1)){
            LogMessages.logError("getPlanetFromParameters called with no valid parameter");
            throw new Exception("No valid parameter found for request");
        }

        MongoDBConnection connection = null;
        Planet planet;
        try {
            connection = new MongoDBConnection();
            planet = connection.getPlanetFromParameters(id, name);
        }
        catch(Exception exp){
            LogMessages.logError(exp.getMessage());
            throw new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
        }
        finally {
            if (connection != null) {
                connection.closeConnection();
            }
        }

        return planet;
    }
}
