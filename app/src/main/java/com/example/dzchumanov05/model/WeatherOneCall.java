package com.example.dzchumanov05.model;

import com.google.gson.annotations.Expose;

public class WeatherOneCall {
    @Expose
    private float lat; // Geographical coordinates of the location (latitude)
    @Expose
    private float lon; // Geographical coordinates of the location (longitude)
//    timezone Timezone name for the requested location
    @Expose
    private long timezone_offset; // Shift in seconds from UTC
    @Expose
    private Current current; // Current weather data API response

//    minutely Minute forecast weather data API response
//        minutely.dt Time of the forecasted data, unix, UTC
//        minutely.precipitation Precipitation volume, mm
    @Expose
    private Hourly[] hourly; // Hourly forecast weather data API response
    @Expose
    private Daily[] daily; // Daily forecast weather data API response

//    private Alerts alerts; // Government weather alerts data from major national weather warning systems
//    alerts.sender_name Name of the alert source
//    alerts.event Alert event name
//    alerts.start Date and time of the start of the alert, Unix, UTC
//    alerts.end Date and time of the end of the alert, Unix, UTC
//    alerts.description Description of the alert

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Hourly[] getHourly() {
        return hourly;
    }

    public void setHourly(Hourly[] hourly) {
        this.hourly = hourly;
    }

    public Daily[] getDaily() {
        return daily;
    }

    public void setDaily(Daily[] daily) {
        this.daily = daily;
    }

    public long getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(long timezone_offset) {
        this.timezone_offset = timezone_offset;
    }
}
