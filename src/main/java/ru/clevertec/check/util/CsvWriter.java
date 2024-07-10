package ru.clevertec.check.util;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {

    public void writeToCsv(String text, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(text);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
