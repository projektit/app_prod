package com.grupp3.projekt_it;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel on 2015-04-15.
 */

public class Forecast {
    Long id;
    Long dt;
    String name;

    Coordinates coord;
    System sys;

    Integer cod;

    Wind wind;
    Clouds clouds;
    Weather[] weather;
    Main main;
    Rain rain;
    Snow snow;

    public Main getMain() {
        return main;
    }

    public Long getDt() {
        return dt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCod() {
        return cod;
    }

    public Coordinates getCoord() {
        return coord;
    }

    public System getSys() {
        return sys;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Snow getSnow() {
        return snow;
    }

    public Rain getRain() {
        return rain;
    }

}

class Main {
    double temp;
    double humidity;
    double pressure;
    double temp_min;
    double temp_max;
    double sea_level;
    double grnd_level;

    public double getGrnd_level() {
        return grnd_level;
    }

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getSea_level() {
        return sea_level;
    }
}

class Coordinates {
    double lat;
    double lon;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String toString()  {
        return "(" +lat +", " + lon+ ")";
    }
}

class Weather {
    Integer id;
    String main;
    String description;
    String icon;

    public String getIcon() {
        return icon;
    }

    public Integer getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }
}

class System {
    String message;
    String country;
    Long sunrise;
    Long sunset;

    public Long getSunset() {
        return sunset;
    }

    public String getMessage() {
        return message;
    }

    public String getCountry() {
        return country;
    }

    public Long getSunrise() {
        return sunrise;
    }
}

class Wind {
    double speed;
    double deg;

    public double getDeg() {
        return deg;
    }

    public double getSpeed() {
        return speed;
    }
}

class Clouds {
    double all;

    public double getAll() {
        return all;
    }
}

class Rain{
    @SerializedName("3h")
    double _3h;

    public double get_3h(){
        return  _3h;
    }
}
class Snow{
    @SerializedName("3h")
    double _3h;

    public double get_3h(){
        return  _3h;
    }
}
