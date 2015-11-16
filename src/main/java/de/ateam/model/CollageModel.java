package main.java.de.ateam.model;

import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.model.text.LetterFactory;


/**
 * Created by viktorspadi on 15.11.15.
 */
public class CollageModel {

    private LetterCollection letterCollection;

    public CollageModel() {

    }

    public void loadFont(String fontName) {
        if(fontName != null)
            this.letterCollection = LetterFactory.getCollection(fontName);
    }

    public LetterCollection getLetterCollection() {
        return this.letterCollection;
    }
}
