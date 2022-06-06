package com.test.dboperations;

import org.apache.log4j.PropertyConfigurator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

public class HSQLDBInsertData{

    public static Connection connection;
    public static HSQLJDBCUtils utils;
    public static HSQLDBCreateTable createTable;
    public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HSQLDBInsertData.class.getName());
    private static final String INSERT_LOG_SQL = "INSERT INTO PUBLIC.SERVERLOGS" +
            "  (event_id, event_duration, type, host, alert) VALUES " +
            " (?, ?, ?, ?, ?);";

    static {
        String log4jConfPath = System.getProperty("user.dir")+"//PropertyFiles//log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }

    static {
        utils = new HSQLJDBCUtils();
        try {
            connection = utils.getConnection();
            createTable = new HSQLDBCreateTable();
            createTable.createTable();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Method to insert data in HSQL Database
     *
     * @param insertDataMap
     */
    public void insertRecord(Map<String, String> insertDataMap) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOG_SQL);
            preparedStatement.setString(1, insertDataMap.get("id"));
            preparedStatement.setString(2, insertDataMap.get("duration"));
            preparedStatement.setString(3, insertDataMap.get("type"));
            preparedStatement.setString(4, insertDataMap.get("host"));
            preparedStatement.setString(5, insertDataMap.get("alert"));
            log.debug("Insert SQL: " + preparedStatement);
            preparedStatement.executeUpdate();
            log.debug("Log Event Record is Inserted to database for event id: " + insertDataMap.get("id"));
        } catch (SQLIntegrityConstraintViolationException e) {
            log.debug("Log Event Record is exists in database for event id: " + insertDataMap.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
