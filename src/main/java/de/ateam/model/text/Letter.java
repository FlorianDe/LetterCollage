package main.java.de.ateam.model.text;

import main.java.de.ateam.utils.OpenCVUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class Letter{
    private Mat letterMask;
    private int width;
    private int height;
    private char symbol;

    protected Letter (BufferedImage map,char symbol) {
        this.width = map.getWidth();
        this.height = map.getHeight();
        this.symbol = symbol;
        init(map);
    }

    private void init(BufferedImage map) {
        this.letterMask = OpenCVUtils.bufferedImageToMat(map);
        Imgproc.cvtColor(this.letterMask, this.letterMask, Imgproc.COLOR_BGR2GRAY);
    }

    public char getSymbol() {
        return symbol;
    }

    public Mat getLetterMask() {
        return letterMask;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
