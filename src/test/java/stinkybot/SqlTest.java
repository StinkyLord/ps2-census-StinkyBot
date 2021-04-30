package stinkybot;

import org.junit.Test;

import java.sql.*;

public class SqlTest {


    @Test
    public void test() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
//        String sqlSelectAllPersons = "INSERT INTO trackedlist VALUES('bb', '67'),('bebo','45')";
        String sqlSelectAllPersons = "SELECT * FROM trackedlist";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://eu02-sql.pebblehost.com:3306/customer_160091_stinkydb",
                "customer_160091_stinkydb",
                "F00k3rws!123");
             PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
