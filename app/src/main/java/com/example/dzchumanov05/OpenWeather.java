package com.example.dzchumanov05;

import com.example.dzchumanov05.model.WeatherOneCall;
import com.example.dzchumanov05.model.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//    https://api.openweathermap.org/...";
public interface OpenWeather {
//%s/weather?q=%s&units=metric&appid=%s", WEATHER_URL_DOMAIN, cityName, BuildConfig.WEATHER_API_KEY);
    @GET("data/2.5/weather?units=metric")
    Call<WeatherRequest> loadCurrentWeather(
            @Query("q") String cityName,
            @Query("appid") String apiKey);
            //TODO: добавить функцию переключения с Цельсиев на Фаренгейты
//            @Query("units") String units)


//"%s/onecall?lat=%s&lon=%s&units=metric&exclude=minutely,daily,alerts&appid=%s",
    @GET("data/2.5/onecall?exclude=minutely,daily,alerts")
    Call <WeatherOneCall> loadForecast(@Query("lat") float lat,
                                       @Query("lon") float lon,
                                       @Query("units") String units,
                                       @Query("appid") String appid);
}