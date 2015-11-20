package main.java.de.ateam.view.toolbar;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.collage.CalculateTestListener;
import main.java.de.ateam.controller.listener.collage.FontSelectionChangedListener;
import main.java.de.ateam.controller.listener.collage.InputTextChangedListener;
import main.java.de.ateam.controller.listener.loadedImages.ShowRoiImageListener;
import main.java.de.ateam.controller.listener.resultImage.FaceDetectionListener;
import main.java.de.ateam.controller.listener.resultImage.ZoomInListener;
import main.java.de.ateam.controller.listener.resultImage.ZoomOutListener;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;
import main.java.de.ateam.utils.FileLoader;
import main.java.de.ateam.utils.FontLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVToolbar extends JToolBar implements CstmObserver {
    private JButton btnNew;
    private JButton btnOpen;
    private JButton btnZoomIn;
    private JButton btnZoomOut;
    private JButton btnCalc;
    private JButton btnSetResultImage;
    private JButton btnFaceDetection;

    private JTextField tfText;
    private JComboBox cbxFonts;

    ICollageController controller;
    public CVToolbar(ICollageController controller){
        this.controller = controller;
        this.createToolbar();
        this.controller.getResultImageModel().addObserver(this);
    }

    public void setStyle(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = getBackground();
        Color color2 = color1.darker();
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(
                0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setStyle(g);
    }

    public void createToolbar(){
        this.btnNew = (JButton) createToolbarButton(new JButton(), "img/New.gif");
        this.add(this.btnNew);

        this.btnOpen = (JButton) createToolbarButton(new JButton(), "img/Open.gif");
        this.add(this.btnOpen);

        this.addSeparator();

        this.btnZoomIn = (JButton) createToolbarButton(new JButton(), "img/ZIn.gif");
        this.btnZoomIn.addActionListener(new ZoomInListener(this.controller));
        this.add(this.btnZoomIn);

        this.btnZoomOut = (JButton) createToolbarButton(new JButton(), "img/ZOut.gif");
        this.btnZoomOut.addActionListener(new ZoomOutListener(this.controller));
        this.add(this.btnZoomOut);

        this.btnCalc = (JButton) createToolbarButton(new JButton(), "img/Calc.gif");
        this.btnCalc.addActionListener(new CalculateTestListener(this.controller));
        this.add(this.btnCalc);

        this.btnSetResultImage = (JButton) createToolbarButton(new JButton(), "img/ResultImage.gif");
        this.btnSetResultImage.addActionListener(new ShowRoiImageListener(controller, controller.getResultImageModel().getEndResultRoiImage()));
        this.add(this.btnSetResultImage);

        this.btnFaceDetection = (JButton) createToolbarButton(new JButton(), "img/FaceDetection.gif");
        this.btnFaceDetection.addActionListener(new FaceDetectionListener(controller));
        this.add(this.btnFaceDetection);

        this.tfText = new JTextField(this.controller.getRoiModel().getInputText());
        this.tfText.getDocument().addDocumentListener(new InputTextChangedListener(controller));
        this.add(this.tfText);

        this.cbxFonts = new JComboBox(FontLoader.getFonts());
        this.cbxFonts.addItemListener(new FontSelectionChangedListener(controller));
        this.add(cbxFonts);
    }

    public AbstractButton createToolbarButton(AbstractButton btn, String filePath){
        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(FileLoader.loadFile(filePath)));
            btn.setIcon(imageIcon);
        } catch (Exception e) {
            String str = filePath.replace("img/", "");
            btn.setText(str.substring(0, str.indexOf(".")).toUpperCase());
        }
        finally{
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        }
        return btn;
    }


    @Override
    public void update(CstmObservable arg0, Object arg1) {
        this.revalidate();
        this.repaint();
    }
}
