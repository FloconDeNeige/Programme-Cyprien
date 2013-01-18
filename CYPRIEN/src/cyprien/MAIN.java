/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cyprien;

import gui.MainWindowModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author satanael
 */
public class MAIN extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MainWindowModel mwm = new MainWindowModel(primaryStage);
        
        System.out.println(System.getProperty("user.home"));
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
