package main.java.de.ateam.model.roi;

import main.java.de.ateam.utils.CstmObservable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by vspadi on 16.11.15.
 */
public class RegionOfInterestImageCollection extends CstmObservable {
    private boolean autoArrange = false;
    private ArrayList<RegionOfInterestImage> roiImages;

    public RegionOfInterestImageCollection() {
        this.roiImages = new ArrayList<>();

    }

    /*Brauchen wir hier eigtl nicht, dafür haben wir unser RoiModel
    public void setLetterCollection(LetterCollection letterCollection) {
        this.letterCollection = letterCollection;
        // evtl direkt berechnen, sonst irgendwo button einbauen der called
        if(regions.size() > 0 && autoArrange)
            arrangeImages();
    }
    */

    public void addImage(BufferedImage image) {
        roiImages.add(new RegionOfInterestImage(image));
        this.setChanged();
        this.notifyObservers(null);
    }

    public void addImage(RegionOfInterestImage roiImage) {
        roiImages.add(roiImage);
        this.setChanged();
        this.notifyObservers(null);
    }

    public boolean removeImage(RegionOfInterestImage roiImage) {
        if(roiImages.contains(roiImage)) {
            roiImages.remove(roiImage);
            if(autoArrange) {
                arrangeImages();
            }
            this.setChanged();
            this.notifyObservers(null);
            return true;
        }
        return false;
    }

    public RegionOfInterestImage getImage(int index) {
        if(0 < index && index < roiImages.size())
            return roiImages.get(index);
        return null;
    }

    public void arrangeImages() {
        // hier heftige magic um bilder anzuordnen würde ich mal behaupten
        this.setChanged();
        this.notifyObservers(null);
    }

    public ArrayList<RegionOfInterestImage> getRoiImages() {
        return roiImages;
    }

    public int getSize(){
        return this.getRoiImages().size();
    }

    public boolean isArrangeable(){
        return (this.getSize() > 0 && this.autoArrange);
    }

    public RegionOfInterestImage getLastAddedImage(){
        if(roiImages!= null && roiImages.size() > 0){
            return roiImages.get(roiImages.size()-1);
        }
        return null;
    }
}
