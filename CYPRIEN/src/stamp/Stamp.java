/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamp;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

/**
 *
 * @author BLEIBER
 */
public class Stamp {
    
    private static Color borderColor = Color.BLACK;
    private static Color captionColor = Color.DARKGRAY;
    private static int borderThickness = 10;
    private static double ratio = 0.75;
    private static int height = 400;
    private WritableImage stampImage;
    private Canvas canvas;
    private GraphicsContext gc;
    
    public Stamp() {
        System.out.println("width : " + width() + "  height : " + height);
        stampImage = new WritableImage(width(), height);
        canvas = new Canvas(width(), height);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(borderColor);
    }
    
    public Stamp(Image picture, String caption) {
        this();
        build(picture, caption);
    }
    
    public void build(Image picture, String caption) {
        gc.fillRect(0,0,width(),borderThickness);
        gc.fillRect(0,borderThickness, borderThickness, height-(2*borderThickness));
        gc.fillRect(0,height-borderThickness, width(), borderThickness);
        gc.fillRect(width()-borderThickness, borderThickness, borderThickness, height-(2*borderThickness));
        Rectangle2D pictureRect = new Rectangle2D(
                (double)borderThickness,
                (double)borderThickness,
                (double)(width()-(2*borderThickness)),
                (height-(2*borderThickness))*2/3);
        Rectangle2D captionRect = new Rectangle2D(
                (double)borderThickness,
                (borderThickness+(height-(2*borderThickness))*2/3),
                (double)(width()-(2*borderThickness)),
                (height-(2*borderThickness))*1/3);
        int imgHeight, imgWidth, x = (int) pictureRect.getMinX(), y = (int) pictureRect.getMinY();
        double imgRatio = picture.getWidth()/picture.getHeight();
        double pictRatio = pictureRect.getWidth()/pictureRect.getHeight();
        double factor;
        if(imgRatio>pictRatio) {
            imgWidth = (int) pictureRect.getWidth();
            imgHeight = (int) (pictureRect.getHeight()*(picture.getHeight()/picture.getWidth()));
            y += ((pictureRect.getHeight()/2)-(imgHeight/2.0));
            System.out.println("changing y");
        } else {
            imgHeight = (int) pictureRect.getHeight();
            imgWidth = (int) (pictureRect.getWidth()*(picture.getWidth()/picture.getHeight()));
            x += ((pictureRect.getWidth()/2)-(imgWidth/2.0));
        }
        gc.drawImage(picture, x, y, imgWidth, imgHeight);
        
        canvas.snapshot(null, stampImage);
    }
    
    public final int width() {
        return (int) (height*ratio);
    }
    
    public void writeToDisk(File file) throws IOException {
        ImageIO.write(SwingFXUtils.fromFXImage(stampImage, null), "png", file);
    }
    
    public static void setBorderColor(Color clr) {
        borderColor = clr;
    }
    
    public static void setCaptionColor(Color clr) {
        captionColor = clr;
    }
    
    public static void setThickness(int thck) {
        borderThickness = thck;
    }
}
