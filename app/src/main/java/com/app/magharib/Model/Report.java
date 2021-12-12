package com.app.magharib.Model;

import java.io.Serializable;

public class Report implements Serializable {

    private String id_report ;
    private String id_user ;
    private String email_user ;
    private String id_user_report ;
    private String email_user_report ;

    public String getId_report() {
        return id_report;
    }

    public String getId_user() {
        return id_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public String getId_user_report() {
        return id_user_report;
    }

    public String getEmail_user_report() {
        return email_user_report;
    }
}
