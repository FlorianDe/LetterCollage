package main.java.de.ateam.model.text;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.utils.FontLoader;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by viktorspadi on 15.11.15.
 */
public abstract class LetterFactory {
    private static HashMap<String, LetterCollection> fontCollection;
    private static LetterCollection lastLoadedFont;

    static{
        init();
    }

    public static void init(){
        fontCollection = new HashMap<>();
    }

    public static LetterCollection getCollection(String fontName) {
        if(fontCollection.containsKey(fontName)){
            lastLoadedFont = fontCollection.get(fontName);
            return lastLoadedFont;
        }
        Font font = FontLoader.getFont(fontName);
        if(font!=null) {
            lastLoadedFont = new LetterCollection(font);
            LetterFactory.fontCollection.put(fontName, lastLoadedFont);
        }
        return lastLoadedFont;
    }

    public static LetterCollection getLastLoadedFont() {
        return lastLoadedFont;
    }
}
