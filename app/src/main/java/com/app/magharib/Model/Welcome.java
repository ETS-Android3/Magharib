package com.app.magharib.Model;

public class Welcome {
    private int image_welcome ;
    private String title ;
    private String description ;

    public Welcome(int image_welcome, String title, String description) {
        this.image_welcome = image_welcome;
        this.title = title;
        this.description = description;
    }

    public int getImage_welcome() {
        return image_welcome;
    }

    public void setImage_welcome(int image_welcome) {
        this.image_welcome = image_welcome;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
