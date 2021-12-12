package com.app.magharib.Model;

import java.io.Serializable;

public class Order implements Serializable {
    private String id_order ;
    private String user_id ;
    private String host_id ;
    private String first_name ;
    private String last_name ;
    private String age_user ;
    private String email_user ;
    private String date_order ;
    private String id_experience ;
    private String name_experience ;
    private String location_experience ;
    private String type_experience ;
    private String is_enabled ;
    private String is_traveler_rating ;
    private String is_host_rating ;

    public String getId_order() {
        return id_order;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getHost_id() {
        return host_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAge_user() {
        return age_user;
    }

    public String getDate_order() {
        return date_order;
    }

    public String getId_experience() {
        return id_experience;
    }

    public String getName_experience() {
        return name_experience;
    }

    public String getLocation_experience() {
        return location_experience;
    }

    public String getType_experience() {
        return type_experience;
    }

    public String getIs_enabled() {
        return is_enabled;
    }

    public String getEmail_user() {
        return email_user;
    }

    public String getIs_traveler_rating() {
        return is_traveler_rating;
    }

    public String getIs_host_rating() {
        return is_host_rating;
    }
}
