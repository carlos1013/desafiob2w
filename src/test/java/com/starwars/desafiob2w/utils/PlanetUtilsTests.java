package com.starwars.desafiob2w.utils;

import com.starwars.desafiob2w.externalconnection.SWAPIConnection;
import com.starwars.desafiob2w.log.LogMessages;
import com.starwars.desafiob2w.model.Planet;
import com.starwars.desafiob2w.persistence.MongoDBConnection;
import mockit.*;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class PlanetUtilsTests {

    @Mocked
    public MongoDBConnection connection;

    @Test
    public void insertPlanetNullPlanet(){
        try {
            Planet planet = null;
            PlanetUtils.insertPlanet(planet);
            fail("Expected Exception to be thrown");
        } catch (Exception expected) {
            assertEquals("Planet was not present in request, please try again",
                    expected.getMessage());
        }
    }

    @Test
    public void insertPlanetAllFieldsNull(){
        try {
            Planet planet = new Planet();
            PlanetUtils.insertPlanet(planet);
            fail("Expected Exception to be thrown");
        } catch (Exception expected) {
            assertEquals("Missing planet name;Missing planet climate;Missing planet terrain;",
                    expected.getMessage());
        }
    }

    @Test
    public void insertPlanetNameNull(){
        try {
            Planet planet = new Planet(null, null, "Terrain",
                    "Climate", null);
            PlanetUtils.insertPlanet(planet);
            fail("Expected Exception to be thrown");
        } catch (Exception expected) {
            assertEquals("Missing planet name;",
                    expected.getMessage());
        }
    }

    @Test
    public void insertPlanetPersistException() throws Exception {
        Planet planet = new Planet(null, "Hoth", "Terrain",
                "Climate", null);

        new MockUp<SWAPIConnection>() {
            @Mock
            public Integer getNumberOfAppearances(String planetName) {
                return 3;
            }
        };

        new Expectations(){{
            connection.persistPlanet((Planet)any);
            result = new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
            times = 1;
        }};

        try {
            PlanetUtils.insertPlanet(planet);
            fail("Expected Exception to be thrown");
        }
        catch (Exception expected){
            assertEquals(LogMessages.UNEXPECTED_ERROR_MESSAGE,
                    expected.getMessage());
        }
    }

    @Test
    public void areFieldsEqualObject1Null(){
        Object obj1 = null;
        Object obj2 = "Teste";
        assertFalse(PlanetUtils.areFieldsEqual(obj1, obj2));
    }

    @Test
    public void areFieldsEqualObject2Null(){
        Object obj1 = "Teste";
        Object obj2 = null;
        assertFalse(PlanetUtils.areFieldsEqual(obj1, obj2));
    }

    @Test
    public void areFieldsEqualObjectEqualStrings(){
        Object obj1 = "Teste";
        Object obj2 = "Teste";
        assertTrue(PlanetUtils.areFieldsEqual(obj1, obj2));
    }

    @Test
    public void areFieldsEqualObjectDifferentStrings(){
        Object obj1 = "Teste";
        Object obj2 = "Teste2";
        assertFalse(PlanetUtils.areFieldsEqual(obj1, obj2));
    }

    @Test
    public void comparePlanetsEmptyPlanets(){
        Planet p1 = new Planet();
        Planet p2 = new Planet();
        assertTrue(PlanetUtils.comparePlanets(p1, p2));
    }

    @Test
    public void comparePlanetsWithDifferentIds(){
        Planet p1 = new Planet("ID240", "Planet", "Jungle",
                "Hot", 2);
        Planet p2 = new Planet("ID245", "Planet", "Jungle",
                "Hot", 2);
        assertFalse(PlanetUtils.comparePlanets(p1, p2));
    }

    @Test
    public void comparePlanetsEqualPlanets(){
        Planet p1 = new Planet("ID240", "Planet", "Jungle",
                "Hot", 2);
        Planet p2 = new Planet("ID240", "Planet", "Jungle",
                "Hot", 2);
        assertTrue(PlanetUtils.comparePlanets(p1, p2));
    }

    @Test
    public void insertPlanetHappyPath() throws Exception {
        Planet planet = new Planet(null, "Hoth", "Terrain",
                "Climate", null);

        Planet expected = new Planet("ID616", "Hoth", "Terrain",
                "Climate", 2);

        new MockUp<SWAPIConnection>() {
            @Mock
            public Integer getNumberOfAppearances(String planetName) {
                return 2;
            }
        };

        new Expectations(){{
            connection.persistPlanet((Planet)any);

            result = new Planet("ID616", "Hoth", "Terrain",
                    "Climate", 2);

            times = 1;
        }};

        Planet response = PlanetUtils.insertPlanet(planet);
        assertTrue(PlanetUtils.comparePlanets(response, expected));
    }

    @Test
    public void getPlanetsPersistenceException() throws Exception {
        new Expectations(){{
            connection.getPlanets();
            result = new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
            times = 1;
        }};

        try {
            PlanetUtils.getPlanets();
            fail("Expected Exception to be thrown");
        }
        catch (Exception expected){
            assertEquals(LogMessages.UNEXPECTED_ERROR_MESSAGE,
                    expected.getMessage());
        }
    }

    @Test
    public void getPlanetsHappyPath() throws Exception {
        Planet expected = new Planet("ID616", "Hoth", "Terrain",
                "Climate", 2);

        new Expectations(){{
            connection.getPlanets();

            Planet p = new Planet("ID616", "Hoth", "Terrain",
                    "Climate", 2);

            Vector<Planet> vtrPlanets = new Vector<>();
            vtrPlanets.add(p);

            result = vtrPlanets;

            times = 1;
        }};

        Vector<Planet> response = PlanetUtils.getPlanets();
        assertTrue(PlanetUtils.comparePlanets(response.get(0), expected));
    }

    @Test
    public void getPlanetFromParametersBothFieldsNotValidException() throws Exception {
        try {
            PlanetUtils.getPlanetFromParameters(null, null);
            fail("Expected Exception to be thrown");
        }
        catch (Exception expected){
            assertEquals("No valid parameter found for request",
                    expected.getMessage());
        }
    }

    @Test
    public void getPlanetFromParametersPersistenceException() throws Exception {
        new Expectations(){{
            connection.getPlanetFromParameters((String)any, (String)any);
            result = new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
            times = 1;
        }};

        try {
            PlanetUtils.getPlanetFromParameters("ID908", null);
            fail("Expected Exception to be thrown");
        }
        catch (Exception expected){
            assertEquals(LogMessages.UNEXPECTED_ERROR_MESSAGE,
                    expected.getMessage());
        }
    }

    @Test
    public void getPlanetFromParametersHappyPath() throws Exception {
        Planet expected = new Planet("ID616", "Hoth", "Terrain",
                "Climate", 2);

        new Expectations(){{
            connection.getPlanetFromParameters((String)any, (String)any);

            result = new Planet("ID616", "Hoth", "Terrain",
                    "Climate", 2);

            times = 1;
        }};

        Planet response = PlanetUtils.getPlanetFromParameters("ID908", null);
        assertTrue(PlanetUtils.comparePlanets(response, expected));
    }

    @Test
    public void deletePlanetByIdIdNotValidException() throws Exception {
        try {
            PlanetUtils.deletePlanetById(null);
            fail("Expected Exception to be thrown");
        }
        catch (Exception expected){
            assertEquals("No valid id passed to request, please try again",
                    expected.getMessage());
        }
    }

    @Test
    public void deletePlanetByIdPersistenceException() throws Exception {
        new Expectations(){{
            connection.deletePlanet((String)any);
            result = new Exception(LogMessages.UNEXPECTED_ERROR_MESSAGE);
            times = 1;
        }};

        try {
            PlanetUtils.deletePlanetById("ID908");
            fail("Expected Exception to be thrown");
        }
        catch (Exception expected){
            assertEquals(LogMessages.UNEXPECTED_ERROR_MESSAGE,
                    expected.getMessage());
        }
    }

    @Test
    public void deletePlanetByIdHappyPath() throws Exception {
        new Expectations(){{
            connection.deletePlanet((String)any);
            times = 1;
        }};

        try {
            PlanetUtils.deletePlanetById("ID908");
        }
        catch (Exception exp){
            fail("Exception thrown during execution");
        }
    }
}
