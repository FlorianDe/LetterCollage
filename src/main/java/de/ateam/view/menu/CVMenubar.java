package main.java.de.ateam.view.menu;

import main.java.de.ateam.controller.ICollageController;

import javax.swing.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenubar extends JMenuBar {
    private CVMenuFile menuFile;
    private CVMenuImageManipulation menuImageManipulation;

    ICollageController controller;
    public CVMenubar(ICollageController controller){
        this.controller = controller;
        initializeUI_Menu();
    }

    private void initializeUI_Menu() {
        this.menuFile = new CVMenuFile("File");
        this.menuImageManipulation = new CVMenuImageManipulation("Manipulation", controller);

        this.add(this.menuFile);
        this.add(this.menuImageManipulation);
    }
}
