package main.java.de.ateam.controller;

import main.java.de.ateam.model.ImageLoaderModel;
import main.java.de.ateam.model.ResultImageModel;

/**
 * Created by Florian on 13.11.2015.
 */
public class CollageController implements ICollageController{

    //MODELS
    ResultImageModel resultImageModel;
    ImageLoaderModel imageLoaderModel;

    @Override
    public ResultImageModel getResultImageModel() {
        return resultImageModel;
    }

    @Override
    public ImageLoaderModel getImageLoaderModel() {
        return imageLoaderModel;
    }


    public CollageController(ResultImageModel resultImageModel, ImageLoaderModel imageLoaderModel){
        this.resultImageModel = resultImageModel;
        this.imageLoaderModel = imageLoaderModel;
    }
}
