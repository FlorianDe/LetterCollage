package main.java.de.ateam.view.dialog;

import main.java.de.ateam.controller.ICollageController;

import javax.swing.*;
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

    private String str_lblRows = "Weighting";
    private String str_okBtn = "Ok";
    private String str_applyBtn = "Apply";
    private String str_cancelBtn = "Cancel";

    private JButton btnAccept;
    private JButton btnApply;
    private JButton btnCancel;

    private JLabel lblLetterSampleSize;
    private JTextField tfLetterSampleSize;

    private JLabel lblScaleEnd;
    private JTextField tfScaleEnd;

    private JLabel lblScaleSteps;
    private JTextField tfScaleSteps;

    ICollageController controller;

    public SettingsDialog(ICollageController controller) {
        super(controller.getCollageView(), "Settings", true);
        this.controller=controller;

        if (this.getOwner() != null) {
            Dimension parentSize = this.getOwner().getSize();
            Point p = this.getOwner().getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        } else {
            this.setLocationRelativeTo(null);
        }
        GridLayout gl = new GridLayout(0, 2);
        this.setLayout(gl);

        this.lblLetterSampleSize = new JLabel(str_lblRows);
        this.tfLetterSampleSize = new JTextField(4);
        this.tfLetterSampleSize.setText(1 + "");

        this.lblScaleEnd = new JLabel(str_lblRows);
        this.tfScaleEnd = new JTextField(4);

        this.lblScaleSteps = new JLabel(str_lblRows);
        this.tfScaleSteps = new JTextField(4);

        this.btnAccept = new JButton(str_okBtn);
        this.btnAccept = new JButton(str_applyBtn);
        this.btnCancel = new JButton(str_cancelBtn);


        //add(this.lblWeighting);
        //add(this.tfWeighting);
        add(this.btnAccept);
        add(this.btnApply);
        add(this.btnCancel);

        this.setOnCancelListener();

        pack();
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


    private double getIntFromJTextField(JTextField jTextField) {
        double value = 0;
        try {
            value = Double.parseDouble(jTextField.getText());
        } catch (NumberFormatException e) {
        }
        return value;
    }

}
