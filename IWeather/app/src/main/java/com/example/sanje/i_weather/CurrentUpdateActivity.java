package com.example.sanje.i_weather;

import android.*;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class CurrentUpdateActivity extends AppCompatActivity implements LocationListener {
    TextView etLocation,etTime,etSunrise,etSunset,etSummary,etTemp,etTempMin,etTempMax,etPressure,etHumidity;
    LocationManager locationManager;
    ProgressDialog progressDialog;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_update);
        initViews();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Please grant Location permission in the manifest",Toast.LENGTH_LONG).show();
            // return;
        }else{
            progressDialog.show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 10, this);

        }

    }

    @Override
    public void onLocationChanged(Location location) {
         latitude=location.getLatitude();
         longitude=location.getLongitude();
         Toast.makeText(CurrentUpdateActivity.this,"Location: "+latitude+" , "+longitude,Toast.LENGTH_LONG).show();
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
    //user defined function
    public void initViews(){
        etLocation=(TextView) findViewById(R.id.textViewPlacePlusCountryDescription);
        etTime=(TextView)findViewById(R.id.textViewTimeDescription);
        etSunrise=(TextView)findViewById(R.id.textViewSunriseDescription);
        etSunset=(TextView)findViewById(R.id.textViewSunsetDescription);
        etSummary=(TextView)findViewById(R.id.textViewSummaryDescription);
        etTemp=(TextView)findViewById(R.id.textViewTemperatureDescription);
        etTempMin=(TextView)findViewById(R.id.textViewTemperatureMinimumDescription);
        etTempMax=(TextView)findViewById(R.id.textViewTemperatureMaximumDescription);
        etPressure=(TextView)findViewById(R.id.textViewPressureDescription);
        etHumidity=(TextView)findViewById(R.id.textViewHumidityDescription);
        //initializing location items
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Location...");
        progressDialog.setCancelable(false);
    }
    public void getForecast(){
        if(isNetworkAvailable()){
           //TODO:If network is connected
            String INFOURL="http://api.openweathermap.org/data/2.5/weather?" +
                    "lat="+latitude+"&lon="+longitude+"&appid=ebfcac32bda131ed5a160f2757938396";
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
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }else{
            Toast.makeText(CurrentUpdateActivity.this,"Network Not connected",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void parseResponseData(String responseDataFinal) throws JSONException {

         JSONObject jsonObject=new JSONObject(responseDataFinal);
               JSONArray jsonArray=jsonObject.getJSONArray("weather");
                     JSONObject jsonObject1=jsonArray.getJSONObject(0);
                             String jsonObject11=jsonObject1.getString("main");
                             String jsonObject12=jsonObject1.getString("description");
                     JSONObject jsonObject2=jsonObject.getJSONObject("main");
                             String jsonObject21=String.valueOf(jsonObject2.getDouble("temp"));
                             String jsonObject22=String.valueOf(jsonObject2.getInt("pressure"));
                             String jsonObject23=String.valueOf(jsonObject2.getInt("humidity"));
                              String jsonObject24=String.valueOf(jsonObject2.getDouble("temp_min"));
                              String jsonObject25=String.valueOf(jsonObject2.getDouble("temp_max"));
                     long jsonObject3=jsonObject.getLong("dt");
                     JSONObject jsonObject4=jsonObject.getJSONObject("sys");
                            String jsonObject41=jsonObject4.getString("country");
                            long jsonObject42=jsonObject4.getLong("sunrise");
                            long jsonObject43=jsonObject4.getLong("sunset");
                     String jsonObject5=jsonObject.getString("name");

        Toast.makeText(CurrentUpdateActivity.this,"Success ",Toast.LENGTH_SHORT).show();
        etLocation.setText(jsonObject5+"( "+jsonObject41+" )");
        etTime.setText(getDate(jsonObject3));
        etSunrise.setText(getDate(jsonObject42));
        etSunset.setText(getDate(jsonObject43));
        etSummary.setText(jsonObject11+","+jsonObject12);
        etTemp.setText(jsonObject21);
        etTempMin.setText(jsonObject24);
        etTempMax.setText(jsonObject25);
        etPressure.setText(jsonObject22);
        etHumidity.setText(jsonObject23);

    }
    private String getDate(long dateEpoch){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(dateEpoch);
    }
}
