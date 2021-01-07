package de.ateam.view.panel;

import de.ateam.controller.ICollageController;
import de.ateam.controller.listener.loadedImages.DeleteAllLoadedImagesListener;
import de.ateam.controller.listener.loadedImages.OpenFileChooserListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVImageLoaderFileChooser extends JPanel{
    //int WIDTH = 200;
    int HEIGHT = 40;

    JButton btnOpenFileChooser;
    JButton btnDeleteAll;

    ICollageController controller;
    public CVImageLoaderFileChooser(ICollageController controller){
        this.controller = controller;
        this.createFileChooser();
    }

    private void createFileChooser() {
        this.setLayout(new GridBagLayout());


        this.btnOpenFileChooser = new JButton("Load Images");
        this.btnOpenFileChooser.addActionListener(new OpenFileChooserListener(this.controller));

        this.btnDeleteAll = new JButton("Delete All");
        this.btnDeleteAll.addActionListener(new DeleteAllLoadedImagesListener(this.controller));




        /*
        GridBagConstraints c = new GridBagConstraints();
        this.textFieldPath = new JTextField(10);
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
        */

        this.add(this.btnOpenFileChooser);
        this.add(this.btnDeleteAll);
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
