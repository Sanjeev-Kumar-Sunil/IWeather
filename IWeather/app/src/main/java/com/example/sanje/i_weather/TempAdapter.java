package com.example.sanje.i_weather;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TempAdapter extends BaseAdapter{
    Context context;
    int resource;
    DailyDataModel[] dailyDataModels;

    public TempAdapter(Context context, int resource, DailyDataModel[] dailyDataModels) {
        this.context = context;
        this.resource = resource;
        this.dailyDataModels = dailyDataModels;
    }

    @Override
    public int getCount() {
        return dailyDataModels.length;
    }

    @Override
    public Object getItem(int position) {
        return dailyDataModels[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView=null;
        myView= LayoutInflater.from(context).inflate(resource,parent,false);
        TextView tvTime=(TextView)myView.findViewById(R.id.textViewTimeDescription);
        TextView tvSummary=(TextView)myView.findViewById(R.id.textViewSummaryDescription);
        TextView tvDayTemp=(TextView)myView.findViewById(R.id.textViewTemperatureDayDescription);
        TextView tvNightTemp=(TextView)myView.findViewById(R.id.textViewTemperatureNightDescription);
        TextView tvMinTemp=(TextView)myView.findViewById(R.id.textViewTemperatureMinimumDescription);
        TextView tvMaxTemp=(TextView)myView.findViewById(R.id.textViewTemperatureMaximumDescription);
        TextView tvPressure=(TextView)myView.findViewById(R.id.textViewPressureDescription);
        TextView tvHumidity=(TextView)myView.findViewById(R.id.textViewHumidityDescription);
        //set values in the textview

        tvTime.setText(getDate(dailyDataModels[position].dt));
        tvSummary.setText(dailyDataModels[position].weather[0].main+"-"+dailyDataModels[position].weather[0].description);
        tvDayTemp.setText(String.valueOf(dailyDataModels[position].temp.day));
        tvNightTemp.setText(String.valueOf(dailyDataModels[position].temp.night));
        tvMinTemp.setText(String.valueOf(dailyDataModels[position].temp.min));
        tvMaxTemp.setText(String.valueOf(dailyDataModels[position].temp.max));
        tvPressure.setText(String.valueOf(dailyDataModels[position].pressure));
        tvHumidity.setText(String.valueOf(dailyDataModels[position].humidity));

        return myView;
    }
    private String getDate(long dateEpoch){
        Date date = new Date(dateEpoch*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("d MMM yyyy HH:mm ", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
