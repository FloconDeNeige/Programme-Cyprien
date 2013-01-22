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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author BLEIBER
 */
public class MyImageView extends ImageView {

    private int HEIGHT = 400;
    private int WIDTH = 340;
    private int TEXT_SPACING_VERTICAL = 70;
    private int TEXT_SPACING_HORIZONTAL = 110;
    private Random random = null;
    private double zoomLevel = 1;
    private Coordinate center = new Coordinate();
    private DragData dragData = null;
    private boolean drag = false;

    public MyImageView() {
        super();
        random = new Random(System.currentTimeMillis());
        this.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        this.setImage(getBackgroundImage());
        center.assign(WIDTH / 2, HEIGHT / 2);
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
        for (int i = hOffset; i < HEIGHT; i += TEXT_SPACING_VERTICAL) {
            for (int j = wOffset; j < WIDTH; j += TEXT_SPACING_HORIZONTAL) {
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
        if (width + x > this.getImage().getWidth()) {
            width = (int) (this.getImage().getWidth() - x);
        }
        int height = (int) this.getViewport().getHeight();
        if (height + y > this.getImage().getHeight()) {
            height = (int) (this.getImage().getHeight() - y);
        }
        return new WritableImage(this.getImage().getPixelReader(), x, y, width, height);
    }

    public void adjustZoom(double d) {
        double width = d * WIDTH;
        double height = d * HEIGHT;
        Rectangle2D rect = new Rectangle2D(center.x - (width / 2), center.y - (height / 2), width, height);
        zoomLevel = 1 / d;
        this.setViewport(rect);
        this.setFitHeight(HEIGHT);
    }
    
    public void computeCenter() {
        Rectangle2D rect = this.getViewport();
        center = new Coordinate((rect.getMinX()+rect.getMaxX())/2, (rect.getMinY()+rect.getMaxY())/2);
    }

    public void setGrab() {
        openHand();
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                startDrag(t.getScreenX(), t.getScreenY());
                closedHand();
                t.consume();
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                drag(dragData.updateDragPosition(t.getScreenX(), t.getScreenY()));
                t.consume();
            }
        });
        this.setOnMouseReleased(new EventHandler() {
            @Override
            public void handle(Event t) {
                stopDrag();
                openHand();
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

    public void startDrag(double x, double y) {
        drag = true;
        dragData = new DragData(this.getImage(), this.getViewport(), x, y);
    }

    public void stopDrag() {
        drag = false;
        dragData = null;
    }

    public void drag(Coordinate coord) {
        Rectangle2D rect = this.getViewport();
        this.setViewport(new Rectangle2D(Math.max(0, Math.min(dragData.maxX,coord.x)), Math.max(0, Math.min(dragData.maxY,coord.y)), rect.getWidth(), rect.getHeight()));
        computeCenter();
    }

    public class Coordinate {

        public double x, y;

        public Coordinate() {
        }

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void assign(double a, double b) {
            x = a;
            y = b;
        }
    }

    public class DragData {

        public double maxX, maxY;
        public double base_x, base_y;
        public double mouse_x, mouse_y;
        public double imageWidth, imageHeight;

        public DragData(Image image, Rectangle2D rect) {
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            maxX = imageWidth - rect.getWidth();
            maxY = imageHeight - rect.getHeight();
            base_x = rect.getMinX();
            base_y = rect.getMinY();
        }
        
        public DragData(Image image, Rectangle2D rect, double mx, double my) {
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            maxX = imageWidth - rect.getWidth();
            maxY = imageHeight - rect.getHeight();
            base_x = rect.getMinX();
            base_y = rect.getMinY();
            mouse_x = mx;
            mouse_y = my;
        }
        
        public Coordinate updateDragPosition(double x, double y) {
            return new Coordinate(((mouse_x-x)/zoomLevel)+base_x, ((mouse_y-y)/zoomLevel)+base_y);
        }
    }
}
