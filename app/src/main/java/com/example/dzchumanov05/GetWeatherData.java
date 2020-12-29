package com.example.dzchumanov05;

import android.graphics.Bitmap;

import com.example.dzchumanov05.model.WeatherOneCall;
import com.example.dzchumanov05.model.WeatherRequest;
import com.google.gson.Gson;

public class GetWeatherData {
    private static final String WEATHER_URL_DOMAIN = "https://api.openweathermap.org/data/2.5";
    private static final String WEATHER_ICON_PATH  = "https://openweathermap.org/img/wn/";

    static Bitmap getBitmap(String imageName) {
        String apiCall = String.format("%s%s@2x.png", WEATHER_ICON_PATH, imageName);
        return new GetUrlData().getBitmap(apiCall);
    }

    static Object getData(String cityName) {
        String apiCall = String.format("%s/weather?q=%s&units=metric&appid=%s",
                WEATHER_URL_DOMAIN, cityName, BuildConfig.WEATHER_API_KEY);
        return getObjectFromGson(WeatherRequest.class, apiCall);
    }

    static Object getData(float lat, float lon) {
        String apiCall = String.format("%s/onecall?lat=%s&lon=%s&units=metric&exclude=minutely,daily,alerts&appid=%s",
                WEATHER_URL_DOMAIN, lat, lon, BuildConfig.WEATHER_API_KEY);
        return getObjectFromGson(WeatherOneCall.class, apiCall);
    }

    private static Object getObjectFromGson(Class<? extends Object> objClass, String apiCall) {
        // читаем данные из строки запроса и сохраняем их в виде строковой переменной
        GetUrlData urlData = new GetUrlData();
        final String strData = urlData.getData(apiCall);
        // преобразуем данные запроса в модель посредством библиотеки Gson
        Gson gson = new Gson();
        return gson.fromJson(strData, objClass);
    }
}