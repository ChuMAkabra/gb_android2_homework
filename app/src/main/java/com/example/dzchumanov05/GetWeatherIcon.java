package com.example.dzchumanov05;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GetWeatherIcon {
    private static final String WEATHER_ICON_PATH  = "https://openweathermap.org/img/wn/";

    static void loadIconIntoImageView(String image, ImageView viewId) {
        Uri imageUri = getIconUrl(image);
        // скачаем иконку и положим ее в указанный ImageView с помощью библиотеки Picasso
        // (изображения кэшируются, поэтому приложение будет работать значительно быстрее)
        Picasso.get().load(imageUri).into(viewId);
    }
    static Uri getIconUrl(String iconName) {
        return Uri.parse(String.format("%s%s@2x.png", WEATHER_ICON_PATH, iconName));
    }
}