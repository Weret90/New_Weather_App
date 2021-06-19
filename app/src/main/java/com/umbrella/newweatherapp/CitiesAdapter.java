package com.umbrella.newweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder> {

    private ArrayList<City> cities;
    private OnCityClickListener onCityClickListener;

    public CitiesAdapter(ArrayList<City> cities) {
        this.cities = cities;
    }

    public interface OnCityClickListener {
        void onCityClick(int position);
    }

    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    @NonNull
    @Override
    public CitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesViewHolder holder, int position) {
        City city = cities.get(position);
        holder.textViewName.setText(city.getName());
        holder.textViewStatus.setText(city.getStatus());
        holder.textViewTemperature.setText(city.getTemperature());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CitiesViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewStatus;
        private TextView textViewTemperature;

        public CitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTemperature = itemView.findViewById(R.id.textViewTemperature);

            itemView.setOnClickListener(v -> {
                if (onCityClickListener != null) {
                    onCityClickListener.onCityClick(getAdapterPosition());
                }
            });
        }
    }
}
