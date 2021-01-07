package de.ateam.controller.roi;

import java.util.ArrayList;

/**
 * Created by Florian on 12.01.2016.
 */
public class CalculationResultList extends ArrayList<CalculationResult>{
    private int imgIndex;
    private int letterIndex;

    public CalculationResultList(int imgIndex, int letterIndex){
        this.imgIndex = imgIndex;
        this.letterIndex = letterIndex;
    }

    public int getImgIndex() {
        return imgIndex;
    }

    public int getLetterIndex() {
        return letterIndex;
    }
}
