package com.grupp3.projekt_it;

import java.util.ArrayList;

// POJO to garden object (which is in json format) to json string, for storage

public class Garden {
    String location;
    String name;
    ArrayList <String> flowers;
    Forecast forecast;

    public Garden(String name, String location){
        this.location = location;
        this.name = name;
        this.flowers = new ArrayList<String>();
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setForecast(Forecast forecast){
        this.forecast = forecast;
    }

    public Forecast getForecast() {
        return forecast;
    }
}
