package com.example.dzchumanov05;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActivityMain extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // устанавливаем тулбар в Action Bar
        Toolbar toolbar = initToolbar();
        // создаем кнопку-"сэндвич", открывающую боковую панель (навигационное меню)
        initDrawerToggle(toolbar);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawerToggle(Toolbar toolbar) {
        // не отображаем название активити в тулбаре
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_container);
        // создаем слушатель, который будет обрабатывать открытие и закрытие навигационного меню
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // добавляем его к навигационному меню
        drawer.addDrawerListener(toggle);
        // синхронизируем их
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
