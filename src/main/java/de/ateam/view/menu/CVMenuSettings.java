package de.ateam.view.menu;

import de.ateam.controller.ICollageController;
import de.ateam.controller.listener.resultImage.OpenSettingsListener;
import de.ateam.utils.CstmObservable;
import de.ateam.utils.CstmObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuSettings extends JMenu implements CstmObserver {

    JMenuExtension jme;
    JMenuItem menuItemSettings;

    ICollageController controller;

    public CVMenuSettings(String name, ICollageController controller) {
        super(name);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");


        this.menuItemSettings = this.jme.createJMenuItem(new JMenuItem("Settings"), 'I', "STRING_DESCRIPTION", this);
        this.menuItemSettings.addActionListener(new OpenSettingsListener(controller));


        this.addSeparator();

        this.add(menuItemSettings);


    }

    @Override
    public void update(CstmObservable o, Object arg) {
    }
}
