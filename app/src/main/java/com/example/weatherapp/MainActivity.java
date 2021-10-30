package com.example.weatherapp;

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;
=======
import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

>>>>>>> e2fd5e7f5c4dd82976a5c530666d6c0cbe8e6b9c
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
<<<<<<< HEAD
import com.google.gson.Gson;
=======

import com.google.gson.Gson;

>>>>>>> e2fd5e7f5c4dd82976a5c530666d6c0cbe8e6b9c
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button getWeather;
    TextView weatherText;
    EditText inputText;
<<<<<<< HEAD
    String oldValue;
=======
    final String TOKEN = ""; // токен API openweathermap.org
>>>>>>> e2fd5e7f5c4dd82976a5c530666d6c0cbe8e6b9c

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWeather = findViewById(R.id.getWButton); // Кнопка получения погоды
        weatherText = findViewById(R.id.weather); // Текст, который изменяется для вывода погоды
        inputText = findViewById(R.id.editcity); // Текст из поля

<<<<<<< HEAD
        final String TOKEN_API = getString(R.string.token); // токен API openweathermap.org

=======
>>>>>>> e2fd5e7f5c4dd82976a5c530666d6c0cbe8e6b9c
        Runnable json_parse = new Runnable() {
            @Override
            public void run() {
                try {
                    String inputString = inputText.getText().toString();
<<<<<<< HEAD

                    if(inputString.equals(oldValue)) { // против клацания
                        return;
                    }

                    String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", inputString, TOKEN_API); // в аргументах указываем введенный город и токен
=======
                    String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", inputString, TOKEN); // в аргументах указываем введенный город и токен
>>>>>>> e2fd5e7f5c4dd82976a5c530666d6c0cbe8e6b9c
                    URL obj = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                    connection.setRequestMethod("POST");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
<<<<<<< HEAD

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
                        if(clouds < 10) {
                            cloudsText = "Безоблачно";
                        }
                        else if(clouds < 40 ) {
                            cloudsText = "Слабая облачность";
                        }
                        else if(clouds < 70) {
                            cloudsText = "Средняя облачность";
                        }
                        else if(clouds > 70) {
                            cloudsText = "Пасмурно";
                        }
                        new sendMessage("Погода в городе " + inputString + " " + String.format("%.0f", temp) + "°C" + "\nВлажность воздуха: " + fields.main.getHumidity() + "%\n" + cloudsText);
                        oldValue = inputString;
                    }
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                    new sendMessage("Введи город!");
                }
            }
        };

=======
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
>>>>>>> e2fd5e7f5c4dd82976a5c530666d6c0cbe8e6b9c
        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t1 = new Thread(json_parse);
                t1.start();
            }
        });
    }
<<<<<<< HEAD

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
=======
    public class customInformer {
        public A main;

    }
    public class A {
        public String temp;
    }

>>>>>>> e2fd5e7f5c4dd82976a5c530666d6c0cbe8e6b9c
}
