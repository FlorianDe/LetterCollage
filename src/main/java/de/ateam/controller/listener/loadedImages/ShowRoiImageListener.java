package de.ateam.controller.listener.loadedImages;

import de.ateam.controller.ICollageController;
import de.ateam.model.roi.RegionOfInterestImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ShowRoiImageListener implements ActionListener {
    protected ICollageController controller;
    RegionOfInterestImage roiImage;

    public ShowRoiImageListener(ICollageController controller) {
        this(controller, null);
    }
    public ShowRoiImageListener(ICollageController controller, RegionOfInterestImage roiImage) {
        this.controller = controller;
        this.roiImage = roiImage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.controller.getResultImageModel()!=null) {
            if(roiImage!=null){
                if (!this.controller.getResultImageModel().getActualVisibleImage().equals(this.roiImage.getVisualImage())) {
                    this.controller.getResultImageModel().setActualVisibleRoiImage(this.roiImage);
                }
            }
            else{
                this.controller.getResultImageModel().setActualVisibleRoiImage(this.controller.getResultImageModel().getEndResultVisibleRoiImage());
            }
            //this.controller.getResultImageModel().setZoomFactor(1.0);
        }
    }
}
