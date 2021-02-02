package com.starwars.desafiob2w.externalconnection;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SWAPIConnectionTests {
    @Mocked
    public RestTemplate restTemplate;

    @Test
    public void getNumberOfAppearancesCouldNotGetSWAPIResponse(){
        new MockUp<SWAPIConnection>() {
            @Mock
            public SWAPIGetResponse getResponseFromSWAPI(String planetName) {
                return null;
            }
        };

        Integer response = SWAPIConnection.getNumberOfAppearances("planetName");
        Integer expected = -1;
        assertEquals(expected, response);
    }

    @Test
    public void getNumberOfAppearancesSWAPIMoreThanOneResponse(){
        SWAPIGetResponse swapiGetResponse = new SWAPIGetResponse();
        swapiGetResponse.setCount(2L);

        new MockUp<SWAPIConnection>() {
            @Mock
            public SWAPIGetResponse getResponseFromSWAPI(String planetName) {
                return swapiGetResponse;
            }
        };

        Integer response = SWAPIConnection.getNumberOfAppearances("planetName");
        Integer expected = -1;
        assertEquals(expected, response);
    }

    @Test
    public void getNumberOfAppearancesSWAPILessThanOneResponse(){
        SWAPIGetResponse swapiGetResponse = new SWAPIGetResponse();
        swapiGetResponse.setCount(0L);

        new MockUp<SWAPIConnection>() {
            @Mock
            public SWAPIGetResponse getResponseFromSWAPI(String planetName) {
                return swapiGetResponse;
            }
        };

        Integer response = SWAPIConnection.getNumberOfAppearances("planetName");
        Integer expected = -1;
        assertEquals(expected, response);
    }

    @Test
    public void getNumberOfAppearancesSWAPIOneResponseWithInvalidResults(){
        SWAPIGetResponse swapiGetResponse = new SWAPIGetResponse();
        swapiGetResponse.setCount(1L);

        new MockUp<SWAPIConnection>() {
            @Mock
            public SWAPIGetResponse getResponseFromSWAPI(String planetName) {
                return swapiGetResponse;
            }
        };

        Integer response = SWAPIConnection.getNumberOfAppearances("planetName");
        Integer expected = -1;
        assertEquals(expected, response);
    }

    @Test
    public void getNumberOfAppearancesSWAPIExternalPlanetNotValid(){
        SWAPIGetResponse swapiGetResponse = new SWAPIGetResponse();
        swapiGetResponse.setCount(1L);
        ArrayList<ExternalPlanet> externalPlanetVector = new ArrayList<>();
        externalPlanetVector.add(null);
        swapiGetResponse.setResults(externalPlanetVector);

        new MockUp<SWAPIConnection>() {
            @Mock
            public SWAPIGetResponse getResponseFromSWAPI(String planetName) {
                return swapiGetResponse;
            }
        };

        Integer response = SWAPIConnection.getNumberOfAppearances("planetName");
        Integer expected = -1;
        assertEquals(expected, response);
    }

    @Test
    public void getNumberOfAppearancesHappyPath(){
        SWAPIGetResponse swapiGetResponse = new SWAPIGetResponse();
        swapiGetResponse.setCount(1L);

        ArrayList<ExternalPlanet> externalPlanetVector = new ArrayList<>();
        ExternalPlanet externalPlanet = new ExternalPlanet();
        ArrayList<String> filmsVector = new ArrayList<>();
        filmsVector.add("The Empire Strikes Back");
        externalPlanet.setFilms(filmsVector);

        externalPlanetVector.add(externalPlanet);
        swapiGetResponse.setResults(externalPlanetVector);

        new MockUp<SWAPIConnection>() {
            @Mock
            public SWAPIGetResponse getResponseFromSWAPI(String planetName) {
                return swapiGetResponse;
            }
        };

        Integer response = SWAPIConnection.getNumberOfAppearances("Hoth");
        Integer expected = 1;
        assertEquals(expected, response);
    }

    @Test
    public void getResponseFromSWAPIHappyPath(){
        new Expectations(){{
            restTemplate.getForObject((String)any, SWAPIGetResponse.class);
            result = new SWAPIGetResponse();
            times = 1;
        }};

        SWAPIGetResponse expected = new SWAPIGetResponse();
        SWAPIGetResponse response = SWAPIConnection.getResponseFromSWAPI("Hoth");
        if (response == null){
            fail("Response from getResponseFromSWAPI is null");
        }

        assertEquals(expected.getClass(), response.getClass());
    }

    @Test
    public void getResponseFromSWAPIRestClientException(){
        new Expectations(){{
            restTemplate.getForObject((String)any, SWAPIGetResponse.class);
            result = new RestClientException("");
            times = 1;
        }};

        SWAPIGetResponse response = SWAPIConnection.getResponseFromSWAPI("Hoth");
        assertNull(response);
    }
}
