package de.ateam.model.extra;

import de.ateam.utils.CstmObservable;
import de.ateam.utils.FileLoader;
import de.ateam.utils.OpenCVUtils;
import de.ateam.view.cstmcomponent.HistogramPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

/**
 * Created by Florian on 04.02.2016.
 */
public class  HistogramModel extends CstmObservable {

    private int[] xValues;
    private int[] intensities;
    private int leftSlider;
    private int rightSlider;
    private int maxIntensity;

    public HistogramModel(BufferedImage img){
        BufferedImage buf;
        if(img.getType()!= BufferedImage.TYPE_BYTE_GRAY){
            buf = OpenCVUtils.copyImage(img, BufferedImage.TYPE_BYTE_GRAY);
        } else{
            buf = OpenCVUtils.copyImage(img);
        }
        this.intensities = calculateHistogram(buf);
        this.xValues = new int[this.intensities.length];
        for (int i = 0; i < xValues.length; i++) {
            this.xValues[i] = i;
            if(this.maxIntensity<this.intensities[i]){
                this.maxIntensity = this.intensities[i];
            }
        }

        //TODO REMOVE /2 and 10
        this.leftSlider = 10;
        this.rightSlider = (this.intensities.length-1)/2;
    }

    public int[] calculateHistogram(BufferedImage img){
        int depth = (int)Math.pow(2,img.getColorModel().getPixelSize());
        int[] t_intensitiesimg = new int[depth];
        byte[] pixels = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();

        int color = 0;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i]<0? (pixels[i]+depth) :pixels[i];
            t_intensitiesimg[color]++;
            pixels[i] = (byte)color;
        }

        return t_intensitiesimg;
    }


    public int[] getIntensities() {
        return intensities;
    }

    public int getLeftSlider() {
        return leftSlider;
    }

    public void setLeftSlider(int leftSlider) {
        if(this.rightSlider>leftSlider){
            this.leftSlider = leftSlider;
        }
        this.setChanged();
        this.notifyObservers(null);
    }

    public int getRightSlider() {
        return rightSlider;
    }

    public void setRightSlider(int rightSlider) {
        if(this.leftSlider<rightSlider){
            this.rightSlider = rightSlider;
        }
        this.setChanged();
        this.notifyObservers(null);
    }

    public int[] getxValues() {
        return xValues;
    }

    public int getMaxIntensity() {
        return maxIntensity;
    }

    public static void main(String[] args){
        try {
            BufferedImage img = ImageIO.read(FileLoader.loadFile("img/people/test3.jpg"));
            HistogramModel hm = new HistogramModel(img);
            System.out.println(hm.getIntensities().length);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame jframe = new JFrame("Florians eigenes Histogram sozusagen");
                    HistogramPanel hp = new HistogramPanel(hm);

                    jframe.add(hp);

                    jframe.setVisible(true);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
