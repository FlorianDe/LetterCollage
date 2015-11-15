package main.java.de.ateam.model.text;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class LetterCollection implements Serializable{
    private HashMap<Character, Letter> letterMap;
    private FontMetrics metrics;
    private Graphics2D graphics;
    private Font font;
    private static char[] characters;

    static {
        String chars = "1234567890qwertzuiopüasdfghjklöäyxcvbnmQWERTZUIOPÜASDFGHJKLÖÄYXCVBNM?! -";
        characters = chars.toCharArray();
    }

    protected LetterCollection(Font font) {
        this.letterMap = new HashMap<>();
        this.font = font;
        calculateLetters(200);
    }

    public void calculateLetters(int height) {
        BufferedImage temp = new BufferedImage(height, height, BufferedImage.TYPE_3BYTE_BGR);
        graphics = temp.createGraphics();
        graphics.setFont(this.font);
        metrics = graphics.getFontMetrics();
        Rectangle2D rect;
        for(char c: characters) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0,0,height,height);
            graphics.setColor(Color.WHITE);
            graphics.drawString(""+c, 0,0);
            this.letterMap.put(c,new Letter(temp,c));
        }
    }

    public Letter getLetter(Character c) {
        return this.letterMap.get(c);
    }
}
