package com.example.dzchumanov05;

import com.google.gson.Gson;

public class GetWeatherData {

    static Object getObjectFromGson(String apiCall, Class<? extends Object> objClass) {

        // читаем данные из строки запроса и сохраняем их в виде строковой переменной
        GetUrlData urlData = new GetUrlData();
        final String strData = urlData.getData(apiCall, objClass);

        // преобразуем данные запроса в модель посредством библиотеки Gson
        Gson gson = new Gson();
        return gson.fromJson(strData, objClass);
    }
}
