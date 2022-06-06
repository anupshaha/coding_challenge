package com.test;

import com.test.base.Base;
import com.test.dataprocessing.DataProcessor;
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
        List<JSONObject> objects = parser.parseInputFile(getLogFilePath());
        dataProcessor.ProcessAndInsertDataFromJsonObjects(objects);
    }
}
