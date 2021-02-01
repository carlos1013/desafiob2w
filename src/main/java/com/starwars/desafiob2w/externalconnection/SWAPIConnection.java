package com.starwars.desafiob2w.externalconnection;

import com.starwars.desafiob2w.log.LogMessages;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class SWAPIConnection {

    public static SWAPIGetResponse getResponseFromSWAPI(String planetName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject
                    ("https://swapi.dev/api/planets/?search=" + planetName,
                            SWAPIGetResponse.class);
        }
        catch (RestClientException exp){
            LogMessages.logError("Error connecting to SWAPI API: " + exp.getMessage());
            return null;
        }
    }

    //do not throw exceptions in this method, post cannot depend on an external API
    public static Integer getNumberOfAppearances(String planetName) {
        SWAPIGetResponse SWAPIGetResponse = getResponseFromSWAPI(planetName);

        //could not get a response from server, simply return
        if (SWAPIGetResponse == null || SWAPIGetResponse.getCount() == null){
            return -1;
        }

        //no result found for planet name
        if (SWAPIGetResponse.getCount() < 1){
            LogMessages.logError("Could not find planet with ("
                    + planetName + ") name in SWAPI API");
            return -1;
        }

        //more than one planet with name found
        if (SWAPIGetResponse.getCount() > 1){
            LogMessages.logError("Found more than one planet with ("
                    + planetName + ") name in SWAPI API");
            return -1;
        }

        //missformated exception
        if (SWAPIGetResponse.getResults() == null ||
                SWAPIGetResponse.getResults().size() != 1){
            LogMessages.logError("SWAPI returned a missformated response for ("
                    + planetName + ") - results list not consistent with count");
            return -1;
        }

        ExternalPlanet externalPlanet = SWAPIGetResponse.getResults().get(0);
        //missing informations
        if (externalPlanet == null || externalPlanet.getFilms() == null){
            LogMessages.logError("SWAPI returned missformated planet information for ("
                    + planetName + ")");
            return -1;
        }

        return externalPlanet.getFilms().size();
    }

}
