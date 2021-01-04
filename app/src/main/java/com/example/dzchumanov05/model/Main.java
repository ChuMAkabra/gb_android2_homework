package com.example.dzchumanov05.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Main  implements Serializable {
    @Expose
    private float temp;
    @Expose
    private int pressure;
    @Expose
    private int humidity;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
