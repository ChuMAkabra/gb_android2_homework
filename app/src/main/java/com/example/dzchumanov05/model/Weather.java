package com.example.dzchumanov05.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Weather implements Serializable {
    //    weather.id Weather condition id
    @Expose
    private String main; // Group of weather parameters (Rain, Snow, Extreme etc.)
    @Expose
    private String description; // Weather condition within the group (full list of weather conditions). Get the output in your language
    @Expose
    private String icon; // Weather icon id

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}