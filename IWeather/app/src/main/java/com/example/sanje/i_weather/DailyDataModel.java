package com.example.sanje.i_weather;



class DailyDataModel {
    /*":[{"dt":1516168800,"temp":{"day":293.13,"min":283.18,"max":294.69,"night":283.18,"eve":291.07,"morn":293.13},
    "pressure":1024.77,"humidity":98,"weather":[{"id":800,"main":"Clear",
    "description":"sky is clear","icon":"01d"}],"speed":2.06,"deg":274,"clouds":0
     */
    public final long dt;
    public final TempModel temp;
    public final float pressure;
    public final int humidity;
    public final WeatherModel[] weather;
    public final float speed;

    public DailyDataModel(long dt, TempModel temp, float pressure, int humidity, WeatherModel[] weather, float speed) {
        this.dt = dt;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weather = weather;
        this.speed = speed;
    }
}
