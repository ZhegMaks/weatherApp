package com.example.weatherapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.browser.trusted.Token;
import android.widget.EditText;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.attribute.AclEntryType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetWeather{
    public static GetWeather instance;

    public GetWeather() {

    }

    public HashMap<String, String> WeatherFromApi(String url) {
        HashMap<String, String> results = new HashMap<>();
        try {
            StringBuffer response = new StringBuffer();

            Gson gson = new Gson();
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            getFields fields = gson.fromJson(String.valueOf(response), getFields.class);
            float temps = Float.valueOf(fields.main.getTemp()) - Float.valueOf(273.15f); // -> перевод в цельсии
            // положим в наши хэши температуру, влажность, облачность, осадки
            results.put("temp", String.valueOf(temps)); // температура
            results.put("humidity", fields.main.getHumidity()); // влажность
            results.put("all", fields.main.getHumidity()); // облачность
            results.put("description", fields.weather[0].getPrecipitation()); // осадки
            return results;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    // получение основных полей из API
    public class getFields {
        private fields main;
        private fields clouds;
        private fields[] weather;
    }

    public class fields {
        private String temp; // Температура воздуха
        private String humidity; // Влажность
        private String all; // Облачность
        private String description; // осадки

        private String getTemp() {
            return temp;
        }

        private String getHumidity() {
            return humidity;
        }

        private String getClouds() {
            return all;
        }

        private String getPrecipitation() { return description; }
    }

    public static GetWeather getInstance(){
        if(instance == null){		//если объект еще не создан
            instance = new GetWeather();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }
}
