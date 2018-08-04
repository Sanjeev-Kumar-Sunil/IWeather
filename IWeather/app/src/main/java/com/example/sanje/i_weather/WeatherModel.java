package com.example.sanje.i_weather;


class WeatherModel {
    //"weather":[{"id":800,"main":"Clear","description":"sky is clear","icon":"01d"}]
    public final String main;
    public final String description;

    public WeatherModel(String main, String description) {
        this.main = main;
        this.description = description;
    }
}
