/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.derivative;

import java.util.Random;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author BLEIBER
 */
public class MyImageView extends ImageView {
    
    private MyImageView self = null;
    private final static int HEIGHT = 400;
    private final static int WIDTH = 400;
    private final static int TEXT_SPACING_VERTICAL = 70;
    private final static int TEXT_SPACING_HORIZONTAL = 110;
    private Random random = null;
    private double zoomLevel = 1;
    private Coordinate center = new Coordinate();
    private boolean drag = false;
    
    public MyImageView() {
        super();
        random = new Random(System.currentTimeMillis());
        this.setViewport(new Rectangle2D(0,0,WIDTH, HEIGHT));
        this.setImage(getBackgroundImage());
        center.assign(WIDTH/2, HEIGHT/2);
        this.setPreserveRatio(true);
        this.setSmooth(true);
        this.setCache(true);
    }
    
    public Image getBackgroundImage() {
        WritableImage wim = new WritableImage(WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGREY);
        gc.setStroke(Color.GREY);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        int wOffset = -random.nextInt(TEXT_SPACING_HORIZONTAL);
        int hOffset = -random.nextInt(TEXT_SPACING_VERTICAL);
        for(int i = hOffset;i<HEIGHT;i+=TEXT_SPACING_VERTICAL) {
            for(int j = wOffset;j<WIDTH;j+=TEXT_SPACING_HORIZONTAL) {
                System.out.println("writing at coordinates " + j + " & " + i);
                gc.strokeText("no image loaded", j, i);
            }
        }
        canvas.snapshot(null, wim);
        return wim;
    }
    
    public Image getCroppedImage() {
        int x = (int) this.getViewport().getMinX();
        int y = (int) this.getViewport().getMinY();
        int width = (int) this.getViewport().getWidth();
        if(width+x > this.getImage().getWidth()) {
            width = (int) (this.getImage().getWidth() - x);
        }
        int height = (int) this.getViewport().getHeight();
        if(height+y > this.getImage().getHeight()) {
            height = (int) (this.getImage().getHeight() - y);
        }
        return new WritableImage(this.getImage().getPixelReader(), x, y, width, height);
    }

    public void adjustZoom(double d) {
        double width = d*WIDTH;
        double height = d*HEIGHT;
        Rectangle2D rect = new Rectangle2D(center.x-(width/2), center.y-(height/2), width, height);
        zoomLevel = d;
        this.setViewport(rect);
        this.setFitHeight(HEIGHT);
    }
    
    public void setGrab() {
        this.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event t) {
                if(!drag) {
                    openHand();
                }
                t.consume();
            }
        });
        this.setOnMouseExited(new EventHandler() {
            @Override
            public void handle(Event t) {
                if(!drag) {
                    pointer();
                }
                t.consume();
            }
        });
        this.setOnMousePressed(new EventHandler() {
            @Override
            public void handle(Event t) {
                drag = true;
                closedHand();
                t.consume();
            }
        });
        this.setOnMouseDragged(new EventHandler() {
            @Override
            public void handle(Event t) {
                System.out.println("dragging");
                t.consume();
            }
        });
        this.setOnMouseReleased(new EventHandler() {
            @Override
            public void handle(Event t) {
                System.out.println("drag over");
                drag = false;
                pointer();
                t.consume();
            }
        });
    }
    
    public void openHand() {
        this.setCursor(Cursor.OPEN_HAND);
    }
    
    public void closedHand() {
        this.setCursor(Cursor.CLOSED_HAND);
    }
    
    public void pointer() {
        this.setCursor(Cursor.DEFAULT);
    }
    
    
    
    public class Coordinate {
        public int x, y;
        
        public Coordinate() {}
        
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void assign(int a, int b) {
            x = a;
            y = b;
        }
    }
}
