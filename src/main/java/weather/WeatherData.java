package weather;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static hiddenData.HiddenData.WEATHER_API_KEY;

public class WeatherData {
    public static DailyWeatherResponse getWeatherResponse(Float latitude, Float longitude) {
        Gson gson = new Gson();
        return gson.fromJson(getDataString(latitude, longitude), DailyWeatherResponse.class);
    }

    public static String getDataString(Float latitude, Float longitude) {
        String API_Key = WEATHER_API_KEY.getRow();
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="
                + latitude.toString() + "&lon=" + longitude.toString()
                + "&exclude=current,minutely,hourly&appid="
                + API_Key + "&units=metric&lang=ru";
        try {
            return getWeatherData(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Getting Data Error!");
            return "";
        }
    }

    private static String getWeatherData(String urlForm) throws IOException {
        URL url = new URL(urlForm);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder weatherData = new StringBuilder();
        String s;
        while ((s = reader.readLine()) != null) {
            weatherData.append(s);
        }
        reader.close();
        return weatherData.toString();
    }
}
