package weather;

public class DailyWeatherResponse {
    Float latitude;
    Float longitude;
    String timezone;
    Integer timezone_offset;
    Daily[] daily;

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public Integer getTimezone_offset() {
        return timezone_offset;
    }

    public Daily getDaily(int index) {
        return daily[index];
    }
}
