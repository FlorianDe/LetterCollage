package main.java.de.ateam.model;

import main.java.de.ateam.model.text.Letter;
import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.model.text.LetterFactory;
import main.java.de.ateam.utils.FontLoader;

import java.awt.*;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class CollageModel {

    private LetterFactory letterFactory;
    private LetterCollection letterCollection;

    public CollageModel() {
        this.letterFactory = new LetterFactory();
    }

    public void loadFont(String font) {
        if(font != null)
            this.letterCollection = this.letterFactory.loadFont(font);
    }

    public String[] getAllFonts() {
        return FontLoader.getFonts();
    }

    public LetterCollection getLetterCollection() {
        return this.letterCollection;
    }
}
