package com.grupp3.projekt_it;
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
    String water;
    String misc;
    String sun;
    String img_url;

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

    public void setWater(String water) {
        this.water = water;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public void setImg_url(String img_url) { this.img_url = img_url; }

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

    public String getWater() { return water; }

    public String getMisc() {
        return misc;
    }

    public String getSun() {
        return sun;
    }

    public String getImg_url() { return img_url; }
}

