package com.greenerlawn.greenerlawn;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;

import java.util.List;

public class SearchCity extends AppCompatActivity {
    private static final String OPEN_API_KEY = "bea4b929ff482f02d7ab334b6e015467";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double heightPercentage = .8;
        double widthPercentage = .8;
        getWindow().setLayout((int)(width*widthPercentage), (int)(height*heightPercentage));

        final EditText searchBar = findViewById(R.id.searchCities);

        Bundle bundle = getIntent().getExtras();
        initList(bundle.getString("Country"));

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                initList(searchBar.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void initList(String city){
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();

        config.ApiKey = OPEN_API_KEY;
        try {
            WeatherClient client = builder.attach(this)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient.class)
                    .config(config)
                    .build();

            client.searchCity(city, new WeatherClient.CityEventListener() {
                @Override
                public void onCityListRetrieved(List<City> cityList) {
                    ListAdapter cityAdapter = new ArrayAdapter<City>(SearchCity.this, android.R.layout.simple_list_item_1, cityList);
                    ListView cityListView = findViewById(R.id.listCities);
                    cityListView.setAdapter(cityAdapter);

                    cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            City chosenCity = (City) parent.getAdapter().getItem(position);
                            endActivity(chosenCity);
                        }
                    });

                }

                @Override
                public void onWeatherError(WeatherLibException wle) {

                }

                @Override
                public void onConnectionError(Throwable t) {

                }
            });
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }
    }

    private void endActivity(City city){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("resultId",city.getId());
        returnIntent.putExtra("resultCountry",city.getName());
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
