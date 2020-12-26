package com.example.dzchumanov05;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Создал этот абстрактный класс для логирования методов обратного вызова
 * Наследуя его, можно избежать:
 * 1) однообразоного переопределения всех интересующих нас методов;
 * 2) ручного указания названия активити в первом аргументе Log.d().
 */

public abstract class AbstractActivity extends AppCompatActivity {
    protected String className = this.getClass().getName();
    protected final String SP_NAME = "WEATHER";
    protected final String SP_DARK_THEME = "IS_DARK_THEME";
    protected final String SP_LAST_CITY = "SP_LAST_CITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // установить тему в соответствии с SharedPreferences
        if(isDarkTheme())
            setTheme(R.style.AppMainDark_NoActionBar);
        else
            setTheme(R.style.AppMain_NoActionBar);

        Log.d(className, "onCreate()");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(className, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(className, "onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(className, "onRestoreInstanceState()");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(className, "onResume()");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(className, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(className, "onStop()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(className, "onSaveInstanceState()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(className, "onDestroy()");
    }

    public boolean isDarkTheme() {
        SharedPreferences sh = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        return sh.getBoolean(SP_DARK_THEME, true);
    };

    public void setDarkTheme(boolean isChecked){
        SharedPreferences sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if(isChecked) {
            sp.edit()
                    .putBoolean(SP_DARK_THEME, true)
                    .apply();
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            sp.edit()
                    .putBoolean(SP_DARK_THEME, false)
                    .apply();
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }
}