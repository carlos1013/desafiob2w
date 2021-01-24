package com.starwars.desafiob2w.model;

public class Planet {
    public String id;
    public String name;
    public String climate;
    public String terrain;
    public Integer numberOfAppearances;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public Integer getNumberOfAppearances() {
        return numberOfAppearances;
    }

    public void setNumberOfAppearances(Integer numberOfAppearances) {
        this.numberOfAppearances = numberOfAppearances;
    }
}
