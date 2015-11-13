package main.java.de.ateam.view.panel;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.resultImage.MouseScrollListener;
import main.java.de.ateam.controller.listener.resultImage.MouseWheelZoomListener;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVResultImagePanel extends JPanel implements CstmObserver, Scrollable {
    private final Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    ICollageController controller;

    public CVResultImagePanel(ICollageController controller){
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);


        MouseScrollListener mal = new MouseScrollListener(controller);
        this.addMouseListener(mal);
        this.addMouseMotionListener(mal);
        MouseWheelZoomListener mwzl = new MouseWheelZoomListener(controller);
        this.addMouseWheelListener(mwzl);
        this.setAutoscrolls(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHints(rh);

        if(this.controller.getResultImageModel().getMouseMode() == ResultImageModel.MouseMode.DRAG) {
            this.setCursor(handCursor);
        } else {
            this.setCursor(defaultCursor);
        }


        g2d.drawImage(this.controller.getResultImageModel().getResultImage(),
                0,0,
                (int)this.controller.getResultImageModel().getRenderSize().getWidth(),
                (int)this.controller.getResultImageModel().getRenderSize().getHeight(),
                null);

        g2d.dispose();
    }


    @Override
    public Dimension getPreferredSize() {
        ResultImageModel m = this.controller.getResultImageModel();
        if(m.getResultImage()==null)
            return new Dimension(0,0);
        return new Dimension(m.getRenderSize());
    }

    @Override
    public void update(CstmObservable o, Object arg) {
        this.scrollRectToVisible(this.controller.getResultImageModel().getViewRect());
        this.revalidate();
        this.repaint();
    }


    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return null;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

}
