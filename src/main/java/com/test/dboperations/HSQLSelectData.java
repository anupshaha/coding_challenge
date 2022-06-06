package com.test.dboperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HSQLSelectData {
    public static Connection connection;
    public static HSQLJDBCUtils utils;
    public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HSQLSelectData.class.getName());
    public ResultSet result = null;

    static {
        utils = new HSQLJDBCUtils();
        try {
            connection = utils.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> selectRecord(String SELECT_LOG_SQL) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LOG_SQL);
            result = preparedStatement.executeQuery();
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("id", result.getString("event_id"));
            resultMap.put("type", result.getString("type"));
            resultMap.put("host", result.getString("host"));
            return resultMap;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}