/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.derivative;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

/**
 *
 * @author BLEIBER
 */
public class DummyPixelReader implements PixelReader {

    @Override
    public PixelFormat getPixelFormat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getArgb(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color getColor(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Buffer> void getPixels(int i, int i1, int i2, int i3, WritablePixelFormat<T> wpf, T t, int i4) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getPixels(int i, int i1, int i2, int i3, WritablePixelFormat<ByteBuffer> wpf, byte[] bytes, int i4, int i5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getPixels(int i, int i1, int i2, int i3, WritablePixelFormat<IntBuffer> wpf, int[] ints, int i4, int i5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
