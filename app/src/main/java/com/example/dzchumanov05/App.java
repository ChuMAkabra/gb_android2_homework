package com.example.dzchumanov05;

import android.app.Application;

import androidx.room.Room;

import com.example.dzchumanov05.db.HistoryDao;
import com.example.dzchumanov05.db.HistoryDatabase;

public class App extends Application {
    App instance;
    HistoryDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        // Сохраняем объект приложения (для Singleton’а)
        instance = this;
        // Строим базу
        db = Room
                .databaseBuilder(instance, HistoryDatabase.class, "history_database")
                .allowMainThreadQueries() // Только для примеров и тестирования.
                .build();
    }

    public HistoryDao getHistoryDao() {
        return db.getHistoryDao();
    }

    // Получаем объект приложения
    public App getInstance() {
        return instance;
    }
}
