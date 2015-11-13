package main.java.de.ateam.view.menu;

import main.java.de.ateam.controller.ICollageController;

import javax.swing.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenubar extends JMenuBar {
    private CVMenuFile menuFile;

    ICollageController controller;
    public CVMenubar(ICollageController controller){
        this.controller = controller;
        initializeUI_Menu();
    }

    private void initializeUI_Menu() {
        this.menuFile = new CVMenuFile("File");


        this.add(this.menuFile);
    }
}
