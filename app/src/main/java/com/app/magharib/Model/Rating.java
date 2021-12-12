package com.app.magharib.Model;

public class Rating {
    private String host_id ;
    private String traveler_id ;
    private int rating ;
    private String comment ;
    private String type ;
    private String date ;

    public String getTraveler_id() {
        return traveler_id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getHost_id() {
        return host_id;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
