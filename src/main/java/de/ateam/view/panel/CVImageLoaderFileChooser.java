package main.java.de.ateam.view.panel;

import main.java.de.ateam.controller.ICollageController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVImageLoaderFileChooser extends JPanel{
    //int WIDTH = 200;
    int HEIGHT = 40;


    JTextField textFieldPath;
    JButton btnAddFile;
    JButton btnOpenFileChooser;

    ICollageController controller;
    public CVImageLoaderFileChooser(ICollageController controller){
        this.controller = controller;

        this.createFileChooser();
    }

    private void createFileChooser() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.textFieldPath = new JTextField(10);




        this.btnOpenFileChooser = new JButton("Load");




        this.btnAddFile = new JButton("Add");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        this.add(this.textFieldPath, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        this.add(this.btnOpenFileChooser, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        this.add(this.btnAddFile, c);


    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int)super.getPreferredSize().getWidth(),HEIGHT);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension((int)super.getPreferredSize().getWidth(),HEIGHT);
    }

    private static void setMinimumSize(final Component c) {
        c.setMinimumSize(new Dimension(c
                .getPreferredSize().width - 1,
                c.getPreferredSize().height));
    }
}
