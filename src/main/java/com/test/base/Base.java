package com.test.base;

import com.test.dboperations.HSQLDBInsertData;
import com.test.fileoperations.FileParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.apache.log4j.Logger.getLogger;

public class Base {

    public static Properties properties;
    public static FileReader file = null;
    public static String PROPERTYFILEPATH = System.getProperty("user.dir") + "//PropertyFiles//Property.properties";
    public FileParser parser = null;
    public HSQLDBInsertData insert;

    public static Logger log = getLogger(Base.class.getName());


/*    static {
        Logger log = getLogger(Base.class.getName());
        String log4jConfPath = System.getProperty("user.dir")+"//PropertyFiles//log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }*/

    public Base() {
        String log4jConfPath = System.getProperty("user.dir")+"//PropertyFiles//log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        this.parser = new FileParser();
        this.insert = new HSQLDBInsertData();
    }

    public static String getProperty(String propertyName) throws IOException {
        properties = new Properties();
        file = new FileReader(PROPERTYFILEPATH);
        properties.load(file);
        return properties.getProperty(propertyName);
    }

    public static String getLogFilePath() {
        String PATH=null;
        try{
            PATH = System.getProperty("user.dir") + getProperty("PATH");
        }catch (Exception e){
            e.printStackTrace();
        }
        return PATH;
    }

}
