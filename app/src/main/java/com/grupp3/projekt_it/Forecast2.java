package com.grupp3.projekt_it;

/**
 * Created by Daniel on 2015-05-11.
 */
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast2{
    private City city;
    private int cnt;
    private int cod;
    private ForecastList [] list;
    private Double message;

    public City getCity(){
        return this.city;
    }
    public void setCity(City city){
        this.city = city;
    }
    public int getCnt(){
        return this.cnt;
    }
    public void setCnt(int cnt){
        this.cnt = cnt;
    }
    public int getCod(){
        return this.cod;
    }
    public void setCod(int cod){
        this.cod = cod;
    }
    public ForecastList[] getList(){
        return this.list;
    }
    public void setList(ForecastList[] list){
        this.list = list;
    }
    public Double getMessage(){
        return this.message;
    }
    public void setMessage(Double message){
        this.message = message;
    }
}
class ForecastList{
    private Clouds clouds;
    private Long dt;
    private String dt_txt;
    private ForecastMain main;
    private Rain rain;
    private ForecastSys sys;
    private ForecastWeather[] weather;
    private Wind wind;

    public Clouds getClouds(){
        return this.clouds;
    }
    public void setClouds(Clouds clouds){
        this.clouds = clouds;
    }
    public Long getDt(){
        return this.dt;
    }
    public void setDt(Long dt){
        this.dt = dt;
    }
    public String getDt_txt(){
        return this.dt_txt;
    }
    public void setDt_txt(String dt_txt){
        this.dt_txt = dt_txt;
    }
    public ForecastMain getMain(){
        return this.main;
    }
    public void setMain(ForecastMain main){
        this.main = main;
    }
    public Rain getRain(){
        return this.rain;
    }
    public void setRain(Rain rain){
        this.rain = rain;
    }
    public ForecastSys getSys(){
        return this.sys;
    }
    public void setSys(ForecastSys sys){
        this.sys = sys;
    }
    public ForecastWeather[] getWeather(){
        return this.weather;
    }
    public void setWeather(ForecastWeather[] weather){
        this.weather = weather;
    }
    public Wind getWind(){
        return this.wind;
    }
    public void setWind(Wind wind){
        this.wind = wind;
    }
}
class ForecastMain{
    private Double grnd_level;
    private Double humidity;
    private Double pressure;
    private Double sea_level;
    private Double temp;
    private Double temp_kf;
    private Double temp_max;
    private Double temp_min;

    public Double getGrnd_level(){
        return this.grnd_level;
    }
    public void setGrnd_level(Double grnd_level){
        this.grnd_level = grnd_level;
    }
    public Double getHumidity(){
        return this.humidity;
    }
    public void setHumidity(Double humidity){
        this.humidity = humidity;
    }
    public Double getPressure(){
        return this.pressure;
    }
    public void setPressure(Double pressure){
        this.pressure = pressure;
    }
    public Double getSea_level(){
        return this.sea_level;
    }
    public void setSea_level(Double sea_level){
        this.sea_level = sea_level;
    }
    public Double getTemp(){
        return this.temp;
    }
    public void setTemp(Double temp){
        this.temp = temp;
    }
    public Double getTemp_kf(){
        return this.temp_kf;
    }
    public void setTemp_kf(Double temp_kf){
        this.temp_kf = temp_kf;
    }
    public Double getTemp_max(){
        return this.temp_max;
    }
    public void setTemp_max(Double temp_max){
        this.temp_max = temp_max;
    }
    public Double getTemp_min(){
        return this.temp_min;
    }
    public void setTemp_min(Double temp_min){
        this.temp_min = temp_min;
    }
}
class ForecastRain{
    @SerializedName("3h")
    private Double _3h;

    public Double get3h(){
        return this._3h;
    }
    public void set3h(Double _3h){
        this._3h = _3h;
    }
}
class ForecastSnow{
    @SerializedName("3h")
    private Double _3h;

    public Double get3h(){
        return this._3h;
    }
    public void set3h(Double _3h){
        this._3h = _3h;
    }
}
class ForecastSys{
    private Long population;

    public Long getPopulation(){
        return this.population;
    }
    public void setPopulation(Long population){
        this.population = population;
    }
}
class City{
    private ForecastCoord coord;
    private String country;
    private Long id;
    private String name;
    private Long population;
    private ForecastSys sys;

    public ForecastCoord getCoord(){
        return this.coord;
    }
    public void setCoord(ForecastCoord coord){
        this.coord = coord;
    }
    public String getCountry(){
        return this.country;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public Long getId(){
        return this.id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Long getPopulation(){
        return this.population;
    }
    public void setPopulation(Long population){
        this.population = population;
    }
    public ForecastSys getSys(){
        return this.sys;
    }
    public void setSys(ForecastSys sys){
        this.sys = sys;
    }
}
class ForecastClouds{
    private Long all;

    public Long getAll(){
        return this.all;
    }
    public void setAll(Long all){
        this.all = all;
    }
}
class ForecastCoord{
    private Double lat;
    private Double lon;

    public Double getLat(){
        return this.lat;
    }
    public void setLat(Double lat){
        this.lat = lat;
    }
    public Double getLon(){
        return this.lon;
    }
    public void setLon(Double lon){
        this.lon = lon;
    }
}
class ForecastWeather{
    int id;
    String main;
    String description;
    String icon;
}
