package main.java.de.ateam.controller;

import main.java.de.ateam.model.CollageModel;
import main.java.de.ateam.model.ImageLoaderModel;
import main.java.de.ateam.model.ResultImageModel;

/**
 * Created by Florian on 13.11.2015.
 */
public interface ICollageController {
    ResultImageModel getResultImageModel();
    ImageLoaderModel getImageLoaderModel();
    CollageModel getCollageModel();
}
