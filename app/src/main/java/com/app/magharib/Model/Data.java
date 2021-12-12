package com.app.magharib.Model;

public class Data {
    private String date ;
    private String place ;

    ////
    private String destination_city ;
    ////
    private int image_places ;
    private String name_places ;
    private String price_places ;

    ////


    public Data(String date, String place) {
        this.date = date;
        this.place = place;
    }

    public Data(String destination_city) {
        this.destination_city = destination_city;
    }

    public Data(int image_places, String name_places, String price_places) {
        this.image_places = image_places;
        this.name_places = name_places;
        this.price_places = price_places;
    }



    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public int getImage_places() {
        return image_places;
    }

    public String getName_places() {
        return name_places;
    }

    public String getPrice_places() {
        return price_places;
    }
}
