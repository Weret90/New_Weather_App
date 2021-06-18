package com.umbrella.newweatherapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class CityInfoFragment extends Fragment {


    private final String CITY_MOSCOW = "Москва";
    private final String CITY_YAKUTSK = "Якутск";
    private final String MOSCOW_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=55.75&lon=37.61&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private final String PITER_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=59.93&lon=30.33&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private final String YAKUTSK_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=62.03&lon=129.67&appid=67401936c26274f7e3d9b19dd82b101c&lang=ru&units=metric";
    private static final String CLICKED_CITY = "CLICKED_CITY";
    private City clickedCity;
    private TextView cityName;
    private TextView currentTemp;
    private TextView tomorrowTemp;
    private TextView afterTomorrowTemp;
    private TextView tomorrowDay;
    private TextView afterTomorrowDay;
    private TextView currentStatus;
    private TextView tomorrowStatus;
    private TextView afterTomorrowStatus;

    public CityInfoFragment() {
        // Required empty public constructor
    }

    public static CityInfoFragment newInstance(City city) {
        CityInfoFragment fragment = new CityInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLICKED_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityName = view.findViewById(R.id.cityName);
        currentTemp = view.findViewById(R.id.currentTemperature);
        tomorrowTemp = view.findViewById(R.id.tomorrowTemperature);
        afterTomorrowTemp = view.findViewById(R.id.afterTomorrowTemperature);
        tomorrowDay = view.findViewById(R.id.tomorrowDay);
        afterTomorrowDay = view.findViewById(R.id.afterTomorrowDay);
        currentStatus = view.findViewById(R.id.currentStatus);
        tomorrowStatus = view.findViewById(R.id.tomorowStatus);
        afterTomorrowStatus = view.findViewById(R.id.afterTomorrowStatus);

        assert getArguments() != null;
        clickedCity = (City) getArguments().getSerializable(CLICKED_CITY);
        String chosenUrl;
        switch (clickedCity.getName()) {
            case CITY_MOSCOW:
                chosenUrl = MOSCOW_URL;
                break;
            case CITY_YAKUTSK:
                chosenUrl = YAKUTSK_URL;
                break;
            default:
                chosenUrl = PITER_URL;
        }
        DownloadWeatherTask task = new DownloadWeatherTask();
        task.execute(chosenUrl);
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
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String tomorrowDayString = jsonObject.getJSONArray("daily").getJSONObject(1).getString("dt");
                String tomorrowTemperature = jsonObject.getJSONArray("daily").getJSONObject(1).getJSONObject("temp").getString("day");
                String tomorrowStatusString = jsonObject.getJSONArray("daily").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description");
                String afterTomorrowDayString = jsonObject.getJSONArray("daily").getJSONObject(2).getString("dt");
                String afterTomorrowTemperature = jsonObject.getJSONArray("daily").getJSONObject(2).getJSONObject("temp").getString("day");
                String afterTomorrowStatusString = jsonObject.getJSONArray("daily").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("description");
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
                Date tomorrow = new Date(Long.parseLong(tomorrowDayString));
                Date afterTomorrow = new Date(Long.parseLong(afterTomorrowDayString));

                cityName.setText(clickedCity.getName());
                currentTemp.setText(clickedCity.getTemperature());
                tomorrowTemp.setText(tomorrowTemperature);
                afterTomorrowTemp.setText(afterTomorrowTemperature);
                tomorrowDay.setText(sdf.format(tomorrow));
                afterTomorrowDay.setText(sdf.format(afterTomorrow));
                currentStatus.setText(clickedCity.getStatus());
                tomorrowStatus.setText(tomorrowStatusString);
                afterTomorrowStatus.setText(afterTomorrowStatusString);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}