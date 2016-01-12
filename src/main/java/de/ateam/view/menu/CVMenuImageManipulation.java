package main.java.de.ateam.view.menu;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.resultImage.MouseModeSetListener;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuImageManipulation extends JMenu implements CstmObserver{

    JMenuExtension jme;
    JMenu subMenuMouseMode;
    JMenuItem menuItemMouseModeDrag;
    JMenuItem menuItemMouseModePaint;
    JMenuItem menuItemMouseModePolygonPaint;
    JMenuItem menuItemMouseModeSimilarSelect;
    JMenuItem menuItemMouseModeErase;
    JMenuItem menuItemMouseModeDefault;
    JMenuItem menuItemMouseModeZoomIn;
    JMenuItem menuItemMouseModeZoomOut;
    JMenuItem menuItemMouseModeSetWeight;

    ICollageController controller;


    //K�nnte man auch dynamischer �ber die Enum Liste machen....
    public CVMenuImageManipulation(String name, ICollageController controller){
        super(name);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");

        this.subMenuMouseMode = this.jme.createJMenu(new JMenu("MouseMode"), "STRING_DESCRIPTION", this);

        this.menuItemMouseModeDefault = this.jme.createJMenuItem(new JMenuItem("Default"), 'D', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModeDefault.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.DEFAULT));

        this.menuItemMouseModeDrag = this.jme.createJMenuItem(new JMenuItem("Drag"), 'D', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModeDrag.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.DRAG));

        this.subMenuMouseMode.addSeparator();

        this.menuItemMouseModePaint = this.jme.createJMenuItem(new JMenuItem("Paint"), 'P', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModePaint.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.PAINT));

        this.menuItemMouseModePolygonPaint = this.jme.createJMenuItem(new JMenuItem("Polygon"), 'P', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModePolygonPaint.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.POLYGONPAINT));

        this.menuItemMouseModeSimilarSelect = this.jme.createJMenuItem(new JMenuItem("Select Similar"), 'E', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModeSimilarSelect.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.SIMILAR_SELECT));


        this.menuItemMouseModeErase = this.jme.createJMenuItem(new JMenuItem("Erase"), 'P', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModeErase.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.ERASE));

        this.subMenuMouseMode.addSeparator();

        this.menuItemMouseModeZoomIn = this.jme.createJMenuItem(new JMenuItem("ZoomIn"), 'I', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModeZoomIn.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.ZOOMIN));

        this.menuItemMouseModeZoomOut = this.jme.createJMenuItem(new JMenuItem("ZoomOut"), 'O', "STRING_DESCRIPTION", this.subMenuMouseMode);
        this.menuItemMouseModeZoomOut.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.ZOOMOUT));

        this.subMenuMouseMode.addSeparator();

        this.menuItemMouseModeSetWeight = this.jme.createJMenuItem(new JMenuItem("SetWeight"), 'S', "STRING_DESCRIPTION", this.subMenuMouseMode);
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
        this.menuItemMouseModeZoomIn.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ZOOMIN));
        this.menuItemMouseModeZoomOut.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ZOOMOUT));
        this.menuItemMouseModeSetWeight.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.SETWEIGHT));
        this.menuItemMouseModeSimilarSelect.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.SIMILAR_SELECT));

    }
}
