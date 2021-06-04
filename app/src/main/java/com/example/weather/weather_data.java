package com.example.weather;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class weather_data {

    // TODO: Declare the member variables here
    private String Temperature;
    private String wind;
    private String City;
    private String IconName;
    private String pressure;
    private String humidity;
    private String weatherType;
    private String aqi;
    private int Condition;

    // FORECAST VARIABLES
    private String oneDayLater_weatherDate;
    private String twoDaysLater_weatherDate;
    private String threeDaysLater_weatherDate;

    private String oneDayLater_weatherType;
    private String twoDaysLater_weatherType;
    private String threeDaysLater_weatherType;

    private String oneDayLater_avgTemp;
    private String twoDaysLater_avgTemp;
    private String threeDaysLater_avgTemp;

    private int oneDayLater_condition, twoDayLater_condition, threeDayLater_condition;
    private String oneDayLater_icon, twoDayLater_icon, threeDayLater_icon;

    // TODO: Create a WeatherDataModel from a JSON:
    public static weather_data fromJSON(JSONObject jsonObject){
        try {
            weather_data weatherData = new weather_data();
            //Log.d("new","workingggggggggggg");

            weatherData.City = jsonObject.getString("name");
            weatherData.Condition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.IconName = updateWeatherIcon(weatherData.Condition);
            weatherData.weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

            int tempRes = (int) Math.rint(jsonObject.getJSONObject("main").getDouble("temp") - 273.15);
            weatherData.Temperature = Integer.toString(tempRes);
            weatherData.wind = Integer.toString((int) (jsonObject.getJSONObject("wind").getInt("speed") * 3.6));

            weatherData.pressure = Integer.toString(jsonObject.getJSONObject("main").getInt("pressure"));
            weatherData.humidity = Integer.toString(jsonObject.getJSONObject("main").getInt("humidity"));

            return weatherData;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static weather_data fromJSONforAIR(JSONObject jsonObject){
        try {
            weather_data weatherData = new weather_data();
            //Log.d("Clima", "Before.....aqi");
            weatherData.aqi = Integer.toString(jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getInt("aqi"));
            //Log.d("Clima", "GOT AQII");
            return weatherData;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static weather_data fromJSONforForecasst(JSONObject jsonObject){
        try {
            weather_data weatherDataForecast = new weather_data();
            weatherDataForecast.oneDayLater_weatherDate = jsonObject.getJSONArray("list").getJSONObject(8).getString("dt_txt").substring(0,11);
            weatherDataForecast.twoDaysLater_weatherDate = jsonObject.getJSONArray("list").getJSONObject(16).getString("dt_txt").substring(0,11);
            weatherDataForecast.threeDaysLater_weatherDate = jsonObject.getJSONArray("list").getJSONObject(24).getString("dt_txt").substring(0,11);
            weatherDataForecast.oneDayLater_weatherType = jsonObject.getJSONArray("list").getJSONObject(8).getJSONArray("weather").getJSONObject(0).getString("main");
            weatherDataForecast.twoDaysLater_weatherType = jsonObject.getJSONArray("list").getJSONObject(16).getJSONArray("weather").getJSONObject(0).getString("main");
            weatherDataForecast.threeDaysLater_weatherType = jsonObject.getJSONArray("list").getJSONObject(24).getJSONArray("weather").getJSONObject(0).getString("main");

            weatherDataForecast.oneDayLater_avgTemp = Integer.toString((int) (jsonObject.getJSONArray("list").getJSONObject(8).getJSONObject("main").getDouble("temp_max")-273.15));
            weatherDataForecast.twoDaysLater_avgTemp = Integer.toString((int) (jsonObject.getJSONArray("list").getJSONObject(16).getJSONObject("main").getDouble("temp_max")-273.15));
            weatherDataForecast.threeDaysLater_avgTemp = Integer.toString((int) (jsonObject.getJSONArray("list").getJSONObject(24).getJSONObject("main").getDouble("temp_max")-273.15));

            weatherDataForecast.oneDayLater_condition = jsonObject.getJSONArray("list").getJSONObject(8).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherDataForecast.oneDayLater_icon = updateWeatherIcon(weatherDataForecast.oneDayLater_condition);

            weatherDataForecast.twoDayLater_condition = jsonObject.getJSONArray("list").getJSONObject(16).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherDataForecast.twoDayLater_icon = updateWeatherIcon(weatherDataForecast.twoDayLater_condition);

            weatherDataForecast.threeDayLater_condition = jsonObject.getJSONArray("list").getJSONObject(24).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherDataForecast.threeDayLater_icon = updateWeatherIcon(weatherDataForecast.threeDayLater_condition);
            //Log.d("CLIMA",weatherDataForecast.oneDayLater_weatherDate + "TYPE " + weatherDataForecast.oneDayLater_weatherType + " temp - "+weatherDataForecast.oneDayLater_avgTemp);
            //Log.d("CLIMA",weatherDataForecast.twoDaysLater_weatherDate+ "TYPE " + weatherDataForecast.twoDaysLater_weatherType+ " temp - "+weatherDataForecast.twoDaysLater_avgTemp);
            //Log.d("CLIMA",weatherDataForecast.threeDaysLater_weatherDate+ "TYPE " + weatherDataForecast.threeDaysLater_weatherType+ " temp - "+weatherDataForecast.threeDaysLater_avgTemp);

            return weatherDataForecast;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }


    // TODO: Uncomment to this to get the weather image name from the condition:
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    // TODO: Create getter methods for temperature, city, and icon name:


    public String getAqi() {
        if (aqi.equals("1")){
            return "AQ - Good";
        }
        if (aqi.equals("2")){
            return "AQ - Fair";
        }
        if (aqi.equals("3")){
            return "AQ - Moderate";
        }
        if (aqi.equals("4")){
            return "AQ - Moderate";
        }
        if (aqi.equals("5")){
            return "AQ - Worse";
        }
        else {
            return "AQ - Worst";
        }
    }

    public String getWind() {
        return wind+"Km/h";
    }

    public String getPressure() {
        return pressure+"\nhPa";
    }

    public String getHumidity() {
        return humidity+"%";
    }

    public String getWeatherType() {
        return weatherType;
    }

    public String getTemperature() {
        return Temperature + "°C";
    }

    public String getCity() {
        return City;
    }

    public String getIconName() {
        return IconName;
    }

    public String getOneDayLater_weatherDate() {
        return oneDayLater_weatherDate;
    }

    public String getTwoDaysLater_weatherDate() {
        return twoDaysLater_weatherDate;
    }

    public String getThreeDaysLater_weatherDate() {
        return threeDaysLater_weatherDate;
    }

    public String getOneDayLater_weatherType() {
        return oneDayLater_weatherType;
    }

    public String getTwoDaysLater_weatherType() {
        return twoDaysLater_weatherType;
    }

    public String getThreeDaysLater_weatherType() {
        return threeDaysLater_weatherType;
    }

    public String getOneDayLater_avgTemp() {
        return oneDayLater_avgTemp;
    }

    public String getTwoDaysLater_avgTemp() {
        return twoDaysLater_avgTemp;
    }

    public String getThreeDaysLater_avgTemp() {
        return threeDaysLater_avgTemp;
    }

    public String getOneDayLater_icon() {
        return oneDayLater_icon;
    }

    public String getTwoDayLater_icon() {
        return twoDayLater_icon;
    }

    public String getThreeDayLater_icon() {
        return threeDayLater_icon;
    }
}
