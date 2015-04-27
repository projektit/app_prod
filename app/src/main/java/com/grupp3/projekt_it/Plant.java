package com.grupp3.projekt_it;

/**
 * Created by Daniel on 2015-04-22.
 */

//Plant POJO
public class Plant {
    int id;
    String name;
    String swe_name;
    String latin_name;
    String type;
    String soil;
    String zone_min;
    String zone_max;
    int water;
    String misc;
    int sun;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSwe_name(String swe_name) {
        this.swe_name = swe_name;
    }

    public void setLatin_name(String latin_name) {
        this.latin_name = latin_name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public void setZone_min(String zone_min) {
        this.zone_min = zone_min;
    }

    public void setZone_max(String zone_max) {
        this.zone_max = zone_max;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }

    public String getSwe_name() {
        return swe_name;
    }

    public String getLatin_name() {
        return latin_name;
    }

    public String getType() {
        return type;
    }

    public String getSoil() {
        return soil;
    }

    public String getZone_min() {
        return zone_min;
    }

    public String getZone_max() {
        return zone_max;
    }

    public int getWater() {
        return water;
    }

    public String getMisc() {
        return misc;
    }

    public int getSun() {
        return sun;
    }
}

