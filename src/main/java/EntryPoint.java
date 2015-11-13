package main.java;

import main.java.de.ateam.controller.CollageController;
import main.java.de.ateam.model.ImageLoaderModel;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.view.MainFrame;

public class EntryPoint {
    public static void main(String[] args) {
        new EntryPoint().startGUI();
    }

    ResultImageModel resultImageModel;
    ImageLoaderModel imageLoaderModel;

    CollageController controller;
    MainFrame mainFrame;

    public void startGUI(){
        this.resultImageModel = new ResultImageModel();
        this.imageLoaderModel = new ImageLoaderModel();
        this.controller = new CollageController(resultImageModel, imageLoaderModel);

        mainFrame = new MainFrame(this.controller);
    }
}
