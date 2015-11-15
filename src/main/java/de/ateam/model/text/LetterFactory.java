package main.java.de.ateam.model.text;

import main.java.de.ateam.utils.FontLoader;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class LetterFactory {
    public LetterFactory() {

    }

    public static Font getFont(String name) {
        return FontLoader.getFont(name);
    }

    public LetterCollection loadFont(String font) {
        LetterCollection collection = new LetterCollection(FontLoader.getFont(font));
        return collection;
    }

}
