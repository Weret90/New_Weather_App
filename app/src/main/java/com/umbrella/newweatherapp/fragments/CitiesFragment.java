package com.umbrella.newweatherapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umbrella.newweatherapp.CitiesAdapter;
import com.umbrella.newweatherapp.City;
import com.umbrella.newweatherapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


public class CitiesFragment extends Fragment {

    private final String MOSCOW_LATITUDE = "55.75";
    private final String ST_PETERBURG_LATITUDE = "59.93";
    private final String MOSCOW_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=55.75&lon=37.61&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private final String PITER_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=59.93&lon=30.33&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private final String YAKUTSK_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=62.03&lon=129.67&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private ArrayList<City> cities = new ArrayList<>();
    private CountDownLatch countDownLatch = new CountDownLatch(3);

    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (cities.isEmpty()) {
            new Thread(() -> {
                DownloadWeatherTask task1 = new DownloadWeatherTask();
                task1.execute(MOSCOW_URL);
            }).start();
            new Thread(() -> {
                DownloadWeatherTask task2 = new DownloadWeatherTask();
                task2.execute(YAKUTSK_URL);
            }).start();
            new Thread(() -> {
                DownloadWeatherTask task3 = new DownloadWeatherTask();
                task3.execute(PITER_URL);
            }).start();

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        RecyclerView recyclerViewCities = view.findViewById(R.id.recyclerViewCities);
        CitiesAdapter adapter = new CitiesAdapter(cities);
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewCities.setAdapter(adapter);
        adapter.setOnCityClickListener(position -> getParentFragmentManager().beginTransaction().replace(R.id.main_container, CityInfoFragment.newInstance(cities.get(position))).addToBackStack(null).commit());
    }

    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection httpURLConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                countDownLatch.countDown();
            }
            return result.toString();
        }

        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String latitude = jsonObject.getString("lat");
                String city;
                switch (latitude) {
                    case MOSCOW_LATITUDE:
                        city = "Москва";
                        break;
                    case ST_PETERBURG_LATITUDE:
                        city = "Санкт-Петербург";
                        break;
                    default:
                        city = "Якутск";
                        break;
                }
                String temperature = jsonObject.getJSONObject("current").getString("temp") + "°C";
                String status = jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");
                String tomorrowDay = jsonObject.getJSONArray("daily").getJSONObject(1).getString("dt");
                String tomorrowTemperature = jsonObject.getJSONArray("daily").getJSONObject(1).getJSONObject("temp").getString("day") + "°C";
                String tomorrowStatus = jsonObject.getJSONArray("daily").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description");
                String afterTomorrowDay = jsonObject.getJSONArray("daily").getJSONObject(2).getString("dt");
                String afterTomorrowTemperature = jsonObject.getJSONArray("daily").getJSONObject(2).getJSONObject("temp").getString("day") + "°C";
                String afterTomorrowStatus = jsonObject.getJSONArray("daily").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("description");
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
                Date tomorrow = new Date(Long.parseLong(tomorrowDay) * 1000);
                Date afterTomorrow = new Date(Long.parseLong(afterTomorrowDay) * 1000);

                cities.add(new City(city, status, temperature, sdf.format(tomorrow), tomorrowStatus, tomorrowTemperature, sdf.format(afterTomorrow), afterTomorrowStatus, afterTomorrowTemperature));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}