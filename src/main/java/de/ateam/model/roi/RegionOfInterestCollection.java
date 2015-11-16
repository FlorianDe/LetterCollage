package main.java.de.ateam.model.roi;

import main.java.de.ateam.model.text.LetterCollection;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by vspadi on 16.11.15.
 */
public class RegionOfInterestCollection {
    private boolean autoArrange = false;
    private HashMap<BufferedImage, RegionOfInterest> regions;
    private LetterCollection letterCollection;
    private ArrayList<BufferedImage> allImages;

    public RegionOfInterestCollection() {
        this.regions = new HashMap<>();

    }

    public void setLetterCollection(LetterCollection letterCollection) {
        this.letterCollection = letterCollection;
        // evtl direkt berechnen, sonst irgendwo button einbauen der called
        if(regions.size() > 0 && autoArrange)
            arrageImages();
    }

    public RegionOfInterest addImage(BufferedImage image) {
        regions.put(image, new RegionOfInterest(image));
        return regions.get(image);
    }

    public void removeImage(BufferedImage image) {
        if(regions.containsKey(image)) {
            regions.remove(image);
            if(autoArrange)
                arrageImages();
        }
    }

    public RegionOfInterest getImage(BufferedImage image) {
        return regions.get(image);
    }

    public void arrageImages() {
        // hier heftige magic um bilder anzuordnen würde ich mal behaupten
    }

    public ArrayList<BufferedImage> getAllImages() {
        ArrayList<BufferedImage> images = new ArrayList<>();
        Set<BufferedImage> imgKeys = this.regions.keySet();
        for(BufferedImage img: imgKeys) {
            images.add(img);
        }
        return images;
    }
}
