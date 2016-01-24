package main.java.de.ateam.model;

import main.java.de.ateam.exception.NoFontSelectedException;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.roi.RegionOfInterestImageCollection;
import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.model.text.LetterFactory;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.FileLoader;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by viktorspadi on 15.11.15.
 */
public class RoiModel extends CstmObservable{
    public static final double SCALE_STEP_SIZE_MIN = 0.05;
    public static final double SCALE_STEP_SIZE_MAX = 1.0;
    public double scaleStepSize = 0.1;

    public static final double SCALE_START = 1.0;
    public static final double SCALE_MAX = 4.0;
    public double scaleEnd = 2.0;

    private String inputText;
    private LetterCollection letterCollection;
    private RegionOfInterestImageCollection roiImageCollection;

    public RoiModel() {
        this.roiImageCollection = new RegionOfInterestImageCollection();
        //TODO JUST FOR DEVELOPING!
        this.inputText = "VFN";
        try {
            this.roiImageCollection.addImage(ImageIO.read(FileLoader.loadFile("img/pictures/1.jpg")));
            this.roiImageCollection.addImage(ImageIO.read(FileLoader.loadFile("img/pictures/2.jpg")));
            this.roiImageCollection.addImage(ImageIO.read(FileLoader.loadFile("img/pictures/3.jpg")));

            String defaultFontName = "Showcard Gothic";
            if(LetterFactory.getCollection(defaultFontName)!=null)
                this.setLetterCollection(LetterFactory.getCollection(defaultFontName));
            else
                this.setLetterCollection(LetterFactory.getFirstFont());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoFontSelectedException e) {
            e.printStackTrace();
        }
    }

    public void loadFont(String fontName) {
        if(fontName != null) {
            LetterCollection lc = LetterFactory.getCollection(fontName);
            if(lc != null) {
                this.setLetterCollection(lc);
            }
        }
    }

    public LetterCollection getLetterCollection() {
        return this.letterCollection;
    }

    public RegionOfInterestImageCollection getRoiCollection() {
        return this.roiImageCollection;
    }

    public ArrayList<RegionOfInterestImage> getLoadedImages() {
        return roiImageCollection.getRoiImages();
    }

    public void setLetterCollection(LetterCollection letterCollection) {
        this.letterCollection = letterCollection;
        // evtl direkt berechnen, sonst irgendwo button einbauen der called
        if(this.roiImageCollection.isArrangeable()) {
            this.roiImageCollection.arrangeImages();
        }
        this.setChanged();
        this.notifyObservers(null);
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public double getScaleStepSize() {
        return scaleStepSize;
    }

    public void setScaleStepSize(double scaleStepSize) {
        this.scaleStepSize = scaleStepSize;
        this.setChanged();
        this.notifyObservers(null);
    }

    public double getScaleEnd() {
        return scaleEnd;
    }

    public void setScaleEnd(double scaleEnd) {
        this.scaleEnd = scaleEnd;
        this.setChanged();
        this.notifyObservers(null);
    }
}
