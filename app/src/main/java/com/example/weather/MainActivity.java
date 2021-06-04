package com.example.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    // Constants:
    // To display date & time
    TextView dateTime;
    // Permission Code
    final int REQ_CODE = 3;
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String AIR_POLLUTION_URL = "http://api.openweathermap.org/data/2.5/air_pollution";
    final String WEATHER_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast";
    // App ID to use OpenWeather data
    final String APP_ID = "f2f225853212b81fcdc3e92a0ece17ef";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // TODO: Set LOCATION_PROVIDER here:
    String Location_provider = LocationManager.NETWORK_PROVIDER;

    TextView windText;
    TextView aqiText;
    TextView temperature;
    TextView cityName;
    TextView statusText;
    TextView pressText;
    TextView humidText;
    ImageView weatherImage;

    //Forecast variables
    TextView t_dateText;
    TextView second_dateText;
    TextView third_dateText;
    TextView t_dayDescription, second_dayDescription, third_dayDescription;
    TextView t_dayTemp, second_dayTemp, third_dayTemp;
    ImageView t_dayWeatherImage, secondDay_WeatherImage, thirdDay_WeatherImage;

    private int errorId;
    private int successId;
    private int buttonId;
    ImageButton changeCityButton;
    private SoundPool soundPool;

    // TODO: Declare a LocationManager and a LocationListener here:
    LocationManager locationManager;
    LocationListener locationListener;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        errorId = soundPool.load(getApplicationContext(),R.raw.errror,1);
        successId = soundPool.load(getApplicationContext(),R.raw.succes,1);
        buttonId = soundPool.load(getApplicationContext(),R.raw.button,1);

        aqiText = findViewById(R.id.airQuality);
        windText = findViewById(R.id.winds);
        temperature = findViewById(R.id.tempView);
        cityName = findViewById(R.id.locationView);
        weatherImage = findViewById(R.id.weatherSymbol);
        statusText = findViewById(R.id.status);
        pressText = findViewById(R.id.pressure);
        humidText = findViewById(R.id.Humdity);
        changeCityButton = findViewById(R.id.changeCity);

        t_dateText = findViewById(R.id.t_date);
        second_dateText = findViewById(R.id.scnd_date);
        third_dateText = findViewById(R.id.trd_date);
        t_dayDescription = findViewById(R.id.tday_Des);
        second_dayDescription = findViewById(R.id.trd_Des);
        third_dayDescription = findViewById(R.id.scnd_Des);
        t_dayTemp = findViewById(R.id.t_dayTemp);
        second_dayTemp = findViewById(R.id.scnd_dayTemp);
        third_dayTemp = findViewById(R.id.trd_dayTemp);
        t_dayWeatherImage = findViewById(R.id.t_imageView);
        secondDay_WeatherImage = findViewById(R.id.twoDay_Imageview);
        thirdDay_WeatherImage = findViewById(R.id.threeDay_Imageview);

        changeCityButton.setOnClickListener(v -> {
            soundPool.play(buttonId,1,1,0,0,1);
            Intent intent = new Intent(MainActivity.this,Change_city.class);
            startActivity(intent);
        });
    }


    // TODO: Add onResume() here:
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String city = intent.getStringExtra("City");
        if (city != null){
            getWeatherForNewCity(city);
        }
        else {
        getWeatherForCurrentLoc();
    }
    }


    // TODO: Add getWeatherForNewCity(String city) here:
    protected void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        RequestParams pollution_params = new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        requestingForWeatherData(params, pollution_params);
    }

    // TODO: Add getWeatherForCurrentLocation() here:
    private void getWeatherForCurrentLoc() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Log.d("Clima", "ONLOCATIONCHANEGED");
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                //Log.d("Clima","IN LAT :" + latitude);
                //Log.d("Clima","IN LONG:" + longitude);
                RequestParams params = new RequestParams();
                RequestParams pollution_params = new RequestParams();
                pollution_params.put("lat",latitude);
                pollution_params.put("lon",longitude);
                pollution_params.put("appid",APP_ID);
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",APP_ID);
                requestingForWeatherData(params, pollution_params);
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Log.d("Clima", "ProviderDisa");
                AlertDialog.Builder alertBox = new AlertDialog.Builder(MainActivity.this);
                alertBox.setCancelable(false);
                soundPool.play(errorId,1,1,0,0,1);
                alertBox.setMessage("Please make sure your LOCATION is Enabled and TRY AGAIN!!");
                alertBox.setPositiveButton("Close", (dialog, which) -> finish());
                alertBox.show();
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQ_CODE);
            return;
        }
        locationManager.requestLocationUpdates(Location_provider, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Log.d("CLIMA","PERMIGRANTED");
                getWeatherForCurrentLoc();
            } //else --->> Log.d("clima","PERMISION DENIED");

        }
    }
    // TODO: Add Networking(RequestParams params) here:
    private void requestingForWeatherData(RequestParams params, RequestParams pollution_params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                //Log.d("Clima", response.toString());
                weather_data weatherData = weather_data.fromJSON(response);
                soundPool.play(successId,1,1,0,0,1);
                updateUI(weatherData);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                //Log.d("Clima","FAIL" + e.toString());
                //Log.d("Clima", "STATUS CODE" + statusCode);
                Toast.makeText(MainActivity.this,"REQ FAILED", Toast.LENGTH_SHORT).show();
                soundPool.play(errorId,1,1,0,0,1);
                AlertDialog.Builder alertBox = new AlertDialog.Builder(MainActivity.this);
                alertBox.setCancelable(false);
                alertBox.setTitle("Error!!");
                alertBox.setMessage("\nTRY AGAIN!!\nEither your Mobile Data is disabled or you have entered invalid City Name");
                alertBox.setPositiveButton("Close", (dialog, which) -> finish());
                alertBox.show();
            }
        });
        client.get(AIR_POLLUTION_URL,pollution_params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                //Log.d("Climas",response.toString());
                weather_data weatherData = weather_data.fromJSONforAIR(response);
                updateAQI(weatherData);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                //Toast.makeText(MainActivity.this,"REQ 2 FAILED", Toast.LENGTH_SHORT).show();
            }
        });

        client.get(WEATHER_FORECAST_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
               // Log.d("FORECAST",response.toString());
                weather_data weatherData = weather_data.fromJSONforForecasst(response);
                updateForecastUi(weatherData);
            }
        });
    }
    // TODO: Add updateUI() here:
    private void updateAQI(weather_data weather){
        aqiText.setText(weather.getAqi());
    }

    private void updateUI(weather_data weather){
        temperature.setText(weather.getTemperature());
        windText.setText(weather.getWind());
        cityName.setText(weather.getCity());
        statusText.setText(weather.getWeatherType());
        pressText.setText(weather.getPressure());
        humidText.setText(weather.getHumidity());
        int resId = getResources().getIdentifier(weather.getIconName(),"drawable",getPackageName());
        weatherImage.setImageResource(resId);
    }

    private  void updateForecastUi(weather_data weather){
        t_dateText.setText(weather.getOneDayLater_weatherDate());
        second_dateText.setText(weather.getTwoDaysLater_weatherDate());
        third_dateText.setText(weather.getThreeDaysLater_weatherDate());
        t_dayDescription.setText(weather.getOneDayLater_weatherType());
        second_dayDescription.setText(weather.getTwoDaysLater_weatherType());
        third_dayDescription.setText(weather.getThreeDaysLater_weatherType());
        t_dayTemp.setText(weather.getOneDayLater_avgTemp());
        second_dayTemp.setText(weather.getTwoDaysLater_avgTemp());
        third_dayTemp.setText(weather.getThreeDaysLater_avgTemp());
        int OneResId = getResources().getIdentifier(weather.getOneDayLater_icon(),"drawable",getPackageName());
        t_dayWeatherImage.setImageResource(OneResId);
        int TwoResId = getResources().getIdentifier(weather.getTwoDayLater_icon(),"drawable",getPackageName());
        thirdDay_WeatherImage.setImageResource(TwoResId);
        int ThreeResId = getResources().getIdentifier(weather.getThreeDayLater_icon(),"drawable",getPackageName());
        secondDay_WeatherImage.setImageResource(ThreeResId);

    }
    // TODO: Add onPause() here:
    protected void onPause(){
        super.onPause();
        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }
}