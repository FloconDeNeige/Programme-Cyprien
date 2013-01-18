/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import conf.ConfGetter;
import conf.ConfSetter;
import gui.derivative.ElemFactory;
import java.io.File;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author satanael
 */
public class MainWindowModel {
    
    private Stage stage = null;
    private Scene scene = null;
    private VBox vBox = new VBox();
    private HBox hBox = new HBox();
    private MenuBar menuBar = new MenuBar();
    private Menu settingsMenu = new Menu("Settings");
    private ImageView imageView = new ImageView();
    private Image image = null;
    private TextField textField = new TextField();
    private Button newButton = new Button("NEW");
    private Button okButton = new Button("OK");
    
    public MainWindowModel(Stage stage) {
        final Stage primeStage = this.stage = stage;
        Insets insets = new Insets(10,10,10,10);
        MenuItem save = new MenuItem("save");
        MenuItem search = new MenuItem("search");
        save.addEventHandler(ActionEvent.ANY, new EventHandler() {

            @Override
            public void handle(Event t) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setInitialDirectory(new File(ConfGetter.saveDirectory()));
                File chosenDir = dc.showDialog(primeStage);
                ConfSetter.saveDirectory(chosenDir.getAbsolutePath());
            }
            
        });
        settingsMenu.getItems().addAll(save, search);
        menuBar.getMenus().add(settingsMenu);
        HBox.setMargin(newButton, insets);
        HBox.setMargin(okButton, insets);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(newButton, okButton);
        VBox.setMargin(imageView, insets);
        VBox.setMargin(textField, insets);
        vBox.getChildren().addAll(menuBar, imageView, textField, hBox);
        scene = ElemFactory.scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void LoadImage(Image i) {
        imageView.setImage(i);
    }
    
    public void zoomUp() {
        
    }
    
    public void zoomDown() {
        
    }
    
}
