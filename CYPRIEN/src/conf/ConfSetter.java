/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author satanael
 */
public class ConfSetter {

    private Properties prop = new Properties();
    private Path path = FileSystems.getDefault().getPath(null, "prop");

    public void searchDirectory(String dir) throws IOException {
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            prop.load(reader);
            reader.close();
            prop.setProperty("searchDirectory", dir);
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            prop.store(writer, null);
            writer.close();
        } catch (IOException ex) {
            System.out.println("IOException occured!");
            throw new RuntimeException();
        }
    }

    public void saveDirectory(String dir) {
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            prop.load(reader);
            reader.close();
            prop.setProperty("saveDirectory", dir);
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            prop.store(writer, null);
            writer.close();
        } catch (IOException ex) {
            System.out.println("IOException occured!");
            throw new RuntimeException();
        }
    }
}
