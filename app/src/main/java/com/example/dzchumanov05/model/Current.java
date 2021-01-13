package com.example.dzchumanov05.model;

import com.google.gson.annotations.Expose;

public class Current {
    @Expose
    private long dt; // Current time, Unix, UTC
//    sunrise Sunrise time, Unix, UTC
//    sunset Sunset time, Unix, UTC
    @Expose
    private float temp; // Temperature. Units - default: kelvin, metric: Celsius, imperial: Fahrenheit
//    feels_like Temperature. This temperature parameter accounts for the human perception of weather. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
//    pressure Atmospheric pressure on the sea level, hPa
//    humidity Humidity, %
//    dew_point AAtmospheric temperature (varying according to pressure and humidity) below which water droplets begin to condense and dew can form. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
//    clouds Cloudiness, %
//    uvi Midday UV index
//    visibility Average visibility, metres
//    wind_speed Wind speed. Wind speed. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour. How to change units used
//    wind_gust (where available) Wind gust. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour. How to change units used
//    wind_deg Wind direction, degrees (meteorological)
//    rain
//    rain.1h (where available) Rain volume for last hour, mm
//    snow
//    snow.1h (where available) Snow volume for last hour, mm
    @Expose
    private Weather[] weather;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }
}
