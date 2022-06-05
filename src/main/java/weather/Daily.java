package weather;

public class Daily {
    Integer dt;
    Integer sunrise;
    Integer sunset;
    Temp temp;
    FeelsLike feels_like;
    Integer pressure;
    Integer humidity;
    Float dew_point;
    Float wind_speed;
    Integer wind_deg;
    Weather[] weather;
    Integer clouds;
    Float pop;
    Float rain;
    Float uvi;

    public Integer getDt() {
        return dt;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeels_like() {
        return feels_like;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Float getDew_point() {
        return dew_point;
    }

    public Float getWind_speed() {
        return wind_speed;
    }

    public Integer getWind_deg() {
        return wind_deg;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Integer getClouds() {
        return clouds;
    }

    public Float getPop() {
        return pop;
    }

    public Float getRain() {
        return rain;
    }

    public Float getUvi() {
        return uvi;
    }
}
