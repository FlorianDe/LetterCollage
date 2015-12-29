package main.java.de.ateam.model.text;

import main.java.de.ateam.utils.OpenCVUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class Letter{
    private Mat resultMask;
    private Mat calculationMask;
    //private int width;
    //private int height;
    private char symbol;

    protected Letter (char symbol, BufferedImage bi_resultMask,  BufferedImage bi_calculationMask) {
        //this.width = map.getWidth();
        //this.height = map.getHeight();
        this.symbol = symbol;
        this.resultMask = OpenCVUtils.bufferedImageToMat(bi_resultMask);
        this.calculationMask = OpenCVUtils.bufferedImageToMat(bi_calculationMask);
        Imgproc.cvtColor(this.resultMask, this.resultMask, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(this.calculationMask, this.calculationMask, Imgproc.COLOR_BGR2GRAY);
        //init(map);
    }

    /*
    private void init(BufferedImage map) {
        //TODO mir erkl�ren :D! ---> EY AUF JEDEN FALL ANDERS MACHEN!!!! AM BESTEN BEIDE BUCHSTABEN MIT 2 GRÖ?EN ZEICHNEN NICHT RUNTERSKALIEREN!!!
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
        this.resultMask = OpenCVUtils.bufferedImageToMat(map);
        this.calculationMask = new Mat(sz, CvType.CV_8UC3);

        Imgproc.resize(this.resultMask, this.calculationMask, sz);
        Imgproc.cvtColor(this.resultMask, this.resultMask, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(this.calculationMask, this.calculationMask, Imgproc.COLOR_BGR2GRAY);
    }
    */

    public char getSymbol() {
        return symbol;
    }

    public Mat getCalculationMask() { return calculationMask; }

    public Mat getResultMask() {
        return resultMask;
    }

    /*
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    */
}
