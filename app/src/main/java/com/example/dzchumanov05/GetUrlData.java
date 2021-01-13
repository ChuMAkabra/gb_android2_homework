package com.example.dzchumanov05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class GetUrlData {

//    Bitmap getBitmap(String apiCall) {
//        HttpsURLConnection urlConnection = null;
//        InputStream in;
//        Bitmap image = null;
//        try {
//            urlConnection = getConnection(apiCall);
//            in = urlConnection.getInputStream();
//            image = BitmapFactory.decodeStream(in);
//        } catch (IOException e) {
//            // TODO: возможно имеет смысл обработать разные ошибки отдельно
//            //  (MalformedURLException, ProtocolException, IOException), но для отладки (не для пользователя)
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
//        return image;
//    }

    String getData(String apiCall) {
        HttpsURLConnection urlConnection = null;
        InputStream in;
        BufferedReader inReader = null;
        String lines = null;
        try {
            urlConnection = getConnection(apiCall);
            in = urlConnection.getInputStream();
            inReader = new BufferedReader(new InputStreamReader(in));
            lines = getLines(inReader);
        } catch (IOException e) {
            // TODO: возможно имеет смысл обработать разные ошибки отдельно
            //  (MalformedURLException, ProtocolException, IOException), но для отладки (не для пользователя)
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return lines;
    }

    HttpsURLConnection getConnection(String apiCall) throws IOException {
        HttpsURLConnection urlConnection = null;
        URL url = new URL(apiCall);
        // 1) Открываем соединение
        urlConnection = (HttpsURLConnection) url.openConnection();
        // 2) Подготоваливаем запрос
        // устанавливаем метод протокола - GET (получение данных)
        urlConnection.setRequestMethod("GET");
        // устанавливаем таймаут (ожидание не больше 10 сек)
        urlConnection.setReadTimeout(10000);
        // 3) Читаем данные в поток
        return urlConnection; //.getInputStream();
    }

    private static String getLines(BufferedReader in) {
        // TODO: подробно изучить эту строку
        return in.lines().collect(Collectors.joining("\n"));
    }
}
