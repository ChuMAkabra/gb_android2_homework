package com.example.dzchumanov05.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Wind  implements Serializable {
    @Expose
    private float speed;
    @Expose
    private int deg;

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
