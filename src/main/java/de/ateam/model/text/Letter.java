package main.java.de.ateam.model.text;

import main.java.de.ateam.utils.OpenCVUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class Letter{
    private Mat letterMask;
    private Mat calculationMask;
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
        //TODO mir erklären :D!
        float ratio = (float)map.getHeight() / (float)map.getWidth();
        int width = LetterCollection.SAMPLER_SIZE;
        int height = LetterCollection.SAMPLER_SIZE;

        if(ratio > 1){
            width = (int)((float)width / ratio);
        } else {
            height = (int)((float)height * ratio);
        }
        Size sz = new Size(width,height);
        //System.out.println("Size:" + sz);
        this.letterMask = OpenCVUtils.bufferedImageToMat(map);
        this.calculationMask = new Mat(sz, CvType.CV_8UC3);
        Imgproc.resize(this.letterMask, this.calculationMask, sz);
        Imgproc.cvtColor(this.letterMask, this.letterMask, Imgproc.COLOR_BGR2GRAY);
    }

    public char getSymbol() {
        return symbol;
    }

    public Mat getCalculationMask() { return calculationMask; }

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
