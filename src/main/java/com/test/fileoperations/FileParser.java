package com.test.fileoperations;

import com.test.base.Base;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Read and Add Data to ArrayList
 */
public class FileParser{

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FileParser.class.getName());
    static {
        String log4jConfPath = System.getProperty("user.dir")+"//PropertyFiles//log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }

    private FileInputStream inputStream = null;
    private Scanner scanner = null;
    private List<JSONObject> jsonArray = null;
    private String PATH = null;

    /**
     * Method to Parse Text File
     *
     * @param PATH Log File Path
     * @return Array List of JSON Objects (Read from File)
     * @throws IOException
     * @throws ParseException
     */
    public List<JSONObject> parseInputFile(String PATH)  {
        try {

            log.info("File Parsing Started for File: "+ PATH);

            inputStream = new FileInputStream(PATH);
            scanner = new Scanner(inputStream, "UTF-8");
            jsonArray = new ArrayList<JSONObject>();
            while (scanner.hasNext()) {
                JSONObject obj = (JSONObject) new JSONParser().parse(scanner.nextLine());
                jsonArray.add(obj);
            }
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (scanner != null) {
                scanner.close();
            }
        }

        log.info("File Parsing Completed for File: "+ PATH);

        return jsonArray;
    }

    public long countLineFromFile(String fileName) {

        Path path = Paths.get(fileName);
        long lines = 0;
        try {
            // much slower, this task better with sequence access
            //lines = Files.lines(path).parallel().count();
            lines = Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static String getTheJsonFilePath() {

        boolean isInputValid = false;
        String userInput = "";
        System.out.print("Do you want to specify the .json file? Select -> (Y/N) ");
        Scanner scanner = new Scanner(System.in);
        String userAnswer = scanner.nextLine();

        while (isInputValid == false) {
            switch (userAnswer.toLowerCase()) {
                case "y": {
                    System.out.print("Please provide the file path: ");
                    userInput = scanner.nextLine();
                    isInputValid = true;
                    break;
                }
                case "n": {
                    System.out.print("Default file path has been applied\n" );
                    userInput = Base.getLogFilePath();
                    isInputValid = true;
                    break;
                }
                default:
                    System.out.println("Inputted incorrect value.\nPlease try again:");
                    userAnswer = scanner.nextLine();
                    break;
            }
        }
        return userInput;
    }
}
