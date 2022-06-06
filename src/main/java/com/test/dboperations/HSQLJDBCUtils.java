package com.test.dboperations;

import com.test.base.Base;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HSQLJDBCUtils<connection> extends Base {

    public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HSQLJDBCUtils.class.getName());

    private static String jdbcURL;
    private static String jdbcUsername;
    private static String jdbcPassword;
    public Connection connection;



    static {
        try {
            jdbcURL = getProperty("jdbcURL");
            jdbcUsername = getProperty("jdbcUsername");
            jdbcPassword = getProperty("jdbcPassword");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Connection getConnection() throws ClassNotFoundException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            log.debug("Connected to HSQL Database");
            } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return connection;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}
