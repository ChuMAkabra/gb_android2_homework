package com.example.dzchumanov05;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navView;
    private Toolbar toolbar;
    String lastCityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // устанавливаем тулбар в Action Bar
        toolbar = initToolbar();
        // настраиваем боковую панель (навигационное меню)
        initDrawer(toolbar);

        //TODO: при первом запуске по геолокации определить город.
        // Если нет доступа, вывести последний город из истории.
        // Если история пуста - предложить пользователю воспользоваться поиском
        // Возможно вывести инструкцию во всплывающем окне

        // пока что просто добавим пустой основной фрагмент
        // TODO: вынести работу с getSupportFragmentManager в отдельный метод
        FragmentMain fragmentMain = FragmentMain.create("Moscow");
        getSupportFragmentManager()
                .beginTransaction()
                // TODO: вместо Москвы определять город по локации
                .replace(R.id.fragment_main, fragmentMain, null)
                .commit();
        if (fragmentMain.getArguments() != null) {
            lastCityName = fragmentMain.getArguments().getString(FragmentMain.CITY);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // TODO: реализовать поиск
        MenuItem searchMenu =  menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentMain fragmentMain = FragmentMain.create(query);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_main,  fragmentMain, null)
                        .commit();

                if (fragmentMain.getArguments() != null) {
                    lastCityName = fragmentMain.getArguments().getString(FragmentMain.CITY);
                }

                // TODO: спрятать Search ActionView? Или пусть пользователь и дальше вводит?
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
                // TODO: открыть фрагмент с данными о городе из последнего поиска (или Мск по умолчанию)
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_main, FragmentMain.create(lastCityName), null)
                        .commit();
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
}
