/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import conf.ConfGetter;
import conf.ConfSetter;
import gui.derivative.ElemFactory;
import gui.derivative.MyImageView;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javax.imageio.ImageIO;
import name.antonsmirnov.javafx.dialog.Dialog;
import stamp.Stamp;

/**
 *
 * @author satanael
 */
public class MainWindowModel {

    private Stage stage = null;
    private Scene scene = null;
    private final VBox vBox = new VBox();
    private final VBox zoomSliderVBox = new VBox();
    private final HBox imageHBox = new HBox();
    private final HBox ratioSliderHBox = new HBox();
    private final HBox buttonHBox = new HBox();
    private final MenuBar menuBar = new MenuBar();
    private final Menu settingsMenu = new Menu("Settings");
    private final MyImageView imageView = new MyImageView();
    private final Slider zoomSlider = new Slider(100, 400, 100);
    private final Slider ratioSlider = new Slider(0, 1, 0.5);
    private TextField textField = new TextField();
    private Button newButton = new Button("NEW");
    private Button okButton = new Button("OK");

    public MainWindowModel(Stage stage) {
        final Stage primeStage = this.stage = stage;
        Insets insets = new Insets(10, 10, 10, 10);
        Insets insideInsets = new Insets(10, 0, 10, 0);
        Insets verticalInsets = new Insets(0, 10, 0, 10);

        /*
         * Setting the menu
         */

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

        /*
         * Setting zoomSliderVBox
         */

        zoomSlider.setBlockIncrement(0.005);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number _old, Number _new) {
                updateImageViewZoom(_new);
            }
        });
        Label zoomLabel = new Label("Zoom");
        //zoomLabel.getFont().
        zoomSlider.setOrientation(Orientation.VERTICAL);
        zoomSliderVBox.setAlignment(Pos.CENTER);
        zoomSliderVBox.getChildren().addAll(zoomLabel, zoomSlider);

        /*
         * Setting imageHBox
         */

        imageView.setGrab();
        HBox.setMargin(imageView.getPane(), insets);
        HBox.setMargin(zoomSliderVBox, insets);
        imageHBox.setAlignment(Pos.CENTER);
        imageHBox.getChildren().addAll(imageView.getPane(), zoomSliderVBox);

        /*
         * Setting ratioSliderHBox
         */

        ratioSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                imageView.adjustRatio((double) t1);
            }
        });
        Label ratioLabel = new Label("Ratio");
        // setting the font correctly....
        ratioSlider.setOrientation(Orientation.HORIZONTAL);
        ratioSliderHBox.setAlignment(Pos.CENTER);
        HBox.setMargin(ratioLabel, verticalInsets);
        ratioSliderHBox.getChildren().addAll(ratioLabel, ratioSlider);

        /*
         * Setting buttonHBox
         */

        newButton.addEventHandler(ActionEvent.ACTION, new EventHandler() {
            @Override
            public void handle(Event t) {
                Image image = null;
                try {
                    image = ElemFactory.imageChooser(primeStage);
                } catch (FileNotFoundException ex) {
                    Dialog.showError("Image invalide", "Image non trouvée. Assurez vous qu'il s'agisse bien d'une image, et que vous ayez les droits d'accès.");
                }
                if (image != null) {
                    imageView.loadImage(image);
                }
            }
        });
        okButton.addEventHandler(ActionEvent.ACTION, new EventHandler() {
            @Override
            public void handle(Event t) {
                if (!"".equals(textField.getText())) {
                    File file = new File(ConfGetter.saveDirectory(), textField.getText() + ".png");
                    try {
                        new Stamp(imageView.getCroppedImage(), textField.getText()).writeToDisk(file);
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindowModel.class.getName()).log(Level.SEVERE, null, ex);
                        Dialog.showError("Erreur", "Impossible d'écrire l'image générée sur le disque.");
                    }
                } else {
                    System.out.println("textfield vide");
                }
            }
        });
        HBox.setMargin(newButton, insets);
        HBox.setMargin(okButton, insets);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.getChildren().addAll(newButton, okButton);
        VBox.setMargin(imageView, insets);
        VBox.setMargin(textField, insets);
        vBox.getChildren().addAll(menuBar, imageHBox, ratioSliderHBox, textField, buttonHBox);
        scene = ElemFactory.scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }

    public void updateImageViewZoom(Number nb) {
        double prc = (double) nb;
        double zoom = 1 / (prc / 100);
        imageView.adjustZoom(zoom);
    }
}
