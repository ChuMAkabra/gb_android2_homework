package com.example.dzchumanov05;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dzchumanov05.model.WeatherRequest;
import com.google.android.material.navigation.NavigationView;

public class ActivityMain extends AbstractActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navView;
    private Toolbar toolbar;
    String lastCityName;
    private SharedPreferences sp;
    private final String PREVIOUS_ORIENTATION = "PREVIOUS_ORIENTATION";

    private WeatherRequest weatherRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // устанавливаем тулбар в Action Bar
        toolbar = initToolbar();
        // настраиваем боковую панель (навигационное меню)
        initDrawer(toolbar);
        // сохраняем ссылку на SharedPreferences
        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        //TODO: при первом запуске по геолокации определить город.
        // Если нет доступа, вывести последний город из истории.
        // Если история пуста - предложить пользователю воспользоваться поиском
        // Возможно вывести инструкцию во всплывающем окне

        // при первом запуске показать погоду для последнего города из истории поиска
        if(savedInstanceState == null) {
            lastCityName = getSPCityName();
            createFragment(lastCityName);
        }
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        // не отображаем название активити в тулбаре
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        drawer = findViewById(R.id.drawer_container);
        // создаем слушатель, который будет обрабатывать открытие и закрытие навигационного меню
        // (это и есть та кнопка-"сэндвич")
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // добавляем его к навигационному меню
        drawer.addDrawerListener(toggle);
        // синхронизируем их
        toggle.syncState();
        // регистрируем слушателя, умеющего обрабатывать пункты навигационного меню
        // (им будет наша активити, поскольку она реализует нужный интерфейс)
        navView = drawer.findViewById(R.id.navigation_bar);
        navView.setNavigationItemSelectedListener(this);
        // TODO: менять сэндвич на стрелочку, когда находимся не на главной странице
    }

    private String getSPCityName() {
        // TODO: вместо Москвы определять город по локации
        // если приложение запускается впервые после установки, город - Москва
        return sp.getString(SP_LAST_CITY, "Moscow");
    }

    // создадим фрагмент, если указанный город есть в базе.
    private void createFragment(String cityQuery) {
        // правим вводимое название города (удаляем лишние пробелы, заменяем
        // дефисы на пробелы и добавляем заглавные буквы к каждой части названия)
        String cityNameRes = prepareCityName(cityQuery);

        // получим данные о текущей погоде, если указанный город есть в базе
        Thread thread = new Thread(() -> {
           weatherRequest = FragmentMain.getCurrentWeather(cityNameRes);
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // если данные найдены, выведем их в новый фрагмент
        if (weatherRequest != null) {
            FragmentMain fragmentMain = FragmentMain.create(cityNameRes, weatherRequest);
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main, fragmentMain, null)
                .commit();

            // перезапишем историю поиска
            if (fragmentMain.getArguments() != null) {
                lastCityName = fragmentMain.getArguments().getString(FragmentMain.CITY);
                setSPCityName(lastCityName);
            }
        }
        else {
            // иначе выведем диалоговое окно с причиной ошибки
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.not_found_title)
                    .setMessage(R.string.city_not_found_msg)
                    .setCancelable(true)
                    .setIcon(R.mipmap.ic_launcher_round)
            ;
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    private void setSPCityName(String cityName) {
        sp.edit()
                .putString(SP_LAST_CITY, cityName)
                .apply();
    }
    static String prepareCityName(String cityName) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // реализуем функцию поиска города и вывода его данных во фрагмент
        MenuItem searchMenu =  menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                createFragment(query);
                // TODO: спрятать Search ActionView? Или пусть пользователь и дальше вводит?
                /** как программно установить фокус на элементе навигационного меню? */
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // TODO: отказаться от опций тулбара в пользу нижнего навигационного меню
            case R.id.about:
                getSupportFragmentManager()
                        .beginTransaction()
//                        .addToBackStack(null)
                        .replace(R.id.fragment_main, new FragmentAbout(), null)
                        .commit();
                return true;
            case R.id.tools:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_main, new FragmentTools(), null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // TODO: добавлять в стек только главную страницу
            case R.id.home:
                // открыть фрагмент с данными о городе из последнего поиска (или Мск по умолчанию)
                lastCityName = getSPCityName();
                createFragment(lastCityName);
                break;
            case R.id.tools:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_main, new FragmentTools(), null)
                        .commit();
                break;
            case R.id.contact:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:dmitry.chumanov@yandex.ru"));
                startActivity(intent);
                break;
            case R.id.about:
                // TODO: выводить фрагмент в диалоговое окно
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_main, new FragmentAbout(), null)
                        .commit();
                break;
            default:
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        // если боковая панель открыта, при нажатии кнопки "Назад" она закроется
        if(drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PREVIOUS_ORIENTATION, getResources().getConfiguration().orientation);
    }
}
