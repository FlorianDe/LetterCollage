package main.java.de.ateam.controller;

import main.java.de.ateam.model.CollageModel;
import main.java.de.ateam.model.RoiModel;
import main.java.de.ateam.model.ResultImageModel;

/**
 * Created by Florian on 13.11.2015.
 */
public class CollageController implements ICollageController{
    //Main Model
    CollageModel collageModel;

    @Override
    public ResultImageModel getResultImageModel() {
        return collageModel.getResultImageModel();
    }

    @Override
    public RoiModel getRoiModel() {
        return collageModel.getRoiModel();
    }

    public CollageController(CollageModel collageModel){
        this.collageModel = collageModel;
    }

    public void setResultImageModel(ResultImageModel resultImageModel) {
        this.collageModel.setResultImageModel(resultImageModel);
    }

    public void setRoiModel(RoiModel RoiModel) {
        this.collageModel.setRoiModel(RoiModel);
    }
}
