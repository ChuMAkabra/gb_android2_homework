package com.example.dzchumanov05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class GetUrlData {

    String getData(String apiCall, Class<? extends Object> objClass) {
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
            return getLines(inReader);
        } catch (IOException e) {
            // TODO: возможно имеет смысл обработать разные ошибки отдельно
            //  (MalformedURLException, ProtocolException, IOException), но для отладки (не для пользователя)
            e.printStackTrace();
            // TODO: заменить тост на диалоговое окно (и описать это действие в одном месте)
//            handler.post(() -> Toast.makeText(context, "Something went wrong...!", Toast.LENGTH_LONG).show());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private static String getLines(BufferedReader in) {
        // TODO: подробно изучить эту строку
        return in.lines().collect(Collectors.joining("\n"));
    }

}
