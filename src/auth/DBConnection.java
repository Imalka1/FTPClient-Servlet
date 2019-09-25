package auth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dBConnection;
    private Connection connection;

    private DBConnection() {
        try {
            System.out.println(this.getClass().getClassLoader().getResource("ftp_client_properties/ftp.db").getPath());
            Class.forName("org.sqlite.JDBC");//--Load JDBC driver from library(mysql connector)
            connection = DriverManager.getConnection("jdbc:sqlite:" + this.getClass().getClassLoader().getResource("ftp_client_properties/ftp.db").getPath());//--Establish the database connection via db url, db username and db password
        } catch (SQLException e) {//--Catch if any sql exception occurred
            e.printStackTrace();
        } catch (ClassNotFoundException e) {//--Catch if driver is not loaded or cannot be found
            e.printStackTrace();
        }
    }

    //---------------------------------------Return DBConnection object-------------------------------------------------
    public static DBConnection getDBConnection() {
        if (dBConnection == null) {
            dBConnection = new DBConnection();//--Creates DBConnection object to retrieve database connection
        }
        return dBConnection;
    }

    //-----------------------------------------Return database connection-----------------------------------------------
    public Connection getConnection() {
        return connection;
    }
}
