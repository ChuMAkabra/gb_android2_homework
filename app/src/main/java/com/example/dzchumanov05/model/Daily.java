package com.example.dzchumanov05.model;

public class Daily {
    private long dt; // Time of the forecasted data, Unix, UTC
//    sunrise Sunrise time, Unix, UTC
//    sunset Sunset time, Unix, UTC
    private float temp; // Units – default: kelvin, metric: Celsius, imperial: Fahrenheit
//    temp.morn Morning temperature.
//    temp.day Day temperature.
//    temp.eve Evening temperature.
//    temp.night Night temperature.
//    temp.min Min daily temperature.
//    temp.max Max daily temperature.
//    feels_like This accounts for the human perception of weather. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit. How to change units used
//    feels_like.morn Morning temperature.
//    feels_like.day Day temperature.
//    feels_like.eve Evening temperature.
//    feels_like.night Night temperature.
//    pressure Atmospheric pressure on the sea level, hPa
//    humidity Humidity, %
//    dew_point Atmospheric temperature (varying according to pressure and humidity) below which water droplets begin to condense and dew can form. Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
//    wind_speed Wind speed. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour. How to change units used
//wind_gust (where available) Wind gust. Units – default: metre/sec, metric: metre/sec, imperial: miles/hour. How to change units used
//    wind_deg Wind direction, degrees (meteorological)
//    clouds Cloudiness, %
//    uvi Midday UV index
//    visibility Average visibility, metres
//    pop Probability of precipitation
//rain (where available) Precipitation volume, mm
//snow (where available) Snow volume, mm
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
