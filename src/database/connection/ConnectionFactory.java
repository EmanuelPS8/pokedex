package database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

//    String url = "jdbc:mysql://:/nome_do_banco?useSSL=false";
    public static Connection getConnection( String ip, String port, String user, String password, String database) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database
                + "?useSSL=false"
                + "&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC",user,password);
    }

}
