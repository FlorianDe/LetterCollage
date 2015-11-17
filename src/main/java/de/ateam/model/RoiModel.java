package main.java.de.ateam.model;

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

    private LetterCollection letterCollection;
    private RegionOfInterestImageCollection roiImageCollection;

    public RoiModel() {
        this.roiImageCollection = new RegionOfInterestImageCollection();

        //TODO JUST FOR DEVELOPING!
        try {
            this.roiImageCollection.addImage(ImageIO.read(FileLoader.loadFile("img/people/slide1.jpg")));
            this.roiImageCollection.addImage(ImageIO.read(FileLoader.loadFile("img/people/people2.jpg")));
            this.roiImageCollection.addImage(ImageIO.read(FileLoader.loadFile("img/people/people.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFont(String fontName) {
        if(fontName != null) {
            this.letterCollection = LetterFactory.getCollection(fontName);
            if(this.letterCollection!= null) {
                this.setLetterCollection(this.letterCollection);
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
    }
}
