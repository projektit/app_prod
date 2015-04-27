package com.grupp3.projekt_it;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// POJO to garden object (which is in json format) to json string, for storage

public class Garden {
    String location;
    String name;
    String dbTableName;
    List<Plant> plants;
    Forecast forecast;
    public Garden(){

    }
    public Garden(String name, String location){
        this.location = location;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }

    public void addPlant(Plant plant){
        plants.add(plant);
    }
    public List<Plant> getPlants() {
        return plants;
    }
}
