package com.example.sanje.i_weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DailyWeatherForecastActivity extends AppCompatActivity implements LocationListener{
    ListView lvWeatherList;
    TextView tvPlacePlusCountryDescription;
    LocationManager locationManager;
    ProgressDialog progressDialog;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather_forecast);
        initViews();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Please grant Location permission in the manifest",Toast.LENGTH_LONG).show();
            // return;
        }else{
            progressDialog.show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,4000,1000,this);

        }
    }
    public void initViews(){
       lvWeatherList=(ListView)findViewById(R.id.listViewActivityDailyWeatherForecast);
       tvPlacePlusCountryDescription=(TextView)findViewById(R.id.textViewPlacePlusCountryDescription);
        //initializing location items
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Location...");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Toast.makeText(DailyWeatherForecastActivity.this,"Location: "+latitude+" , "+longitude,Toast.LENGTH_LONG).show();
        locationManager.removeUpdates(this);
        progressDialog.dismiss();
        getForecast();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void getForecast(){
        if(isNetworkAvailable()){
            //TODO:If network is connected
            /*String INFOURL="http://api.openweathermap.org/data/2.5/weather?" +
                    "lat="+latitude+"&lon="+longitude+"&appid=ebfcac32bda131ed5a160f2757938396";*/
            String INFOURL="http://api.openweathermap.org/data/2.5/forecast/daily?" +
                    "lat="+latitude+"&lon="+longitude+"&cnt=10&appid=ebfcac32bda131ed5a160f2757938396";
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(INFOURL).build();
            Call call=client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    //Toast.makeText(CurrentUpdateActivity.this,"Failure",Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onResponse(Response response) throws IOException {
                    // Toast.makeText(CurrentUpdateActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    final String responsedata=response.isSuccessful()?response.body().string():null;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                parseResponseData(responsedata);
                            }catch(Exception e){

                            }
                        }
                    });

                }
            });
        }
    }
    public void parseResponseData(String responseDataFinal) throws JSONException {

        //JSONObject jsonObject=new JSONObject(responseDataFinal);
        if (!TextUtils.isEmpty(responseDataFinal)){
            final PojoClass pojoClass=new GsonBuilder().create().fromJson(responseDataFinal,PojoClass.class);
            updateDisplay(pojoClass);
        }else{
            Toast.makeText(this,"Response is empty",Toast.LENGTH_SHORT).show();
        }



    }
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }else{
            Toast.makeText(DailyWeatherForecastActivity.this,"Network Not connected",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void updateDisplay(PojoClass pojoClassFinal){
        tvPlacePlusCountryDescription.setText(pojoClassFinal.city.name+"( "+pojoClassFinal.city.country+" )");
        Toast.makeText(DailyWeatherForecastActivity.this,"Target Successful",Toast.LENGTH_SHORT).show();

        TempAdapter tempAdapter=new TempAdapter(this,R.layout.activity_item_listview_dailyforecast,pojoClassFinal.list);
        lvWeatherList.setAdapter(tempAdapter);
    }
}
