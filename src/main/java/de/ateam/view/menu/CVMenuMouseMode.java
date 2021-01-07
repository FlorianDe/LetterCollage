package de.ateam.view.menu;

import de.ateam.controller.ICollageController;
import de.ateam.controller.listener.resultImage.MouseModeSetListener;
import de.ateam.model.ResultImageModel;
import de.ateam.utils.CstmObservable;
import de.ateam.utils.CstmObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuMouseMode extends JMenu implements CstmObserver{

    JMenuExtension jme;
    JMenuItem menuItemMouseModeDrag;
    JMenuItem menuItemMouseModePaint;
    JMenuItem menuItemMouseModePolygonPaint;
    JMenuItem menuItemMouseModeSimilarSelect;
    JMenuItem menuItemMouseModeErase;
    JMenuItem menuItemMouseModeDefault;
    JMenuItem menuItemMouseModeSetWeight;

    ICollageController controller;


    public CVMenuMouseMode(String name, ICollageController controller){
        super(name);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");

        this.menuItemMouseModeDefault = this.jme.createJMenuItem(new JMenuItem("Default"), 'D', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeDefault.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.DEFAULT));

        this.menuItemMouseModeDrag = this.jme.createJMenuItem(new JMenuItem("Drag"), 'D', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeDrag.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.DRAG));


        this.menuItemMouseModePaint = this.jme.createJMenuItem(new JMenuItem("Paint"), 'P', "STRING_DESCRIPTION", this);
        this.menuItemMouseModePaint.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.PAINT));

        this.menuItemMouseModePolygonPaint = this.jme.createJMenuItem(new JMenuItem("Polygon"), 'P', "STRING_DESCRIPTION", this);
        this.menuItemMouseModePolygonPaint.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.POLYGONPAINT));

        this.menuItemMouseModeSimilarSelect = this.jme.createJMenuItem(new JMenuItem("Select Similar"), 'E', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeSimilarSelect.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.SIMILAR_SELECT));


        this.menuItemMouseModeErase = this.jme.createJMenuItem(new JMenuItem("Erase"), 'P', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeErase.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.ERASE));


        this.menuItemMouseModeSetWeight = this.jme.createJMenuItem(new JMenuItem("SetWeight"), 'S', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeSetWeight.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.SETWEIGHT));


        update(null, null);
    }


    @Override
    public void update(CstmObservable o, Object arg) {
        this.menuItemMouseModeDrag.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.DRAG));
        this.menuItemMouseModePaint.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.PAINT));
        this.menuItemMouseModePolygonPaint.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.POLYGONPAINT));
        this.menuItemMouseModeErase.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ERASE));
        this.menuItemMouseModeDefault.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.DEFAULT));
        this.menuItemMouseModeSetWeight.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.SETWEIGHT));
        this.menuItemMouseModeSimilarSelect.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.SIMILAR_SELECT));

    }
}
