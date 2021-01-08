package org.suai.server;

import java.io.FileWriter;
import java.io.IOException;

/**
 * создает текстовый файл и записывает в него все события
 */

public class LogsWriter {

    private FileWriter writer;

    public LogsWriter(String path) {

        try {
            writer = new FileWriter(path, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void append(String string) {
        try {
            writer.append(string + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
