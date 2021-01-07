package de.ateam.utils;

/**
 * Created by Florian on 04.11.2015.
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class FontLoader {
    private static TreeMap<String, Font> fonts;

    static {
        fonts = loadFontHelper();
    }

    public static Font getFont(String fontName) {
        Font font = fonts.get(fontName);
        if (fonts.containsKey(fontName)) {
            if (fonts.get(fontName) == null) {
                font = Font.decode(fontName);
                fonts.put(fontName, font);
            }
        }
        return font;
    }


    private static TreeMap<String, Font> loadFontHelper() {
        fonts = new TreeMap<>();
        String[] systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String path = OSUtils.getResourcePathForOS("fonts");
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, f);
                    GraphicsEnvironment ge =
                            GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(font);
                    fonts.put(font.getName(), font);
                } catch (IOException | FontFormatException e) {
                    System.out.printf("File %s is not a loadable Font!\n", f.getName());
                }
            }
        }

        for (String systemFontName : systemFonts) {
            fonts.put(systemFontName, null);
        }
        return fonts;
    }

    public static String[] reloadFonts() {
        return loadFontHelper().keySet().toArray(new String[fonts.size()]);
    }

    public static String[] loadFonts() {
        if (fonts == null) {
            return loadFontHelper().keySet().toArray(new String[fonts.size()]);
        }
        return fonts.keySet().toArray(new String[fonts.size()]);
    }

    @Override
    public String toString() {
        return "FontLoader [toString()=" + fonts.toString() + "]";
    }


}
