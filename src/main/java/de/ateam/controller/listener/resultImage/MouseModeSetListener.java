package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;
import de.ateam.model.ResultImageModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MouseModeSetListener implements ActionListener {
    protected ICollageController controller;
    private ResultImageModel.MouseMode mouseMode;

    public MouseModeSetListener(ICollageController controller, ResultImageModel.MouseMode mouseMode) {
        this.controller = controller;
        this.mouseMode = mouseMode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.controller.getResultImageModel().setMouseMode(mouseMode);
    }
}
