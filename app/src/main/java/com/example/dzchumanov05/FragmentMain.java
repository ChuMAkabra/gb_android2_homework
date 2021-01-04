package com.example.dzchumanov05;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dzchumanov05.model.Hourly;
import com.example.dzchumanov05.model.WeatherOneCall;
import com.example.dzchumanov05.model.WeatherRequest;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class FragmentMain extends AbstractFragment {
    static final String CITY = "CITY";
    static final String CURRENT_WEATHER = "CURRENT_WEATHER";
    private Context application;
    private Context context;

    public static final String YANDEX_POGODA_LINK = "https://yandex.ru/pogoda/";
    private WeatherRequest curWeather;
    private String curCity;
    private String curTemp;
    private String curIcon;
    private Uri curLink;
    private Hourly[] hourly;
    private long timezoneOffset;
//    private List<Bitmap> images;
    private List<String> times;
    private List<String> temps;

    TextView temp;
    ImageView sky;
    TextView details;
    TextView name;
    private List<String> imageNamesAll;

    public static FragmentMain create(String cityName, WeatherRequest weatherRequest){
        FragmentMain fragment = new FragmentMain();
        // записываем имя города и текущую погоду как аргументы фрагмента
        Bundle args = new Bundle();
        args.putString(CITY, cityName);
        args.putSerializable(CURRENT_WEATHER, weatherRequest);
        fragment.setArguments(args);

        return fragment;
    }

    static WeatherRequest getCurrentWeather(String cityName) {
        // запрос 1: через Current Weather Api получить координаты, текущие температуру и иконку погоды выбранного города
        // TODO: заменить на Retrofit
        return (WeatherRequest) GetWeatherData.getData(cityName);
    }

    public String getCurCity() {
        return (getArguments() != null) ? getArguments().getString(CITY) : null;
    }

    public WeatherRequest getCurWeather() {
        return (getArguments() != null) ? (WeatherRequest)getArguments().getSerializable(CURRENT_WEATHER) : null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        application = inflater.getContext();
        context = getContext();
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // получим данные с сервера
        if ((curCity = getCurCity()) != null) downloadData(curCity);
        // выведем данные в элементы фрагмента (повторно запросим название города - оно могло стать null)
        if ((curCity = getCurCity()) != null) outputData((ConstraintLayout) view, curCity);
        // создадим и установим Recycler View для прогноза погоды
        initRecyclerView(view);
    }

    private void downloadData(String curCity) {
        curWeather = getCurWeather();
        if (curWeather != null) {
            float lat = curWeather.getCoord().getLat();
            float lon = curWeather.getCoord().getLon();
            curTemp = floatTempToString(curWeather.getMain().getTemp());
            curIcon = curWeather.getWeather()[0].getIcon();
            curLink = generateLink(curCity);

            // запрос 2: через One Call Api по координатам получить почасовой прогноз погоды на 2 дня вперед и имена иконок погоды
            // TODO: заменить на Retrofit
            getForecast(lat, lon);
            if(hourly != null) {
//            WeatherOneCall weatherOneCall = (WeatherOneCall) GetWeatherData.getData(lat, lon);
//            if (weatherOneCall != null) {
//                hourly = weatherOneCall.getHourly();
//                timezoneOffset = weatherOneCall.getTimezone_offset();

                // запрос 3: по именам иконок загрузить их изображения с сервера
                // получаем лист имен всех иконок
                imageNamesAll = new ArrayList<>();
                for (Hourly hour : hourly) {
                    imageNamesAll.add(hour.getWeather()[0].getIcon());
                }
            } else {
//                ActivityMain.showAlertDialog(getContext(), R.string.not_found_title, R.string.city_not_found_msg, 0, true);
//                // обнулим записанное ранее в аргументы фрагмента название города
//                // (это укажет на отсутствие прогноза для города в базе OneCall)
//                if (this.getArguments() != null) {
//                    this.getArguments().putString(CITY, null);
//                }
            }
        }
        else {
//            ActivityMain.showAlertDialog(getContext(), R.string.not_found_title, R.string.forecast_not_found_msg, 0, true);
//            // обнулим записанное ранее в аргументы фрагмента название города
//            // (это укажет на отсутствие города в базе - вероятно допущена опечатка)
//            if (this.getArguments() != null) {
//                this.getArguments().putString(CITY, null);
//            }
        }
    }

    private void getForecast(float lat, float lon) {
        Thread thread = new Thread(() -> {
        try {
            Response<WeatherOneCall> response = ((ActivityMain)getActivity()).openWeather.loadForecast(lat, lon, "metric", BuildConfig.WEATHER_API_KEY).execute();
            if (response != null) {
                WeatherOneCall weatherOneCall = response.body();
                hourly =  weatherOneCall.getHourly();
                timezoneOffset =  weatherOneCall.getTimezone_offset();
            }
        } catch (IOException e) {
            e.printStackTrace();
            ActivityMain.showAlertDialog(getContext(), R.string.not_found_title, R.string.city_not_found_msg, 0, true);
            // обнулим записанное ранее в аргументы фрагмента название города
            // (это укажет на отсутствие прогноза для города в базе OneCall)
            if (getArguments() != null) {
                getArguments().putString(CITY, null);
            }
        }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        openWeather.loadForecast(lat, lon, "metric", BuildConfig.WEATHER_API_KEY).enqueue(new Callback<WeatherOneCall>() {
//            @Override
//            public void onResponse(Call<WeatherOneCall> call, Response<WeatherOneCall> response) {
//                if(response.body() != null) {
//                    hourly = response.body().getHourly();
//                    timezoneOffset = response.body().getTimezone_offset();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherOneCall> call, Throwable t) {
//                //TODO: запихнуть сюда алерт диалог!
//                ActivityMain.showAlertDialog(getContext(), R.string.not_found_title, R.string.city_not_found_msg, 0, true);
//                // обнулим записанное ранее в аргументы фрагмента название города
//                // (это укажет на отсутствие прогноза для города в базе OneCall)
//                if (getArguments() != null) {
//                    getArguments().putString(CITY, null);
//                }
//            }
//        });
    }


    private Uri generateLink(String city) {
        // TODO: проверить работоспособность ссылки перед генерацией
        return Uri.parse(YANDEX_POGODA_LINK + city);
    }

    private String floatTempToString(float temp) {
        return String.format("%d°C", Math.round(temp));
    }
    private String timeToString(Time t3) {
        // не отображаем секунды
        String[] strSplit = t3.toString().split(":");
        return String.format("%s:%s", strSplit[0], strSplit[1]);
    }

    private void outputData(@NonNull ConstraintLayout view, String curCity) {
        // получим ссылки на элементы фрагмента
        temp = view.findViewById(R.id.tvTemp);
        sky = view.findViewById(R.id.ivIcon);
        details = view.findViewById(R.id.tvDetails);
        name = view.findViewById(R.id.tvCity);

        // выведем на экран полученные данные о текущем дне
        temp.setText(curTemp);
        // TODO: на том языке, на котором пользователь ввел в поисковике (исправить?)
        name.setText(curCity);
        if(curLink != null)
        {
            details.setVisibility(View.VISIBLE);
            details.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, curLink);
                startActivity(intent);
            });
        }
        // установим текущую картинку
//        sky.setImageBitmap(curIcon);
        GetWeatherData.loadIconIntoImageView(curIcon, sky);

        // заполним данные времени и температуры
        times = new ArrayList<>();
        temps = new ArrayList<>();
        for (Hourly h : hourly) {
            // в API время в секундах, а Time() требует милисекунды
            Time time = new Time((h.getDt() + timezoneOffset) * 1000);
            times.add(timeToString(time));
            temps.add(floatTempToString(h.getTemp()));
        }
    }

    private void initRecyclerView(View view) {
        // получаем RV
        RecyclerView rvForecast = view.findViewById(R.id.rvForecast);
        // Эта установка служит для повышения производительности системы
        // (все элементы будут иметь одинаковый размер, и не надо его пересчитывать)
        rvForecast.setHasFixedSize(true);
        // добавляем к RV менеджер макетов
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvForecast.setLayoutManager(linearLayout);
        // добавляем к RV докоратор в виде сепаратора
        DividerItemDecoration divider = new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(application, R.drawable.separator)));
        rvForecast.addItemDecoration(divider);
        // добавляем к RV адаптер
        AdapterWeather adapter = null;
        if(times != null)  adapter = new AdapterWeather(times, imageNamesAll, temps);
        rvForecast.setAdapter(adapter);
    }
}
