package main.java.de.ateam.model;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.FileLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Florian on 13.11.2015.
 */
public class ImageLoaderModel extends CstmObservable {
    private ArrayList<BufferedImage> loadedImages;

    public ImageLoaderModel(){
        this.loadedImages = new ArrayList<>();
        try {
            this.loadedImages.add(ImageIO.read(FileLoader.loadFile("img/1.jpg")));
            this.loadedImages.add(ImageIO.read(FileLoader.loadFile("img/2.gif")));
            this.loadedImages.add(ImageIO.read(FileLoader.loadFile("img/3.gif")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<BufferedImage> getLoadedImages() {
        return loadedImages;
    }

    public void setLoadedImages(ArrayList<BufferedImage> loadedImages) {
        this.loadedImages = loadedImages;
        this.setChanged();
        this.notifyObservers(null);
    }

    public void removeImage(BufferedImage img){
        this.loadedImages.remove(img);
        this.setChanged();
        this.notifyObservers(null);
    }
}
