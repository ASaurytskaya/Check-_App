package ru.clevertec.check.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CsvWriterTest {
    @TempDir
    Path tempDir;

    private CsvWriter csvWriter;

    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriter();
    }

    @Test
    void testWriteToCsv_Success() throws IOException {
        String text = "Date;Time\n10.07.2024;14:30:00";
        String filename = tempDir.resolve("test.csv").toString();

        csvWriter.writeToCsv(text, filename);

        File file = new File(filename);
        assertTrue(file.exists());

        String content = Files.readString(file.toPath());
        assertEquals(text, content);
    }
}
