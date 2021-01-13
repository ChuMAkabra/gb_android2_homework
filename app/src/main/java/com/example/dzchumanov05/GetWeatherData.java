package com.example.dzchumanov05;

import android.net.Uri;
import android.widget.ImageView;

import com.example.dzchumanov05.model.WeatherOneCall;
import com.example.dzchumanov05.model.WeatherRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class GetWeatherData {
    private static final String WEATHER_URL_DOMAIN = "https://api.openweathermap.org/data/2.5";
    private static final String WEATHER_ICON_PATH  = "https://openweathermap.org/img/wn/";
    private static Object data;
    private static String strData;
//    private static Bitmap bitmap;

//    static Bitmap getBitmap(String imageName) {
//        String apiCall = String.format("%s%s@2x.png", WEATHER_ICON_PATH, imageName);
//        Thread thread = new Thread(()-> bitmap = new GetUrlData().getBitmap(apiCall));
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return bitmap ;
//    }

    static void loadIconIntoImageView(String image, ImageView viewId) {
        Uri imageUri = getIconUrl(image);
        // скачаем иконку и положим ее в указанный ImageView с помощью библиотеки Picasso
        // (изображения кэшируются, поэтому приложение будет работать значительно быстрее)
        Picasso.get().load(imageUri).into(viewId);
    }
    static Uri getIconUrl(String iconName) {
        return Uri.parse(String.format("%s%s@2x.png", WEATHER_ICON_PATH, iconName));
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
        Thread thread = new Thread(() -> {
            // читаем данные из строки запроса и сохраняем их в виде строковой переменной
            GetUrlData urlData = new GetUrlData();
            strData = urlData.getData(apiCall);
            // преобразуем данные запроса в модель посредством библиотеки Gson
            Gson gson = new Gson();
            data = gson.fromJson(strData, objClass);
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }
}