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
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class CitiesFragment extends Fragment {

    private final String MOSCOW_URL = "https://api.openweathermap.org/data/2.5/weather?q=москва&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private final String PITER_URL = "https://api.openweathermap.org/data/2.5/weather?q=санкт-петербург&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private final String YAKUTSK_URL = "https://api.openweathermap.org/data/2.5/weather?q=якутск&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private RecyclerView recyclerViewCities;
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
                DownloadWeatherTask task2 = new DownloadWeatherTask();
                task2.execute(PITER_URL);
            }).start();

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        recyclerViewCities = view.findViewById(R.id.recyclerViewCities);
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

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String city = jsonObject.getString("name");
                String temp = jsonObject.getJSONObject("main").getString("temp");
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                cities.add(new City(city, description, temp));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}