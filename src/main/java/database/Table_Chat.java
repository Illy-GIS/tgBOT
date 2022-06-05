package database;

public class Table_Chat {
    private Integer ID_Chat;
    private Float Latitude;
    private Float Longitude;
    private Byte Is_Subscribed;

    public Table_Chat() {}

    public Table_Chat(Integer ID_Chat, Float latitude, Float longitude, Byte is_Subscribed) {
        this.ID_Chat = ID_Chat;
        Latitude = latitude;
        Longitude = longitude;
        Is_Subscribed = is_Subscribed;
    }

    public Integer getID_Chat() {
        return ID_Chat;
    }

    public void setID_Chat(Integer ID_Chat) {
        this.ID_Chat = ID_Chat;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float longitude) {
        Longitude = longitude;
    }

    public Byte getIs_Subscribed() {
        return Is_Subscribed;
    }

    public void setIs_Subscribed(Byte is_Subscribed) {
        Is_Subscribed = is_Subscribed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Table_Chat{");
        sb.append("ID_Chat=").append(ID_Chat);
        sb.append(", Latitude=").append(Latitude);
        sb.append(", Longitude=").append(Longitude);
        sb.append(", Is_Subscribed=").append(Is_Subscribed);
        sb.append('}');
        return sb.toString();
    }
}
