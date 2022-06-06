package com.test;

import com.test.base.Base;
import com.test.dataprocessing.DataProcessor;
import com.test.fileoperations.FileParser;
import org.json.simple.JSONObject;

import java.util.List;

public class Runner extends Base {

    public DataProcessor dataProcessor = null;

    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.processLogData();
    }

    public void processLogData() {
        this.dataProcessor = new DataProcessor();
        String PATH= FileParser.getTheJsonFilePath();
        List<JSONObject> objects = parser.parseInputFile(PATH);
        dataProcessor.ProcessAndInsertDataFromJsonObjects(objects);
    }
}
