package com.example.sanje.i_weather;



class TempModel {
    /*"temp":{"day":293.13,"min":283.18,"max":294.69,"night":283.18,"eve":291.07,"morn":293.13}*/
    public final float day;
    public final float min;
    public final float max;
    public final float night;

    public TempModel(float day, float min, float max, float night) {
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
    }
}
