package de.ateam.controller.listener.loadedImages;

import de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteAllLoadedImagesListener implements ActionListener {
    protected ICollageController controller;

    public DeleteAllLoadedImagesListener(ICollageController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.controller.getRoiModel().getRoiCollection().removeAllImages();
    }
}
