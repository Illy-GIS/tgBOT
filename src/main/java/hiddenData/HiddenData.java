package hiddenData;

public enum HiddenData {
    DB_URL("jdbc:mysql://localhost:3306/tgBotDB?autoReconnect=true&useSSL=FALSE&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"),
    DB_USER("root"),
    DB_PASSWORD("100701"),
    WEATHER_API_KEY("439cb9864642a0e171c6b65db1ac3bee"),
    BOT_NAME("illyzhil_bot"),
    BOT_TOKEN("1324004386:AAG4RTPplYNhLB3Y1Tz8dbaDOWcb2EjfkYI"),
    NOTIFY_GAP_SECONDS("5");

    private final String row;

    HiddenData(String s) {
        row = s;
    }

    public String getRow() {
        return row;
    }
}
