package com.grupp3.projekt_it;


// POJO to garden object (which is in json format) to json string, for storage
/*
 *
 * @author Marcus Elwin
 * @author Daniel Freberg
 * @author Esra Kahraman
 * @author Oscar Melin
 * @author Mikael Mölder
 * @author Erik Nordell
 * @author Felicia Schnell
 *
*/
/**
 * POJO class created to easily use googles GSON library to parse json String objects into java objects
 * Defines all information saved locally about a garden
 */
public class Garden {
    String tableName;
    String location;
    String name;
    int zone;
    Forecast forecast;
    Forecast2 forecast2;
    int picNumber;
    public Garden(){

    }

    public Forecast2 getForecast2() {
        return forecast2;
    }

    public void setForecast2(Forecast2 forecast2) {
        this.forecast2 = forecast2;
    }

    public Garden(String name, String location, String tableName, int zone, int picNumber){
        this.location = location;
        this.name = name;
        this.tableName = tableName;
        this.zone = zone;
        this.picNumber = picNumber;
    }

    public int getZone() {
        return zone;
    }

    public String getTableName() {
        return tableName;
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

    public int getPicNumber() {
        return picNumber;
    }
}
