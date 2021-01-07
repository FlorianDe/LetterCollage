
import de.ateam.controller.CollageController;
import de.ateam.model.CollageModel;
import de.ateam.model.text.LetterCollection;
import de.ateam.model.text.LetterFactory;
import de.ateam.utils.FontLoader;
import de.ateam.utils.OpenCVUtils;
import de.ateam.view.CollageFrame;
import de.ateam.view.dialog.SetRoiWeightingDialog;
import de.ateam.view.dialog.SettingsDialog;
import nu.pattern.OpenCV;
import org.opencv.core.Core;

import java.awt.*;

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

    public void startGUI(){
        this.collageModel = new CollageModel();
        this.collageController = new CollageController(this.collageModel);
        LetterFactory.setController(collageController);
        this.collageFrame = new CollageFrame(this.collageController);
        this.collageController.setView(this.collageFrame);
    }
}
