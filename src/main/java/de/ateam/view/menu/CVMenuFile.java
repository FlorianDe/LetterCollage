package main.java.de.ateam.view.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuFile extends JMenu {

    JMenuExtension jme;
    JMenuItem menuItemLoad;
    JMenuItem menuItemSave;

    public CVMenuFile(String name){
        super(name);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");

        this.menuItemLoad = this.jme.createJMenuItem(new JMenuItem("Laden"), 'L', "STRING_DESCRIPTION", this);
        this.menuItemSave = this.jme.createJMenuItem(new JMenuItem("Speichern"), 'S', "STRING_DESCRIPTION", this);
    }

}
