package main.java.de.ateam.view.panel;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.resultImage.MouseAdapterListener;
import main.java.de.ateam.controller.listener.resultImage.MouseWheelZoomListener;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;
import main.java.de.ateam.utils.FileLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVResultImagePanel extends JPanel implements CstmObserver, Scrollable {
    private final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private final static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private static Cursor cstm_crosshair = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private static Cursor cstm_eraser = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private static Cursor cstm_magicwand = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private static final AlphaComposite OVER_HALF = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);

    static{
        cstm_crosshair = tryLoadCursor("img/icons/cursor/32_cstm_crosshair.png", new Point(16,16));
        cstm_eraser = tryLoadCursor("img/icons/cursor/32_cstm_eraser.png", new Point(0,31));
        cstm_magicwand = tryLoadCursor("img/icons/cursor/32_cstm_magicwand.png", new Point(2,2));

    }

    private static Cursor tryLoadCursor(String path, Point hotspot){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = null;
        try {
            image = ImageIO.read(FileLoader.loadFile(path));
            return toolkit.createCustomCursor(image, hotspot, path);
        } catch (IOException e) {
            // ignore
        }
        return Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    }

    private JScrollPane scrollPane;
    private RenderingHints renderingHints;

    ICollageController controller;

    public CVResultImagePanel(ICollageController controller){
        renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.controller.getRoiModel().getRoiCollection().addObserver(this);


        MouseAdapterListener mal = new MouseAdapterListener(controller);
        this.addMouseListener(mal);
        this.addMouseMotionListener(mal);
        MouseWheelZoomListener mwzl = new MouseWheelZoomListener(controller);
        this.addMouseWheelListener(mwzl);

        this.setAutoscrolls(true);


        update(null, null);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHints(renderingHints);

        switch (this.controller.getResultImageModel().getMouseMode()){
            case DRAG:
                this.setCursor(handCursor);
                break;
            case PAINT:
                this.setCursor(cstm_crosshair);
                break;
            case ERASE:
                this.setCursor(cstm_eraser);
                break;
            case SIMILAR_SELECT:
                this.setCursor(cstm_magicwand);
                break;
            default:
                this.setCursor(defaultCursor);
                break;
        }

        g2d.drawImage(this.controller.getResultImageModel().getActualVisibleImage(),
                0, 0,
                (int) this.controller.getResultImageModel().getRenderSize().getWidth(),
                (int) this.controller.getResultImageModel().getRenderSize().getHeight(),
                null);


        if(this.controller.getResultImageModel().isResolutionRasterVisible()){
            g2d.setComposite(OVER_HALF);
            g2d.setColor(Color.GRAY);
            ResultImageModel rim = this.controller.getResultImageModel();

            int maxHeight = (int) rim.getRenderSize().getHeight();
            int maxWidth = (int) rim.getRenderSize().getWidth();
            if(this.getScrollPane()!=null){
                Dimension parentSize = this.getScrollPane().getViewport().getSize();
                if(maxHeight>(int)parentSize.getHeight()){
                    maxHeight = (int)parentSize.getHeight();
                }
                if(maxWidth>(int)parentSize.getWidth()) {
                    maxWidth = (int)parentSize.getWidth();
                }
            }

            int heightStepSize = (int)(rim.getRenderSize().getHeight()/LetterCollection.SAMPLER_SIZE);
            int widthStepSize =  (int)(rim.getRenderSize().getWidth()/LetterCollection.SAMPLER_SIZE);

            for (int y = 0; y <= maxHeight; y+= heightStepSize)
                g2d.drawLine(rim.getMargin(), rim.getMargin()+y, maxWidth+rim.getMargin(), rim.getMargin()+y);
            for (int x = 0; x <= maxWidth; x+= widthStepSize)
                g2d.drawLine(rim.getMargin()+x, rim.getMargin(), rim.getMargin()+x, maxHeight+rim.getMargin());
        }

        Rectangle r = this.controller.getResultImageModel().getActualDrawnRoi();
        if(r!=null) {
            g2d.setColor(this.controller.getResultImageModel().getActualDrawColor());
            g2d.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
        }

        g2d.dispose();
    }


    @Override
    public Dimension getPreferredSize() {
        ResultImageModel m = this.controller.getResultImageModel();
        if(m.getActualVisibleImage()==null)
            return new Dimension(0,0);
        return new Dimension(m.getRenderSize());
    }

    @Override
    public void update(CstmObservable o, Object arg) {
        this.scrollRectToVisible(this.controller.getResultImageModel().getViewRect());
        this.revalidate();
        this.repaint();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
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