package com.app.magharib.Model;

import java.io.Serializable;

public class Experience implements Serializable {
    private String id_experience ;
    private String user_id ;
    private String first_name ;
    private String last_name ;
    private String type_experience ;
    private String image_uri_experience ;
    private String name_experience ;
    private String location_experience ;
    private String price_experience ;
    private String details_experience ;
    private String email_experience ;
    private String phone_experience ;
    private String is_enabled ;

    public String getId_experience() {
        return id_experience;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getType_experience() {
        return type_experience;
    }

    public String getImage_uri_experience() {
        return image_uri_experience;
    }

    public String getName_experience() {
        return name_experience;
    }

    public String getLocation_experience() {
        return location_experience;
    }

    public String getPrice_experience() {
        return price_experience;
    }

    public String getDetails_experience() {
        return details_experience;
    }

    public String getEmail_experience() {
        return email_experience;
    }

    public String getPhone_experience() {
        return phone_experience;
    }

    public String getIs_enabled() {
        return is_enabled;
    }
}
