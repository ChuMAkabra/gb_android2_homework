package com.example.dzchumanov05;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dzchumanov05.model.Hourly;
import com.example.dzchumanov05.model.WeatherOneCall;
import com.example.dzchumanov05.model.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class FragmentMain extends AbstractFragment {
    private static final String CITY = "CITY";
    private Context application;
    private Context context;
    private final Handler handler = new Handler(); // хендлер, указывающий на основной (UI) поток
    private final String WEATHER_URL_DOMAIN = "https://api.openweathermap.org/data/2.5";
    public static final String YANDEX_POGODA_LINK = "https://yandex.ru/pogoda/";
    private String curTemp;
    private Bitmap curIcon;
    private Uri curLink;
    private Hourly[] hourly;
    private long timezoneOffset;
    private List<Bitmap> images;
    private List<String> times;
    private List<String> temps;

    private RecyclerView rvForecast;
    TextView temp;
    ImageView sky;
    TextView details;
    TextView name;

    public static FragmentMain create(String cityName){
        FragmentMain fragment = new FragmentMain();
        // правим вводимое название города (удаляем лишние пробелы, заменяем
        // дефисы на пробелы и добавляем заглавные буквы к каждой части названия)
        String cityNameRes = prepareCityName(cityName);
        // записываем данные в качестве аргументов фрагмента
        Bundle args = new Bundle();
        args.putString(CITY, cityNameRes);
        fragment.setArguments(args);

        return fragment;
    }

    private static String prepareCityName(String cityName) {
        // разделим название города на части (если > 2 слов), удалив лишние пробелы
        String[] parts = cityName
                .replaceAll("\\s{2,}", " ")
                .trim()
                .split("[\\s|-]");

        // начнем название города с первого слова (с заглавной буквы)
        String cityNameRes = capitalize(parts[0]);
        // добавим через пробел другие части с большой буквы (если > 2 слов)
        if (parts.length > 1) {
            for (int i = 1; i < parts.length; i++) {
                parts[i] = capitalize(parts[i]);
                cityNameRes = String.format("%s %s", cityNameRes, parts[i]);
            }
        }
        return cityNameRes;
    }

    private static String capitalize(String word) {
        return String.format("%s%s", word.substring(0, 1).toUpperCase(), word.substring(1).toLowerCase());
    }

    private String getCity() {
        return (getArguments() != null) ? getArguments().getString(CITY) : null;
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

        final String curCity = getCity();
        if (curCity != null) {
            // получим данные с сервера
            downloadData(curCity);
            // выведем данные в элементы фрагмента
            outputData((ConstraintLayout) view, curCity);
        }
        // создадим и установим Recycler View для прогноза погоды
        // TODO: повторно по памяти реализовать RecyclerView
        addRecyclerView(view);
    }

    private void addRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvForecast);

        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayout);

        // TODO: разобраться, почему не добавляется сепаратор
        DividerItemDecoration divider = new DividerItemDecoration(context, LinearLayout.VERTICAL);
        divider.setDrawable(application.getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(divider);

        AdapterWeather adapter = new AdapterWeather(times, images, temps);
        recyclerView.setAdapter(adapter);

    }

    private void downloadData(String curCity) {
        Thread thread = new Thread(() -> {
            // запрос 1: через Current Weather Api получить координаты, текущие температуру и иконку погоды выбранного города
            String apiCall = String.format("%s/weather?q=%s&units=metric&appid=%s", WEATHER_URL_DOMAIN, curCity, BuildConfig.WEATHER_API_KEY);
            final WeatherRequest weatherRequest = (WeatherRequest) getObjectFromGson(apiCall, WeatherRequest.class);
            // TODO: возможно добавить здесь проверку на weatherRequest != null (возможно вывести диалоговое окно, что такого города не существует)
            float lat = weatherRequest.getCoord().getLat();
            float lon = weatherRequest.getCoord().getLon();
            curTemp = floatTempToString(weatherRequest.getMain().getTemp());
            curIcon = getBitmap(weatherRequest.getWeather()[0].getIcon());
            curLink = generateLink(curCity);

            // запрос 2: через One Call Api по координатам получить почасовой прогноз погоды на 2 дня вперед и имена иконок погоды
            String apiCall2 = String.format("%s/onecall?lat=%s&lon=%s&units=metric&exclude=minutely,daily,alerts&appid=%s",
                    WEATHER_URL_DOMAIN, Float.toString(lat), Float.toString(lon), BuildConfig.WEATHER_API_KEY);
            WeatherOneCall weatherOneCall = (WeatherOneCall) getObjectFromGson(apiCall2, WeatherOneCall.class);
            // TODO: возможно добавить здесь проверку на weatherOneCall != null (возможно вывести диалоговое окно, что данные по городу не найдены - хотя это странно)
            hourly = weatherOneCall.getHourly();
            timezoneOffset = weatherOneCall.getTimezone_offset();
            images = new ArrayList<>();

            // запрос 3: по именам иконок загрузить их изображения с сервера
            // получаем лист имен всех иконок
            List<String> imageNamesAll = new ArrayList<>();
            for (Hourly hour : hourly) {
                imageNamesAll.add(hour.getWeather()[0].getIcon());
            }
            // получаем набор имен уникальных иконок
            Set<String> imageNamesUnique = new HashSet<>(imageNamesAll);
            // получаем хэш-таблицу уникальных битмапов
            Map<String, Bitmap> imagesUnique = new HashMap<>(imageNamesUnique.size());
            // устанавливаем обратный отчет потоков
            CountDownLatch countDownLatch = new CountDownLatch(imageNamesUnique.size());
            // создаем динамический пул потоков (скачать картинки в оптимальном кол-ве потоков)
            ExecutorService executorService = Executors.newCachedThreadPool();
            // для каждой уникальной картинки:
            for (String iName : imageNamesUnique) {
                // качаем картинку в отдельном потоке
                executorService.execute(() -> {
                    Bitmap image = getBitmap(iName);
                    if (image != null) imagesUnique.put(iName,image);
                    countDownLatch.countDown();
                });
            }
            // дожидаемся завершения всех потоков
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // закрываем пул потоков
            executorService.shutdown();
            // получаем лист всех битмапов (в правильном порядке!)
            for (String name : imageNamesAll) {
                images.add(imagesUnique.get(name));
            }
        });
        thread.start();
        try {
            // вынудить главный поток ждать окончания выполнения данного потока
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Uri generateLink(String city) {
        // TODO: проверить работоспособность ссылки перед генерацией
        return Uri.parse(YANDEX_POGODA_LINK + city);
    }

    private Object getObjectFromGson(String apiCall, Class<? extends Object> objClass) {
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL(apiCall);
            // 1) Открываем соединение
            urlConnection = (HttpsURLConnection) url.openConnection();
            // 2) Подготоваливаем запрос
            // устанавливаем метод протокола - GET (получение данных)
            urlConnection.setRequestMethod("GET");
            // устанавливаем таймаут (ожидание не больше 10 сек)
            urlConnection.setReadTimeout(10000);
            // 3) Читаем данные в поток
            BufferedReader inReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            // сохраняем все данные в виде строки
            String strData = getLines(inReader);
            // преобразуем данные запроса в модель посредством библиотеки Gson
            Gson gson = new Gson();
            return gson.fromJson(strData, objClass);
        } catch (IOException e) {
            // TODO: возможно имеет смысл обработать разные ошибки отдельно
            //  (MalformedURLException, ProtocolException, IOException), но для отладки (не для пользователя)
            e.printStackTrace();
            // TODO: заменить тост на диалоговое окно (и описать это действие в одном месте)
            handler.post(() -> Toast.makeText(this.getContext(), "Something went wrong...!", Toast.LENGTH_LONG).show());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
    private String getLines(BufferedReader in) {
        // TODO: подробно изучить эту строку
        return in.lines().collect(Collectors.joining("\n"));
    }
    private Bitmap getBitmap(String imageName) {
        String apiCall = String.format("https://openweathermap.org/img/wn/%s@2x.png", imageName);

        HttpsURLConnection urlConnection = null;
        Bitmap image = null;
        try {
            URL url = new URL(apiCall);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);

            InputStream in = urlConnection.getInputStream();
            image = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: заменить тост на диалоговое окно (и описать это действие в одном месте)
            handler.post(() -> Toast.makeText(this.getContext(), "Something went wrong...!", Toast.LENGTH_LONG).show());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return image;
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
        rvForecast = view.findViewById(R.id.rvForecast);
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
        sky.setImageBitmap(curIcon);

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

}
