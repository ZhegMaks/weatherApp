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
        Runnable json_parse = new Runnable() {
            @Override
            public void run() {
                try {
                    String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&lang=ru", inputText.getText().toString(), TOKEN_API); // в аргументах указываем введенный город и токен
                    String inputString = inputText.getText().toString();

                    HashMap<String, String> getData = GetWeather.getInstance().WeatherFromApi(url);
                    Float getCloudiness = Float.valueOf(getData.get("all"));
                    Float getTemp = Float.valueOf(getData.get("temp"));
                    String getDescription = String.valueOf(getData.get("description"));
                    Integer getHumidity = Integer.valueOf(getData.get("humidity"));
                    System.out.println(getTemp);

                    if (inputString.equals(oldValue)) {
                        return;
                    }
                    if (inputString.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GetWeather.getInstance().resultLabel(weatherText ,"Введи город!");
                            }
                        });

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
                        String finalCloudsText = cloudsText;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GetWeather.getInstance().resultLabel(weatherText, String.format("Погода в городе %s\n%.0f °C\nВлажность воздуха %d\n%s\nОсадки: %s", inputString, getTemp, getHumidity, finalCloudsText, getDescription));
                            }
                        });

                        oldValue = inputString;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GetWeather.getInstance().resultLabel(weatherText, "Введенный город возможно неверный!");
                        }});
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
}

