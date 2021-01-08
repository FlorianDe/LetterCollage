package de.ateam.view.menu;

import de.ateam.controller.ICollageController;
import de.ateam.controller.listener.resultImage.EyeDetectionListener;
import de.ateam.controller.listener.resultImage.FaceDetectionListener;
import de.ateam.controller.listener.resultImage.FullbodyDetectionListener;
import de.ateam.controller.listener.resultImage.SaliencyMapDetectionListener;
import de.ateam.utils.CstmObservable;
import de.ateam.utils.CstmObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuDetection extends JMenu implements CstmObserver {

    JMenuExtension jme;
    JMenuItem menuItemEyeDetection;
    JMenuItem menuItemFaceDetection;
    JMenuItem menuItemFullbodyDetection;
    JMenuItem menuItemSaliencyDetection;

    ICollageController controller;

    public CVMenuDetection(String name, ICollageController controller) {
        super(name);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");


        this.menuItemEyeDetection = this.jme.createJMenuItem(new JMenuItem("Eye Detection"), 'I', "STRING_DESCRIPTION", this);
        this.menuItemEyeDetection.addActionListener(new EyeDetectionListener(controller));

        this.menuItemFaceDetection = this.jme.createJMenuItem(new JMenuItem("Face Detection"), 'O', "STRING_DESCRIPTION", this);
        this.menuItemFaceDetection.addActionListener(new FaceDetectionListener(controller));

        this.menuItemFullbodyDetection = this.jme.createJMenuItem(new JMenuItem("Fullbody Detection"), 'I', "STRING_DESCRIPTION", this);
        this.menuItemFullbodyDetection.addActionListener(new FullbodyDetectionListener(controller));

        this.menuItemSaliencyDetection = this.jme.createJMenuItem(new JMenuItem("Saliency Map Detection"), 'I', "STRING_DESCRIPTION", this);
        this.menuItemSaliencyDetection.addActionListener(new SaliencyMapDetectionListener(controller));

    }

    @Override
    public void update(CstmObservable o, Object arg) {

    }
}
