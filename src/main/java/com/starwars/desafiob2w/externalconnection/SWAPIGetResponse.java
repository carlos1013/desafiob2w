package com.starwars.desafiob2w.externalconnection;

import java.util.ArrayList;
import java.util.List;

public class SWAPIGetResponse {
    private Long count;
    String next;
    String previous;
    ArrayList<ExternalPlanet> results = new ArrayList <> ();

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<ExternalPlanet> getResults() {
        return results;
    }

    public void setResults(ArrayList<ExternalPlanet> results) {
        this.results = results;
    }
}
