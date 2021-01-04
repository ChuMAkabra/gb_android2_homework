package com.example.dzchumanov05.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Clouds implements Serializable {
    @Expose
    private int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
