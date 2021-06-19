package com.umbrella.newweatherapp.fragments;

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

public class CityInfoFragment extends Fragment {

    private static final String CLICKED_CITY = "CLICKED_CITY";

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

        TextView cityName = view.findViewById(R.id.cityName);
        TextView currentTemp = view.findViewById(R.id.currentTemperature);
        TextView tomorrowTemp = view.findViewById(R.id.tomorrowTemperature);
        TextView afterTomorrowTemp = view.findViewById(R.id.afterTomorrowTemperature);
        TextView tomorrowDay = view.findViewById(R.id.tomorrowDay);
        TextView afterTomorrowDay = view.findViewById(R.id.afterTomorrowDay);
        TextView currentStatus = view.findViewById(R.id.currentStatus);
        TextView tomorrowStatus = view.findViewById(R.id.tomorowStatus);
        TextView afterTomorrowStatus = view.findViewById(R.id.afterTomorrowStatus);

        assert getArguments() != null;
        City city = (City) getArguments().getSerializable(CLICKED_CITY);

        cityName.setText(city.getName());
        currentTemp.setText(city.getTemperature());
        tomorrowTemp.setText(city.getTomorrowDayTemperature());
        afterTomorrowTemp.setText(city.getAfterTomorrowDayTemperature());
        tomorrowDay.setText(city.getTomorrowDay());
        afterTomorrowDay.setText(city.getAfterTomorrowDay());
        currentStatus.setText(city.getStatus());
        tomorrowStatus.setText(city.getTomorrowDayStatus());
        afterTomorrowStatus.setText(city.getAfterTomorrowDayStatus());
    }
}