package com.example.weatherapp;

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
    String oldValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWeather = findViewById(R.id.getWButton); // Кнопка получения погоды
        weatherText = findViewById(R.id.weather); // Текст, который изменяется для вывода погоды
        inputText = findViewById(R.id.editcity); // Текст из поля

        final String TOKEN_API = getString(R.string.token); // токен API openweathermap.org

        Runnable json_parse = new Runnable() {
            @Override
            public void run() {
                try {
                    String inputString = inputText.getText().toString();

                    if (inputString.equals(oldValue)) { // против клацания
                        return;
                    }

                    String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", inputString, TOKEN_API); // в аргументах указываем введенный город и токен
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
                    getFields fields = gson.fromJson(String.valueOf(response), getFields.class);
                    System.out.println(response);
                    Float temp = Float.valueOf(fields.main.getTemp()) - 273.15f; // Кельвины -> Цельсии

                    if (inputString.isEmpty()) {
                        new sendMessage("Введи город!");
                        // sleep(100);
                    } else {
                        String cloudsText = "";
                        int clouds = Integer.valueOf(fields.clouds.getClouds());
                        if (clouds < 10) {
                            cloudsText = "Безоблачно";
                        } else if (clouds < 40) {
                            cloudsText = "Слабая облачность";
                        } else if (clouds < 70) {
                            cloudsText = "Средняя облачность";
                        } else if (clouds > 70) {
                            cloudsText = "Пасмурно";
                        }
                        new sendMessage("Погода в городе " + inputString + " " + String.format("%.0f", temp) + "°C" + "\nВлажность воздуха: " + fields.main.getHumidity() + "%\n" + cloudsText);
                        oldValue = inputString;
                    }
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                    new sendMessage("Введенный город возможно неверный!");
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

    public class getFields {
        private fields main;
        private fields clouds;
    }

    public class fields {
        private String temp; // Температура воздуха
        private String humidity; // Влажность
        private String all; // Облачность

        private String getTemp() {
            return temp;
        }

        public String getHumidity() {
            return humidity;
        }

        private String getClouds() {
            return all;
        }
    }

    public class sendMessage {
        private String text;
        sendMessage(String text) {
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

