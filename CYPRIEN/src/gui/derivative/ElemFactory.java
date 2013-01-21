/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.derivative;

import conf.ConfGetter;
import conf.ConfSetter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author satanael
 */
public class ElemFactory {

    public static Scene scene(Parent root) {
        return new Scene(root);
    }

    public static void searchDirectoryChooser(Stage stage) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(ConfGetter.searchDirectory()));
        File chosenDir = dc.showDialog(stage);
        if (chosenDir != null) {
            ConfSetter.searchDirectory(chosenDir.getAbsolutePath());
        }
    }
    
    public static void saveDirectoryChooser(Stage stage) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(ConfGetter.saveDirectory()));
        File chosenDir = dc.showDialog(stage);
        if (chosenDir != null) {
            ConfSetter.saveDirectory(chosenDir.getAbsolutePath());
        }
    }
    
    public static Image imageChooser(Stage stage) throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(ConfGetter.searchDirectory()));
        File chosenFile = fc.showOpenDialog(stage);
        if(chosenFile != null) {
            return new Image(new FileInputStream(chosenFile.getPath()));
        } else {
            return null;
        }
    }
}
