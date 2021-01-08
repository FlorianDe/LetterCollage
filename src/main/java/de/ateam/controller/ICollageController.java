package de.ateam.controller;

import de.ateam.controller.roi.RegionOfInterestController;
import de.ateam.model.ResultImageModel;
import de.ateam.model.RoiModel;
import de.ateam.view.CollageFrame;

/**
 * Created by Florian on 13.11.2015.
 */
public interface ICollageController {
    ResultImageModel getResultImageModel();

    RoiModel getRoiModel();

    CollageFrame getCollageView();

    RegionOfInterestController getRoiController();
}
