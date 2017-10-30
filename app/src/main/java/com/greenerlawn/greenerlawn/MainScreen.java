package com.greenerlawn.greenerlawn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import org.w3c.dom.Text;


public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);

        OpenWeatherMapHelper helper = new OpenWeatherMapHelper();

        helper.setApiKey(getString(R.string.OPEN_WEATHER_MAP_API_KEY));

        helper.setUnits(Units.METRIC);

        helper.getCurrentWeatherByCityName("Calgary", new OpenWeatherMapHelper.CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                TextView tv = findViewById(R.id.textView2);
                tv.setText("" + currentWeather.getMain().getTemp()+ "°");
                Log.v("SUCCESS",
                        "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLat() +"\n"
                                +"Weather Description: " + currentWeather.getWeatherArray().get(0).getDescription() + "\n"
                                +"Max Temperature: " + currentWeather.getMain().getTempMax()+"\n"
                                +"Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                                +"City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                );
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v("ERROR", throwable.getMessage());
            }
        });

    }

    public void openTimer(View view) {
        startActivity(new Intent(MainScreen.this, TimePopUp.class));

    }
}
