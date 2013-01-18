/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 *
 * @author BLEIBER
 */
class PropHandle {

    static Properties prop = null;
    static Path path = null;

    static Properties getProp() {
        if (prop == null) {
            prop = new Properties();
        }
        return prop;
    }

    static Path getPath() {
        if (path == null) {
            path = FileSystems.getDefault().getPath("prop");
        }
        return path;
    }

    static void reset() throws FileNotFoundException, IOException {
        getPath();
        getProp();
        OutputStream os = new FileOutputStream(path.toString());
        prop.put("saveDirectory", System.getProperty("user.home"));
        prop.put("searchDirectory", System.getProperty("user.home"));
        prop.store(os, "default values");
        os.close();
    }
}
