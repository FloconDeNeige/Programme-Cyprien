/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import conf.ConfGetter;
import conf.ConfSetter;
import gui.derivative.ElemFactory;
import gui.derivative.MyImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import name.antonsmirnov.javafx.dialog.Dialog;

/**
 *
 * @author satanael
 */
public class MainWindowModel {
    
    private Stage stage = null;
    private Scene scene = null;
    private final VBox vBox = new VBox();
    private final VBox sliderVBox = new VBox();
    private final Button zoomin = new Button("+");
    private final Button zoomout = new Button("-");
    private final HBox hBox1 = new HBox();
    private final HBox hBox2 = new HBox();
    private final MenuBar menuBar = new MenuBar();
    private final Menu settingsMenu = new Menu("Settings");
    private final MyImageView imageView = new MyImageView();
    private final Slider slider = new Slider(100,400,100);
    private TextField textField = new TextField();
    private Button newButton = new Button("NEW");
    private Button okButton = new Button("OK");
    
    public MainWindowModel(Stage stage) {
        final Stage primeStage = this.stage = stage;
        Insets insets = new Insets(10,10,10,10);
        Insets insideInsets = new Insets(10,0,10,0);
        MenuItem save = new MenuItem("save");
        MenuItem search = new MenuItem("search");
        save.addEventHandler(ActionEvent.ANY, new EventHandler() {

            @Override
            public void handle(Event t) {
                ElemFactory.saveDirectoryChooser(primeStage);
            }
        });
        search.addEventHandler(ActionEvent.ANY, new EventHandler() {

            @Override
            public void handle(Event t) {
                ElemFactory.searchDirectoryChooser(primeStage);
            }
        });
        settingsMenu.getItems().addAll(save, search);
        menuBar.getMenus().add(settingsMenu);
        
        slider.setBlockIncrement(0.005);
        zoomin.addEventHandler(ActionEvent.ACTION, new EventHandler() {

            @Override
            public void handle(Event t) {
                slider.increment();
            }
            
        });
        zoomout.addEventHandler(ActionEvent.ACTION, new EventHandler() {

            @Override
            public void handle(Event t) {
                slider.decrement();
            }
            
        });
        
        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number _old, Number _new) {
                updateImageViewZoom(_new);
            }
        
    });
        slider.setOrientation(Orientation.VERTICAL);
        VBox.setMargin(zoomin, insideInsets);
        VBox.setMargin(zoomout, insideInsets);
        sliderVBox.setAlignment(Pos.CENTER);
        sliderVBox.getChildren().addAll(zoomin, slider, zoomout);
        
        imageView.setGrab();
        HBox.setMargin(imageView, insets);
        HBox.setMargin(sliderVBox, insets);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(imageView, sliderVBox);
        
        
        newButton.addEventHandler(ActionEvent.ACTION, new EventHandler() {

            @Override
            public void handle(Event t) {
                Image image = null;
                try {
                    image = ElemFactory.imageChooser(primeStage);
                } catch (FileNotFoundException ex) {
                    Dialog.showError("Image invalide","Image non trouvée. Assurez vous qu'il s'agisse bien d'une image, et que vous ayez les droits d'accès.");
                }
                if(image!=null) {
                    imageView.setImage(image);
                }
            }
            
        });
        HBox.setMargin(newButton, insets);
        HBox.setMargin(okButton, insets);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(newButton, okButton);
        VBox.setMargin(imageView, insets);
        VBox.setMargin(textField, insets);
        vBox.getChildren().addAll(menuBar, hBox1, textField, hBox2);
        scene = ElemFactory.scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void updateImageViewZoom(Number nb) {
        double prc = (double)nb;
        double zoom = 1/(prc/100);
        imageView.adjustZoom(zoom);
    }
    
}
