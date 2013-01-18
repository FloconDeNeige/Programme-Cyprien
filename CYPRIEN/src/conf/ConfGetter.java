/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author satanael
 */
public class ConfGetter {
    
    static Properties prop = PropHandle.getProp();
    static Path path = PropHandle.getPath();
    
    public static String searchDirectory() {
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            prop.load(reader);
            return prop.getProperty("searchDirectory");
        } catch (IOException ex) {
            try {
                PropHandle.reset();
                BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                prop.load(reader);
                return prop.getProperty("searchDirectory");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(ConfSetter.class.getName()).log(Level.SEVERE, null, ex1);
                throw new RuntimeException();
            } catch (IOException ex1) {
                Logger.getLogger(ConfSetter.class.getName()).log(Level.SEVERE, null, ex1);
                throw new RuntimeException();
            }
        }
    }
    
    public static String saveDirectory() {
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            prop.load(reader);
            return prop.getProperty("saveDirectory");
        } catch (IOException ex) {
            try {
                PropHandle.reset();
                BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                prop.load(reader);
                return prop.getProperty("saveDirectory");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(ConfSetter.class.getName()).log(Level.SEVERE, null, ex1);
                throw new RuntimeException();
            } catch (IOException ex1) {
                Logger.getLogger(ConfSetter.class.getName()).log(Level.SEVERE, null, ex1);
                throw new RuntimeException();
            }
        }
    }
    
}
