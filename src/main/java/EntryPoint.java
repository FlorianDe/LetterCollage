package main.java;

import main.java.de.ateam.controller.CollageController;
import main.java.de.ateam.model.CollageModel;
import main.java.de.ateam.model.ImageLoaderModel;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.utils.OpenCVUtils;
import main.java.de.ateam.view.MainFrame;
import org.opencv.core.Core;

public class EntryPoint {
    public static void main(String[] args) {
        OpenCVUtils.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        new EntryPoint().startGUI();
    }

    ResultImageModel resultImageModel;
    ImageLoaderModel imageLoaderModel;
    CollageModel collageModel;

    CollageController controller;
    MainFrame mainFrame;

    public void startGUI(){

        this.resultImageModel = new ResultImageModel();
        this.imageLoaderModel = new ImageLoaderModel();
        this.collageModel = new CollageModel();

        this.controller = new CollageController(resultImageModel, imageLoaderModel, collageModel);

        mainFrame = new MainFrame(this.controller);
    }
}
