package de.ateam.view.dialog;

import de.ateam.controller.ICollageController;
import de.ateam.controller.listener.resultImage.*;
import de.ateam.model.text.LetterCollection;
import de.ateam.utils.CstmObservable;
import de.ateam.utils.CstmObserver;
import de.ateam.view.cstmcomponent.JSliderLabelPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Florian on 23.01.2016.
 */
public class SettingsDialog extends JDialog implements CstmObserver {
    private static final long serialVersionUID = -3844798685587070198L;
    private final int COMP_HEIGHT = 10;
    private final int COMP_WIDTH = 20;

    private String str_lblLetterSampleSize = "Letter Sample Size";
    private String str_lblScaleEnd = "Scale Factor End";
    private String str_lblScaleSteps = "Scale Steps";
    private String str_lblBackgroundColor = "Result Image Background Color";
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

    private JLabel lblBorderThickness;
    private JSliderLabelPanel sliderBorderThickness;

    private JLabel lblBackgroundColor;
    private JButton btnBackgroundColor;

    private JLabel lblBorderColor;
    private JButton btnBorderColor;

    private JCheckBox cbSaliencyMap;
    private JCheckBox cbFaceDetection;

    private JCheckBox cbFullbodyDetection;
    private JCheckBox cbEyeDetection;

    ICollageController controller;

    public SettingsDialog(ICollageController controller) {
        super(controller.getCollageView(), "Settings", false);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);

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
        this.sliderLetterSampleSize = new JSliderLabelPanel(controller, LetterCollection.SAMPLER_MIN, LetterCollection.SAMPLER_MAX, controller.getRoiModel().getLetterCollection().getSAMPLER_SIZE(), 1.0, "");

        double scaleFactor = 0.1;
        this.lblScaleEnd = new JLabel(str_lblScaleEnd);
        this.sliderScaleEnd = new JSliderLabelPanel(controller, (int) (controller.getRoiModel().SCALE_START / scaleFactor), (int) (controller.getRoiModel().SCALE_MAX / scaleFactor), (int) (controller.getRoiModel().getScaleEnd() / scaleFactor), scaleFactor, "");

        double scaleFactorSteps = 0.05;
        this.lblScaleSteps = new JLabel(str_lblScaleSteps);
        this.sliderScaleSteps = new JSliderLabelPanel(controller, (int) (controller.getRoiModel().SCALE_STEP_SIZE_MIN / scaleFactorSteps), (int) (controller.getRoiModel().SCALE_STEP_SIZE_MAX / scaleFactorSteps), (int) (controller.getRoiModel().getScaleStepSize() / scaleFactorSteps), scaleFactorSteps, "");

        this.lblBorderThickness = new JLabel("Border Thickness");
        this.sliderBorderThickness = new JSliderLabelPanel(controller, 0, 50, controller.getResultImageModel().getFontOutlineThickness(), 1.0, "");
        this.sliderBorderThickness.getValueSlider().addChangeListener(new BorderThicknessChangedListener(controller));


        this.lblBorderColor = new JLabel("Result Image Border Color");
        this.btnBorderColor = new JButton();
        this.btnBorderColor.addActionListener(new ChooseBorderColorListener(this.controller));


        this.lblBackgroundColor = new JLabel(str_lblBackgroundColor);
        this.btnBackgroundColor = new JButton();
        this.btnBackgroundColor.addActionListener(new ChooseBackgroundColorListener(this.controller));


        this.cbSaliencyMap = new JCheckBox("Saliency Map Detection On/Off");
        this.cbSaliencyMap.addItemListener(new DetectionSaliencyMapOnOffListener(controller));

        this.cbEyeDetection = new JCheckBox("Eye Detection On/Off");
        this.cbEyeDetection.addItemListener(new DetectionEyeOnOffListener(controller));

        this.cbFullbodyDetection = new JCheckBox("Fullbody Detection On/Off");
        this.cbFullbodyDetection.addItemListener(new DetectionFullbodyOnOffListener(controller));

        this.cbFaceDetection = new JCheckBox("Face Detection On/Off");
        this.cbFaceDetection.addItemListener(new DetectionFaceOnOffListener(controller));


        settingsPanel.add(this.lblLetterSampleSize);
        settingsPanel.add(this.sliderLetterSampleSize);
        settingsPanel.add(this.lblScaleEnd);
        settingsPanel.add(this.sliderScaleEnd);
        settingsPanel.add(this.lblScaleSteps);
        settingsPanel.add(this.sliderScaleSteps);
        settingsPanel.add(this.lblBorderThickness);
        settingsPanel.add(this.sliderBorderThickness);
        settingsPanel.add(this.lblBorderColor);
        settingsPanel.add(this.btnBorderColor);
        settingsPanel.add(this.lblBackgroundColor);
        settingsPanel.add(this.btnBackgroundColor);
        settingsPanel.add(this.cbSaliencyMap);
        settingsPanel.add(this.cbEyeDetection);
        settingsPanel.add(this.cbFullbodyDetection);
        settingsPanel.add(this.cbFaceDetection);

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

        update(null, this);
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


    @Override
    public void update(CstmObservable o, Object arg) {
        this.btnBackgroundColor.setBackground(this.controller.getResultImageModel().getBackgroundColor());
        this.btnBorderColor.setBackground(this.controller.getResultImageModel().getFontOutlineColor());

        this.cbSaliencyMap.setSelected(this.controller.getResultImageModel().isSaliencyMapDetection());
        this.cbEyeDetection.setSelected(this.controller.getResultImageModel().isEyeDetection());
        this.cbFaceDetection.setSelected(this.controller.getResultImageModel().isFaceDetection());
        this.cbFullbodyDetection.setSelected(this.controller.getResultImageModel().isFullbodyDetection());
    }
}
