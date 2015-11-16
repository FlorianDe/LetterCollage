package main.java.de.ateam.controller;

import main.java.de.ateam.model.CollageModel;
import main.java.de.ateam.model.ImageLoaderModel;
import main.java.de.ateam.model.ResultImageModel;

/**
 * Created by Florian on 13.11.2015.
 */
public class CollageController implements ICollageController{

    //MODELS
    ResultImageModel resultImageModel;
    ImageLoaderModel imageLoaderModel;
    CollageModel collageModel;

    @Override
    public ResultImageModel getResultImageModel() {
        return resultImageModel;
    }

    @Override
    public ImageLoaderModel getImageLoaderModel() {
        return imageLoaderModel;
    }

    @Override
    public CollageModel getCollageModel() {
        return collageModel;
    }

    public CollageController(){

    }

    public CollageController(ResultImageModel resultImageModel, ImageLoaderModel imageLoaderModel, CollageModel collageModel){
        this.resultImageModel = resultImageModel;
        this.imageLoaderModel = imageLoaderModel;
        this.collageModel = collageModel;
    }

    public void setResultImageModel(ResultImageModel resultImageModel) {
        this.resultImageModel = resultImageModel;
    }

    public void setImageLoaderModel(ImageLoaderModel imageLoaderModel) {
        this.imageLoaderModel = imageLoaderModel;
    }

    public void setCollageModel(CollageModel collageModel) {
        this.collageModel = collageModel;
    }
}
