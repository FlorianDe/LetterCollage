package de.ateam.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Florian
 */
public class SetRoiWeightingDialog extends JDialog {
    private static final long serialVersionUID = -3844798685587070198L;
    private final int COMP_HEIGHT = 10;
    private final int COMP_WIDTH = 20;

    private String str_lblRows = "Weighting";
    private String str_okBtn = "Ok";
    private String str_cancelBtn = "Cancel";

    private JLabel lblWeighting;
    private JTextField tfWeighting;
    private JButton btnAccept;
    private JButton btnCancel;

    public String getStr_lblRows() {
        return str_lblRows;
    }

    public String getStr_okBtn() {
        return str_okBtn;
    }

    public String getStr_cancelBtn() {
        return str_cancelBtn;
    }

    public JLabel getLblWeighting() {
        return lblWeighting;
    }

    public JTextField getTfWeighting() {
        return tfWeighting;
    }

    public JButton getBtnAccept() {
        return btnAccept;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public SetRoiWeightingDialog(JFrame parent, Point pLocation, double oldWeighting) {
        super(parent, "Weighting", true);
        if (pLocation != null) {
            setLocation(pLocation.x, pLocation.y);
        } else if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        } else {
            this.setLocationRelativeTo(null);
        }
        GridLayout gl = new GridLayout(0, 2);
        this.setLayout(gl);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        this.lblWeighting = new JLabel(str_lblRows);
        this.tfWeighting = new JTextField(4);
        this.tfWeighting.setText(oldWeighting + "");
        this.btnAccept = new JButton(str_okBtn);
        this.btnCancel = new JButton(str_cancelBtn);


        add(this.lblWeighting);
        add(this.tfWeighting);
        add(this.btnAccept);
        add(this.btnCancel);

        this.setOnCancelListener();

        pack();
    }

    public void setOnAcceptListener(ActionListener list) {
        this.btnAccept.addActionListener(list);
    }

    private void setOnCancelListener() {
        this.btnCancel.addActionListener(e -> closeAction());
    }

    public void closeAction() {
        setVisible(false);
        dispose();
    }

    public double getWeighting() {
        return getIntFromJTextField(this.tfWeighting);
    }


    private double getIntFromJTextField(JTextField jTextField) {
        double value = 0;
        try {
            value = Double.parseDouble(jTextField.getText());
        } catch (NumberFormatException e) {
        }
        return value;
    }


    public static SetRoiWeightingDialog ssjd;

    // For testing only!
    public static void main(String[] a) {
        EventQueue.invokeLater(() -> {
            ssjd = new SetRoiWeightingDialog(null, null, 1);
            ssjd.setOnCancelListener();
        });
    }
}
