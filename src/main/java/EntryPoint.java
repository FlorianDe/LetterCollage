package main.java;

import main.java.de.ateam.controller.CollageController;
import main.java.de.ateam.model.CollageModel;
import main.java.de.ateam.utils.FontLoader;
import main.java.de.ateam.utils.OpenCVUtils;
import main.java.de.ateam.view.CollageFrame;
import org.opencv.core.Core;

public class EntryPoint {
    public static void main(String[] args) {
        OpenCVUtils.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new EntryPoint().startGUI();
    }

    CollageModel collageModel;
    CollageController collageController;
    CollageFrame collageFrame;

    public void startGUI(){
        this.collageModel = new CollageModel();
        this.collageController = new CollageController(this.collageModel);
        this.collageFrame = new CollageFrame(this.collageController);
        this.collageController.setView(this.collageFrame);
    }
}
