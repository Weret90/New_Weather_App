package com.umbrella.newweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.umbrella.newweatherapp.fragments.CitiesFragment;
import com.umbrella.newweatherapp.fragments.CityInfoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.main_container, new CitiesFragment()).commit();
    }
}