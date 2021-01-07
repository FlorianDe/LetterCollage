package de.ateam.view.cstmcomponent.MThumbSlider;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Florian on 05.02.2016.
 */
class MThumbSlider extends JSlider {
    protected int thumbNum;

    protected BoundedRangeModel[] sliderModels;

    protected Icon[] thumbRenderers;

    protected Color[] fillColors;

    protected Color trackFillColor;

    private static final String uiClassID = "MThumbSliderUI";

    public MThumbSlider(int n) {
        createThumbs(n);
        updateUI();
    }

    protected void createThumbs(int n) {
        thumbNum = n;
        sliderModels = new BoundedRangeModel[n];
        thumbRenderers = new Icon[n];
        fillColors = new Color[n];
        for (int i = 0; i < n; i++) {
            sliderModels[i] = new DefaultBoundedRangeModel(50, 0, 0, 100);
            thumbRenderers[i] = null;
            fillColors[i] = null;
        }
    }

    public void updateUI() {
        AssistantUIManager.setUIName(this);
        super.updateUI();

    /*
     * // another way // updateLabelUIs();
     * setUI(AssistantUIManager.createUI(this)); //setUI(new
     * BasicMThumbSliderUI(this)); //setUI(new MetalMThumbSliderUI(this));
     * //setUI(new MotifMThumbSliderUI(this));
     */
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public int getThumbNum() {
        return thumbNum;
    }

    public int getValueAt(int index) {
        return getModelAt(index).getValue();
    }

    public void setValueAt(int n, int index) {
        getModelAt(index).setValue(n);
        // should I fire?
    }

    public int getMinimum() {
        return getModelAt(0).getMinimum();
    }

    public int getMaximum() {
        return getModelAt(0).getMaximum();
    }

    public BoundedRangeModel getModelAt(int index) {
        return sliderModels[index];
    }

    public Icon getThumbRendererAt(int index) {
        return thumbRenderers[index];
    }

    public void setThumbRendererAt(Icon icon, int index) {
        thumbRenderers[index] = icon;
    }

    public Color getFillColorAt(int index) {
        return fillColors[index];
    }

    public void setFillColorAt(Color color, int index) {
        fillColors[index] = color;
    }

    public Color getTrackFillColor() {
        return trackFillColor;
    }

    public void setTrackFillColor(Color color) {
        trackFillColor = color;
    }
}
