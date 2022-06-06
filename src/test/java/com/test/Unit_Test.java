package com.test;


import com.test.base.Base;
import com.test.dataprocessing.DataProcessor;
import com.test.dboperations.HSQLSelectData;
import com.test.fileoperations.FileParser;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Unit_Test {

    private FileParser parser;
    private DataProcessor dataProcessor;
    private HSQLSelectData selectData;

    public List<JSONObject> objects;

    public Unit_Test(){
        this.parser = new FileParser();
        this.dataProcessor = new DataProcessor();
        this.selectData = new HSQLSelectData();
    }

    @Test
    public void parseDataTest(){
        this.objects = this.parser.parseInputFile(Base.getLogFilePath());
        long recordCount = this.parser.countLineFromFile(Base.getLogFilePath());
        long parsedDataCount = this.objects.size();
        Assertions.assertEquals(recordCount, parsedDataCount);
    }

    @Test
    public void calculateDurationTest(){
        this.objects = this.parser.parseInputFile(System.getProperty("user.dir")+ "//src//test//resources//TestData//logfile.txt");
        long duration = this.dataProcessor.calculateDuration(objects,"scsmbstgra" );
        Assertions.assertEquals(duration, 5);
    }

    @Test
    public void calculateNegativeDurationTest(){
        this.objects = this.parser.parseInputFile(System.getProperty("user.dir")+ "//src//test//resources//TestData//logfile4.txt");
        long duration = this.dataProcessor.calculateDuration(objects,"scsmbstgrb" );
        Assertions.assertEquals(duration, -1);
    }

}
