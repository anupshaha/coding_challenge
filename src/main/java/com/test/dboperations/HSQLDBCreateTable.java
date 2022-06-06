package com.test.dboperations;

import com.test.base.Base;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HSQLDBCreateTable extends Base {

    public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HSQLDBCreateTable.class.getName());
    private static final String createTableSQL = "CREATE TABLE IF NOT EXISTS PUBLIC.SERVERLOGS (\r\n" + "  event_id  varchar(30) primary key,\r\n" +
            "  event_duration DOUBLE,\r\n" + "  type varchar(30),\r\n" + "  host varchar(30),\r\n" +
            "  alert varchar(6)\r\n" + "  );";
    public static Connection connection;
    public static HSQLJDBCUtils utils;

    static {
        utils = new HSQLJDBCUtils();
        try {
            connection = utils.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * Mrthod to Create Table in HSQL Database if Table not Exists
     *
     * @throws SQLException
     */
    public void createTable() throws SQLException {

        //log.debug("Create Table SQL: "+ createTableSQL);
        try{
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            log.debug("Table is created/already exists");
        } catch (SQLException e) {
            HSQLJDBCUtils.printSQLException(e);
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
