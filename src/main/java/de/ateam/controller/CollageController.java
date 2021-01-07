package de.ateam.controller;

import de.ateam.controller.roi.RegionOfInterestController;
import de.ateam.model.CollageModel;
import de.ateam.model.RoiModel;
import de.ateam.model.ResultImageModel;
import de.ateam.view.CollageFrame;

/**
 * Created by Florian on 13.11.2015.
 */
public class CollageController implements ICollageController{

    //Main Model
    private RegionOfInterestController roiController;
    private CollageModel collageModel;
    private CollageFrame collageView;

    @Override
    public ResultImageModel getResultImageModel() {
        return collageModel.getResultImageModel();
    }

    @Override
    public RoiModel getRoiModel() {
        return collageModel.getRoiModel();
    }

    @Override
    public CollageFrame getCollageView() {
        return collageView;
    }

    public CollageController(CollageModel collageModel){
        this.collageModel = collageModel;
        this.roiController = new RegionOfInterestController(this);
    }

    public void setResultImageModel(ResultImageModel resultImageModel) {
        this.collageModel.setResultImageModel(resultImageModel);
    }

    public void setRoiModel(RoiModel RoiModel) {
        this.collageModel.setRoiModel(RoiModel);
    }

    public void setView(CollageFrame collageView) {
        this.collageView = collageView;
    }

    public RegionOfInterestController getRoiController() {
        return roiController;
    }

    public void setRoiController(RegionOfInterestController roiController) {
        this.roiController = roiController;
    }
}
