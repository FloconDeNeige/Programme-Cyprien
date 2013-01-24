/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.derivative;

import java.util.Random;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author BLEIBER
 */
public class MyImageView extends ImageView {

    private StackPane sp = new StackPane();
    private final int ABSOLUTE_HEIGHT = 500;
    private final int ABSOLUTE_WIDTH = 500;
    private final double MAX_RATIO = 4;
    private int width, height;
    private int viewportWidth, viewportHeight;
    private int TEXT_SPACING_VERTICAL = 70;
    private int TEXT_SPACING_HORIZONTAL = 110;
    private Random random = null;
    private double zoomLevel = 1;
    private Coordinate center = new Coordinate();
    private DragData dragData = null;
    private boolean drag = false;
    private double innateZoom = 1;

    public MyImageView() {
        super();
        sp.setPrefSize(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);
        sp.setMaxSize(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);
        sp.setMinSize(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);
        sp.setAlignment(Pos.CENTER);
        width = ABSOLUTE_WIDTH;
        height = ABSOLUTE_HEIGHT;
        random = new Random(System.currentTimeMillis());
        this.setViewport(new Rectangle2D(0, 0, ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT));
        this.setImage(getBackgroundImage());
        center.assign(ABSOLUTE_WIDTH / 2, ABSOLUTE_HEIGHT / 2);
        this.setPreserveRatio(true);
        this.setSmooth(true);
        this.setCache(true);
        sp.getChildren().add(this);
    }
    
    public StackPane getPane() {
        return sp;
    }

    public Image getBackgroundImage() {
        WritableImage wim = new WritableImage(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);
        Canvas canvas = new Canvas(ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGREY);
        gc.setStroke(Color.GREY);
        gc.fillRect(0, 0, ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT);
        int wOffset = -random.nextInt(TEXT_SPACING_HORIZONTAL);
        int hOffset = -random.nextInt(TEXT_SPACING_VERTICAL);
        for (int i = hOffset; i < ABSOLUTE_HEIGHT; i += TEXT_SPACING_VERTICAL) {
            for (int j = wOffset; j < ABSOLUTE_WIDTH; j += TEXT_SPACING_HORIZONTAL) {
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
        int height = (int) this.getViewport().getHeight();
        return new WritableImage(this.getImage().getPixelReader(), x, y, width, height);
    }
    
    public void loadImage(Image img) {
        this.setImage(img);
        double imgWidth = img.getWidth();
        double imgHeight = img.getHeight();
        width = ABSOLUTE_WIDTH;
        height = ABSOLUTE_HEIGHT;
        double rat = Math.max(width/imgWidth,height/imgHeight);
        if(rat>1) {
            innateZoom = rat;
        }
        adjustZoom(1);
        adjustRatio(0.5);
    }

    public void adjustZoom(double d) {
        zoomLevel = 1 / d;
        computeViewport();
        normalizeCenter();
        render();
    }
    
    public void adjustRatio(double d) {
        if(d>0.5) {
            height = ABSOLUTE_HEIGHT;
            width = (int) (height*(MAX_RATIO/(Math.pow(MAX_RATIO, 2*d))));
        } else {
            width = ABSOLUTE_WIDTH;
            height = (int) (width/(MAX_RATIO/(Math.pow(MAX_RATIO, 2*d))));
        }
        computeViewport();
        normalizeCenter();
        render();
    }
    
    private void computeViewport() {
        viewportWidth = (int) (width/zoom());
        viewportHeight = (int) (height/zoom());
    }
    
    private void normalizeCenter() {
        int halfWidth = viewportWidth/2;
        int halfHeight = viewportHeight/2;
        int imageWidth = (int) this.getImage().getWidth();
        int imageHeight = (int) this.getImage().getHeight();
        if((center.x-halfWidth)<0) {
            center.x = halfWidth;
        } else if((center.x+halfWidth)>imageWidth) {
            center.x = imageWidth-halfWidth;
        }
        if((center.y-halfHeight)<0) {
            center.y = halfHeight;
        } else if((center.y+halfHeight)>imageHeight) {
            center.y = imageHeight-halfHeight;
        }
    }
    
    public void render() {
        Rectangle2D rect = new Rectangle2D(center.x - (viewportWidth / 2), center.y - (viewportHeight / 2), viewportWidth, viewportHeight);
        this.setViewport(rect);
        this.setFitHeight(height);
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

    private double zoom() {
        return zoomLevel*innateZoom;
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
            return new Coordinate(((mouse_x-x)/zoom())+base_x, ((mouse_y-y)/zoom())+base_y);
        }
    }
}
