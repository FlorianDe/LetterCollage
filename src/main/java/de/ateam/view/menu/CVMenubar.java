package de.ateam.view.menu;

import de.ateam.controller.ICollageController;

import javax.swing.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenubar extends JMenuBar {
    private CVMenuFile menuFile;
    private CVMenuMouseMode menuImageManipulation;
    private CVMenuView menuView;
    private CVMenuDetection menuDetection;
    private CVMenuSettings menuSettings;

    ICollageController controller;

    public CVMenubar(ICollageController controller) {
        this.controller = controller;
        initializeUI_Menu();
    }

    private void initializeUI_Menu() {
        this.menuFile = new CVMenuFile("File");
        this.menuImageManipulation = new CVMenuMouseMode("MouseMode", controller);
        this.menuView = new CVMenuView("View", controller);
        this.menuDetection = new CVMenuDetection("Detection", controller);
        this.menuSettings = new CVMenuSettings("Settings", controller);

        this.add(this.menuFile);
        this.add(this.menuImageManipulation);
        this.add(this.menuDetection);
        this.add(this.menuView);
        this.add(this.menuSettings);
    }
}
