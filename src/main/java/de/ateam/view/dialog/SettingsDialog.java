package main.java.de.ateam.view.dialog;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.roi.RegionOfInterestCalculator;
import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.view.cstmcomponent.JSliderLabelPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Florian on 23.01.2016.
 */
public class SettingsDialog extends JDialog {
    private static final long serialVersionUID = -3844798685587070198L;
    private final int COMP_HEIGHT = 10;
    private final int COMP_WIDTH = 20;

    private String str_lblLetterSampleSize = "Letter Sample Size";
    private String str_lblScaleEnd = "Scale Factor End";
    private String str_lblScaleSteps = "Scale Steps";
    private String str_okBtn = "Ok";
    private String str_applyBtn = "Apply";
    private String str_cancelBtn = "Cancel";

    private JButton btnAccept;
    private JButton btnApply;
    private JButton btnCancel;

    private JLabel lblLetterSampleSize;
    private JSliderLabelPanel sliderLetterSampleSize;

    private JLabel lblScaleEnd;
    private JSliderLabelPanel sliderScaleEnd;

    private JLabel lblScaleSteps;
    private JSliderLabelPanel sliderScaleSteps;

    ICollageController controller;

    public SettingsDialog(ICollageController controller) {
        super(controller.getCollageView(), "Settings", false);
        this.controller=controller;

        if (this.getOwner() != null) {
            Dimension parentSize = this.getOwner().getSize();
            Point p = this.getOwner().getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        } else {
            this.setLocationRelativeTo(null);
        }
        BorderLayout outter = new BorderLayout();
        this.setLayout(outter);

        GridLayout settingsGL = new GridLayout(0, 2);
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(settingsGL);
        settingsPanel.setBorder(new EmptyBorder(5, 10, 5, 5));

        this.lblLetterSampleSize = new JLabel(str_lblLetterSampleSize);
        this.sliderLetterSampleSize = new JSliderLabelPanel(controller, LetterCollection.SAMPLER_MIN, LetterCollection.SAMPLER_MAX,controller.getRoiModel().getLetterCollection().getSAMPLER_SIZE(),1.0,"");

        double scaleFactor = 0.1;
        this.lblScaleEnd = new JLabel(str_lblScaleEnd);
        this.sliderScaleEnd = new JSliderLabelPanel(controller, (int)(controller.getRoiModel().SCALE_START/scaleFactor),(int)(controller.getRoiModel().SCALE_MAX/scaleFactor),(int)(controller.getRoiModel().getScaleEnd()/scaleFactor),scaleFactor,"");

        double scaleFactorSteps = 0.05;
        this.lblScaleSteps = new JLabel(str_lblScaleSteps);
        this.sliderScaleSteps = new JSliderLabelPanel(controller,(int)(controller.getRoiModel().SCALE_STEP_SIZE_MIN/scaleFactorSteps),(int)(controller.getRoiModel().SCALE_STEP_SIZE_MAX/scaleFactorSteps),(int)(controller.getRoiModel().getScaleStepSize()/scaleFactorSteps),scaleFactorSteps,"");


        settingsPanel.add(this.lblLetterSampleSize);
        settingsPanel.add(this.sliderLetterSampleSize);
        settingsPanel.add(this.lblScaleEnd);
        settingsPanel.add(this.sliderScaleEnd);
        settingsPanel.add(this.lblScaleSteps);
        settingsPanel.add(this.sliderScaleSteps);



        GridLayout buttonGL = new GridLayout(1, 3);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(buttonGL);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.btnAccept = new JButton(str_okBtn);
        this.btnApply = new JButton(str_applyBtn);
        this.btnCancel = new JButton(str_cancelBtn);


        buttonPanel.add(btnAccept);
        buttonPanel.add(btnApply);
        buttonPanel.add(btnCancel);

        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(settingsPanel, BorderLayout.CENTER);

        this.setOnCancelListener();

        this.pack();
    }

    public void setOnApplyListener(ActionListener list) {
        this.btnApply.addActionListener(list);
    }

    public void setOnOKListener(ActionListener list) {
        this.btnAccept.addActionListener(list);
    }

    private void setOnCancelListener() {
        this.btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAction();
            }
        });
    }

    public void closeAction() {
        setVisible(false);
        dispose();
    }


    public JSliderLabelPanel getSliderLetterSampleSize() {
        return sliderLetterSampleSize;
    }

    public JSliderLabelPanel getSliderScaleEnd() {
        return sliderScaleEnd;
    }

    public JSliderLabelPanel getSliderScaleSteps() {
        return sliderScaleSteps;
    }
}
