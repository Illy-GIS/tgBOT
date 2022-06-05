package database;

import hiddenData.*;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static final String url = HiddenData.DB_URL.getRow();
    private static final String user = HiddenData.DB_USER.getRow();
    private static final String password = HiddenData.DB_PASSWORD.getRow();
    private static Statement statement;

    public DataBase() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("MySQL Connection Error!");
        }
    }

    public void addRow(Integer ID_Chat, Float latitude, Float longitude) {
        String query = "INSERT INTO chatinfo VALUES (" + ID_Chat + ", " + latitude + ", " + longitude + ", 0);";
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("INSERT Error!");
        }
    }

    public Table_Chat getCertainChatRow(Integer ID_Chat) {
        String query = "SELECT * FROM chatinfo WHERE ID_Chat = " + ID_Chat + ";";
        Table_Chat resultChat = new Table_Chat();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                throw new NullPointerException("Requested ID was not found!");
            } else {
                resultChat.setID_Chat(resultSet.getInt("ID_Chat"));
                resultChat.setLatitude(resultSet.getFloat("Latitude"));
                resultChat.setLongitude(resultSet.getFloat("Longitude"));
                resultChat.setIs_Subscribed(resultSet.getByte("Is_Subscribed"));
                return resultChat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SELECT Error!");
            return resultChat;
        }
    }

    public boolean existsChat(Integer ID_Chat) {
        try {
            getCertainChatRow(ID_Chat);
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public void updateLocation(Integer ID_Chat, Float latitude, Float longitude) {
        String query = "UPDATE chatinfo SET Latitude = " + latitude
                + ", Longitude = " + longitude + " WHERE ID_Chat = " + ID_Chat + ";";
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("UPDATE Error!");
        }
    }

    public boolean isSubscribed(Integer ID_Chat) {
        Table_Chat chatRow = getCertainChatRow(ID_Chat);
        return chatRow.getIs_Subscribed() != 0;
    }

    public void subscribe(Integer ID_Chat) {
        if (!isSubscribed(ID_Chat)) {
            String query = "UPDATE chatinfo SET Is_Subscribed = 1 WHERE ID_Chat = " + ID_Chat + ";";
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("UPDATE Subscription Error!");
            }
        }
    }

    public void unsubscribe(Integer ID_Chat) {
        if (isSubscribed(ID_Chat)) {
            String query = "UPDATE chatinfo SET Is_Subscribed = 0 WHERE ID_Chat = " + ID_Chat + ";";
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("UPDATE Subscription Error!");
            }
        }
    }

    public ArrayList<Integer> getSubscribersList() {
        ArrayList<Integer> subscribersList = new ArrayList<>();
        String query = "SELECT ID_Chat FROM chatinfo WHERE Is_Subscribed = 1";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                subscribersList.add(resultSet.getInt("ID_Chat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SELECT Subscribers Error!");
        }
        return subscribersList;
    }
}
