package ru.clevertec.check.util;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {

    private static final String FILENAME = "./src/main/resources/result.csv";

    public void writeToCsv(String text) {
        writeToCsv(text, FILENAME);
    }

    public void writeToCsv(String text, String filename) {
        if(filename == null) filename = FILENAME;

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(text);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
