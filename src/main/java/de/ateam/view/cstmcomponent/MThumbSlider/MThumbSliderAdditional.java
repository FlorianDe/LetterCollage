package main.java.de.ateam.view.cstmcomponent.MThumbSlider;

import java.awt.*;

/**
 * Created by Florian on 05.02.2016.
 */
interface MThumbSliderAdditional {

    public Rectangle getTrackRect();

    public Dimension getThumbSize();

    public int xPositionForValue(int value);

    public int yPositionForValue(int value);

}
