package main.java.de.ateam.utils;

/**
 * Created by Florian on 04.11.2015.
 */
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class FontLoader {
    private static TreeMap<String, Font> fonts;

    static {
        //TODO Remove (Just for test purposes!)
        String fontsss[] = getFonts();
        for(String font: fontsss) {
            System.out.println(font);
        }
    }

    public static Font getFont(String fontName) {
        Font font = fonts.get(fontName);
        if(fonts.containsKey(fontName)) {
            if(fonts.get(fontName) == null) {
                font = Font.decode(fontName);
                fonts.put(fontName, font);
            }
        }
        return font;
    }

    private static String[] getFonts() {
        String[] systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fonts = new TreeMap<>();
        String path = OSUtils.getResourcePathForOS("fonts");
        File folder = new File(path);
        for(File f: folder.listFiles()) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, f);
                GraphicsEnvironment ge =
                        GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                fonts.put(font.getName(), font);
            } catch (IOException|FontFormatException e) {
                System.out.printf("File %s is not a loadable Font!\n", f.getName());
            }
        }

        for(String systemFontName: systemFonts)
            fonts.put(systemFontName, null);

        return fonts.keySet().toArray(new String[fonts.size()]);
    }

    @Override
    public String toString() {
        return "FontLoader [toString()=" + fonts.toString() + "]";
    }


}
