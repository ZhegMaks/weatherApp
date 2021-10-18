package com.example.weatherapp;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button getWeather;
    TextView weatherText;
    EditText inputText;
    final String TOKEN = ""; // токен API openweathermap.org

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWeather = findViewById(R.id.getWButton); // Кнопка получения погоды
        weatherText = findViewById(R.id.weather); // Текст, который изменяется для вывода погоды
        inputText = findViewById(R.id.editcity); // Текст из поля

        Runnable json_parse = new Runnable() {
            @Override
            public void run() {
                try {
                    String inputString = inputText.getText().toString();
                    String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", inputString, TOKEN); // в аргументах указываем введенный город и токен
                    URL obj = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                    connection.setRequestMethod("POST");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Gson gson = new Gson();
                    customInformer custom = gson.fromJson(String.valueOf(response), customInformer.class);
                    System.out.println(response);
                    Float temp = Float.valueOf(custom.main.temp) - 273.15f; // Кельвины -> Цельсии
                    if (inputString.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                weatherText.setText("Введи город!");
                            }
                        });
                        // sleep(100);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                weatherText.setText("Погода в городе " + inputString + " " + String.format("%.0f", temp) + "°C");
                            }
                        });
                    }
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherText.setText("Возможно, введенный город неверный");
                        }
                    });
                }
            }
        };
        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t1 = new Thread(json_parse);
                t1.start();
            }
        });
    }
    public class customInformer {
        public A main;

    }
    public class A {
        public String temp;
    }

}
