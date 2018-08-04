package com.example.sanje.i_weather;



public class PojoClass {
    public final CurrentModel city;
    public final DailyDataModel[] list;
    public PojoClass(CurrentModel city, DailyDataModel[] list) {
        this.city = city;
        this.list = list;
    }
}
