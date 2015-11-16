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
    private static String lastSelectedFont = null;

    static{
        init();
    }

    public static void init(){
        fontCollection = new HashMap<>();
    }

    public static LetterCollection getCollection(String fontName) {
        lastSelectedFont = fontName;
        if(fontCollection.containsKey(fontName)){
            return fontCollection.get(fontName);
        }
        LetterCollection collection = new LetterCollection(FontLoader.getFont(fontName));
        LetterFactory.fontCollection.put(fontName, collection);
        return collection;
    }
}
