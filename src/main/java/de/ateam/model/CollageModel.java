package main.java.de.ateam.model;

/**
 * Created by Florian on 16.11.2015.
 */
public class CollageModel {
    private ResultImageModel resultImageModel;
    private RoiModel roiModel;


    public CollageModel(){
        this.resultImageModel = new ResultImageModel();
        this.roiModel = new RoiModel();
    }

    public ResultImageModel getResultImageModel() {
        return resultImageModel;
    }

    public void setResultImageModel(ResultImageModel resultImageModel) {
        this.resultImageModel = resultImageModel;
    }

    public RoiModel getRoiModel() {
        return roiModel;
    }

    public void setRoiModel(RoiModel roiModel) {
        this.roiModel = roiModel;
    }
}
