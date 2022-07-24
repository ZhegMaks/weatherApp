package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Looper;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button getWeather;
    TextView weatherText;
    EditText inputText;
    String oldValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWeather = findViewById(R.id.getWButton); // Кнопка получения погоды
        weatherText = findViewById(R.id.weather); // Текст, который изменяется для вывода погоды
        inputText = findViewById(R.id.editcity); // Текст из поля
        String TOKEN_API = getString(R.string.token); // токен API openweathermap.org
        GetWeather GetWeather = new GetWeather();
        Runnable json_parse = new Runnable() {
            @Override
            public void run() {
                try {
                    String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&lang=ru", inputText.getText().toString(), TOKEN_API); // в аргументах указываем введенный город и токен
                    String inputString = inputText.getText().toString();

                    HashMap<String, String> getData = GetWeather.WeatherFromApi(url);
                    Float getCloudiness = Float.valueOf(getData.get("all"));
                    Float getTemp = Float.valueOf(getData.get("temp"));
                    String getDescription = String.valueOf(getData.get("description"));
                    Integer getHumidity = Integer.valueOf(getData.get("humidity"));

                    if (inputString.equals(oldValue)) {
                        return;
                    }
                    if (inputString.isEmpty()) {
                        new resultLabel("Введи город!");
                        // sleep(100);
                    } else {
                        String cloudsText = "";

                        if (getCloudiness <= 15) {
                            cloudsText = "Безоблачно";
                        } else if (getCloudiness < 40) {
                            cloudsText = "Переменная облачность";
                        } else if (getCloudiness < 70) {
                            cloudsText = "Средняя облачность";
                        } else if (getCloudiness > 70) {
                            cloudsText = "Пасмурно";
                        }
                        new resultLabel("Погода в городе " +
                                inputString + " " +
                                String.format("%.0f", getTemp) + "°C" +
                                "\nВлажность воздуха: " + getHumidity +
                                "%\n" + cloudsText +
                                "\nОсадки: " + getDescription);
                        oldValue = inputString;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    new resultLabel("Введенный город возможно неверный!");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    new resultLabel("number " + e);
                }
            }
        };

        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread t1 = new Thread(json_parse);
                    t1.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Изменения текста с результатами погоды
    public class resultLabel {
        private String text;
        resultLabel(String text) {
            this.text = text;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weatherText.setText(text);
                }
            });
        }
    }
}

