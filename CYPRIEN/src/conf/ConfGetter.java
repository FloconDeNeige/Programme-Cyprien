/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
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
public class ConfGetter {
    
    private Properties prop = new Properties();
    private Path path = FileSystems.getDefault().getPath(null, "prop");
    
    public Path searchDirectory() throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            prop.load(reader);
            String result = prop.getProperty("searchDirectory");
            return Paths.get(result);
        } catch (IOException ex) {
            System.out.println("IOException occured!");
            throw new RuntimeException();
        }
    }
    
    public Path saveDirectory() {
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            prop.load(reader);
            String result = prop.getProperty("saveDirectory");
            return Paths.get(result);
        } catch (IOException ex) {
            System.out.println("IOException occured!");
            throw new RuntimeException();
        }
    }
    
}
