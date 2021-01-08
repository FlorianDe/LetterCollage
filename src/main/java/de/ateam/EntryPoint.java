package de.ateam;

import de.ateam.controller.CollageController;
import de.ateam.model.CollageModel;
import de.ateam.model.text.LetterFactory;
import de.ateam.view.CollageFrame;
import nu.pattern.OpenCV;

public class EntryPoint {
    public static void main(String[] args) {
        OpenCV.loadLocally();
//        OpenCVUtils.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        new EntryPoint().startGUI();
    }

    CollageModel collageModel;
    CollageController collageController;
    CollageFrame collageFrame;

    public void startGUI() {
        this.collageModel = new CollageModel();
        this.collageController = new CollageController(this.collageModel);
        LetterFactory.setController(collageController);
        this.collageFrame = new CollageFrame(this.collageController);
        this.collageController.setView(this.collageFrame);
    }
}
