package de.ateam.view.panel;

import de.ateam.controller.ICollageController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVFileChooser extends JFileChooser {

    ICollageController controller;
    public CVFileChooser(ICollageController controller){
        super("Choose a file/s");
        this.controller = controller;
        this.disableNewFolderButton(this);
        FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        this.setFileFilter(imageFilter);
        this.setMultiSelectionEnabled(true);
        this.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }


    public static void disableNewFolderButton(Container c) {
        int len = c.getComponentCount();
        for (int i = 0; i < len; i++) {
            Component comp = c.getComponent(i);
            if (comp instanceof JButton) {
                JButton b = (JButton) comp;
                Icon icon = b.getIcon();
                if (icon != null
                        && icon == UIManager.getIcon("FileChooser.newFolderIcon"))
                    b.setEnabled(false);
            } else if (comp instanceof Container) {
                disableNewFolderButton((Container) comp);
            }
        }
    }


}
