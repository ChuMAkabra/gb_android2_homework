package com.example.dzchumanov05.model;

public class Hourly {
    private long dt; // Time of the forecasted data, Unix, UTC
    private float temp; // Temperature. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit. How to change units used
//    feels_like Temperature. This accounts for the human perception of weather. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
//    pressure Atmospheric pressure on the sea level, hPa
//    humidity Humidity, %
//    dew_point Atmospheric temperature (varying according to pressure and humidity) below which water droplets begin to condense and dew can form. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
//    clouds Cloudiness, %
//    visibility Average visibility, metres
//    wind_speed Wind speed. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour.How to change units used
//    wind_gust (where available) Wind gust. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour. How to change units used
//    cwind_deg Wind direction, degrees (meteorological)
//    pop Probability of precipitation
//    rain
//    rain.1h (where available) Rain volume for last hour, mm
//    snow
//    snow.1h (where available) Snow volume for last hour, mm
    private Weather[] weather;

    public long getDt() {
        return dt;
    }

    public void setDt(int dt) {
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
