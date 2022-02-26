package stinkybot.utils;


import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SqlConnector {

    private final String sqlUser;
    private final String sqlPass;
    private final String sqlUrl = "jdbc:mysql://eu02-sql.pebblehost.com:3306/customer_160091_stinkydb";
    private static SqlConnector instance = null;
    private static final Object lock = new Object();


    private SqlConnector() throws ClassNotFoundException {
        SettingsReader sr = SettingsReader.getInstance();
        Settings settings = sr.getSettings();
        this.sqlUser = settings.getSqlUser();
        this.sqlPass = settings.getSqlPass();
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public static SqlConnector getInstance() {
        if (instance == null)
            synchronized (lock) {
                if (instance == null) {
                    try {
                        instance = new SqlConnector();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        return instance;
    }


    public String updateQuery(String Query) {
        try (Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPass);
             PreparedStatement ps = conn.prepareStatement(Query)) {
            ps.executeUpdate();
            return "success";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public List<String> selectAllNamesFromTrackedList() {
        List<String> list = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPass);
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM trackedlist")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                list.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> selectAllCharacterIdsFromTrackedList() {
        List<String> list = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPass);
             PreparedStatement ps = conn.prepareStatement("SELECT name,characterid FROM trackedlist")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String charid = resultSet.getString("characterid");
                list.add(charid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String selectMaxIdFromDeathEvents() {
        String getMaxQuery = "SELECT id FROM deathevents WHERE id=(SELECT max(id) FROM deathevents)";
        String result = "0";
        try (Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPass);
             PreparedStatement ps = conn.prepareStatement(getMaxQuery)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}