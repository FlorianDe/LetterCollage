package main.java.de.ateam.controller.roi;

import main.java.de.ateam.controller.ICollageController;

/**
 * Created by Florian on 17.11.2015.
 */
public class RegionOfInterestController {

    ICollageController controller;
    RegionOfInterestCalculator roiCalculator;
    RegionOfInterestDetector roiDetector;

    public RegionOfInterestCalculator getRoiCalculator() {
        return roiCalculator;
    }

    public void setRoiCalculator(RegionOfInterestCalculator roiCalculator) {
        this.roiCalculator = roiCalculator;
    }

    public RegionOfInterestDetector getRoiDetector() {
        return roiDetector;
    }

    public void setRoiDetector(RegionOfInterestDetector roiDetector) {
        this.roiDetector = roiDetector;
    }

    public RegionOfInterestController(ICollageController controller){
        this.controller = controller;
        this.roiCalculator = new RegionOfInterestCalculator(controller);
        this.roiDetector = new RegionOfInterestDetector(controller);
    }


}
