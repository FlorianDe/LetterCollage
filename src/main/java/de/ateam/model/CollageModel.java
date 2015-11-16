package main.java.de.ateam.model;

import main.java.de.ateam.model.roi.RegionOfInterestCollection;
import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.model.text.LetterFactory;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.FileLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by viktorspadi on 15.11.15.
 */
public class CollageModel extends CstmObservable{

    private LetterCollection letterCollection;
    private RegionOfInterestCollection regionOfInterestCollection;

    public CollageModel() {
        this.regionOfInterestCollection = new RegionOfInterestCollection();

        try {
            this.regionOfInterestCollection.addImage(ImageIO.read(FileLoader.loadFile("img/1.jpg")));
            this.regionOfInterestCollection.addImage(ImageIO.read(FileLoader.loadFile("img/2.gif")));
            this.regionOfInterestCollection.addImage(ImageIO.read(FileLoader.loadFile("img/3.gif")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadFont(String fontName) {
        if(fontName != null) {
            this.letterCollection = LetterFactory.getCollection(fontName);
            this.regionOfInterestCollection.setLetterCollection(this.letterCollection);
        }
    }

    public LetterCollection getLetterCollection() {
        return this.letterCollection;
    }

    public RegionOfInterestCollection getROICollection() {
        return this.regionOfInterestCollection;
    }

    public ArrayList<BufferedImage> getLoadedImages() {
        return regionOfInterestCollection.getAllImages();
    }
}
