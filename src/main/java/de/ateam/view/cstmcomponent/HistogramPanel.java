package main.java.de.ateam.view.cstmcomponent;

import main.java.de.ateam.model.extra.HistogramModel;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;
import main.java.de.ateam.utils.OpenCVUtils;
import main.java.de.ateam.utils.ShapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Florian on 04.02.2016.
 */
public class HistogramPanel extends JPanel implements CstmObserver{

    BufferedImage buf;
    RenderingHints rh;
    HistogramModel model;

    public HistogramPanel(HistogramModel model){
        this.model = model;
        model.addObserver(this);
        setPaintSettings();
        drawBufferedImage();
    }

    private void setPaintSettings(){
        rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        rh.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHints(rh);

        g2d.drawImage(buf, 0, 0, this.getWidth(),this.getHeight(), null);

        ShapeUtils.setTransparency(g2d,0.75f);
        g2d.fillRect(0,0, (int)(((double)this.getWidth()/this.model.getxValues().length)*this.model.getLeftSlider()),this.getHeight());
        g2d.fillRect((int)(((double)this.getWidth()/this.model.getxValues().length)*this.model.getRightSlider()),0, this.getWidth(),this.getHeight());
        ShapeUtils.setTransparency(g2d,1.0f);


        g2d.dispose();
    }

    public void drawBufferedImage(){
        buf = new BufferedImage(this.model.getxValues().length,this.model.getMaxIntensity(),BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D buf_g2d = buf.createGraphics();
        buf_g2d.setColor(Color.WHITE);
        buf_g2d.fillRect(0,0,buf.getWidth(),buf.getHeight());
        buf_g2d.setColor(Color.BLACK);
        buf_g2d.setRenderingHints(rh);

        for (int i = 0; i < this.model.getxValues().length; i++) {
            int posX = this.model.getxValues().length-i;
            buf_g2d.drawLine(posX, this.model.getMaxIntensity(), posX , this.model.getMaxIntensity()-this.model.getIntensities()[i]);
        }
        /*buf_g2d.drawPolygon(this.model.getxValues(),this.model.getIntensities(),this.model.getxValues().length);
        buf_g2d.fillPolygon(this.model.getxValues(),this.model.getIntensities(),this.model.getxValues().length);
        buf_g2d.fillPolygon(new int[]{0,0,this.model.getxValues().length, this.model.getxValues().length},new int[]{0,this.model.getIntensities()[0],this.model.getIntensities()[this.model.getxValues().length-1],0},4);

        */
        /*AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -buf.getHeight()));
        buf_g2d.transform(at);
        */

        /*
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-buf.getWidth(null), -buf.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        buf = op.filter(buf, null);
        */

        buf_g2d.dispose();
    }

    @Override
    public void update(CstmObservable o, Object arg) {
        drawBufferedImage();
        this.revalidate();
        this.repaint();
    }
}
