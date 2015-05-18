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
//Object to hold information about the monthly advice boxes which appear in MainActivity
public class MainItemObject {
    //Holds html advice text
    private String htmlText;
    //Holds the image url
    private String imageUrl;

    //Contructor for the MainItemObject
    public MainItemObject(String htmlText, String imageUrl) {
        this.htmlText = htmlText;
        this.imageUrl = imageUrl;
    }
    //Method which allows external calls to acces object htmlText
    public String getHtmlText() {
        return htmlText;
    }
    //Method which allows external calls to acces object imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public String toString(){
        return htmlText + "            " + imageUrl;
    }
}
