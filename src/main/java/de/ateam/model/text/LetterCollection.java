package main.java.de.ateam.model.text;

import main.java.de.ateam.utils.OpenCVUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class LetterCollection implements Serializable{
    private static final int LETTER_SIZE = 50;
    private HashMap<Character, Letter> letterMap;
    private FontMetrics metrics;
    private Graphics2D g2d;
    private Font font;
    private static char[] characters;

    static {
        //TODO Sollte am Besten ersetzt werden, durch die Buchstaben, die benötigt werden!
        characters = "#1234567890qwertzuiopüasdfghjklöäyxcvbnmQWERTZUIOPÜASDFGHJKLÖÄYXCVBNM?! -".toCharArray();
    }

    protected LetterCollection(Font font) {
        this.letterMap = new HashMap<>();
        this.font = font;

        long start = System.currentTimeMillis();
        this.letterMap.putAll(calculateLetters(characters, LETTER_SIZE));
        System.out.printf("Time to calculate %s letters : %sms\n", letterMap.size(), (System.currentTimeMillis() - start));
    }

    public HashMap<Character, Letter> calculateLetters(char[] characters){
        return calculateLetters(characters);
    }

    public HashMap<Character, Letter> calculateLetters(char[] characters, int height) {
        HashMap<Character, Letter> tempLetters = new HashMap<>();
        Canvas can = new Canvas();
        this.font = this.font.deriveFont((float) height);
        this.metrics =  can.getFontMetrics(this.font);
        int lineAsc = metrics.getMaxAscent();
        int lineDesc = metrics.getDescent();
        //Wenn man das gleich entfernt, funktionieren einige Schriften nicht mehr so hübsch, jedoch alle anderen sehr gut!
        if((metrics.getMaxAscent()+metrics.getDescent())>=metrics.getHeight()){
            lineAsc = metrics.getHeight();
        }
        System.out.printf("metrics.getAscent():%s\n, metrics.getDescent():%s\n, metrics.getHeight():%s\n, metrics.getLeading():%s\n, metrics.getMaxAscent():%s\n, metrics.getMaxDescent():%s\n", metrics.getAscent(), metrics.getDescent(), metrics.getHeight(), metrics.getLeading(), metrics.getMaxAscent(), metrics.getMaxDescent());


        //Das ist eigtl der rechenaufwändigste Part neben dem Algo später, dies sollte serialized werden ggfs. braucht für 72Buchstaben beim ersten Mal iwie lange, danach 50ms :D!!!!
        for(char c: characters) {
            BufferedImage tempBuf = new BufferedImage(this.metrics.charWidth(c), lineAsc, BufferedImage.TYPE_3BYTE_BGR);
            g2d = tempBuf.createGraphics();
            g2d.setFont(this.font);
            g2d.setColor(Color.WHITE);
            g2d.drawString(c+"", 0, lineAsc - lineDesc);
            tempLetters.put(c,new Letter(tempBuf,c));
        }


        return tempLetters;
    }

    public Letter getLetter(Character c) {
        if(this.letterMap.containsKey(c))
            return this.letterMap.get(c);
        this.letterMap.putAll(calculateLetters(new char[]{c}, LETTER_SIZE));
        return this.letterMap.get(c);
    }

    public BufferedImage drawBufFromString(String str){
        int width = 0;
        int maxHeight = 0;
        int height = 0;
        for(int i = 0; i < str.length(); i++){
            Letter let = getLetter(str.charAt(i));
            width+= let.getWidth()+let.getHeight()/25;
            maxHeight = (maxHeight<let.getHeight())?let.getHeight():maxHeight;
            if(str.charAt(i) == '\n') {
                if(height == 0)
                    height = maxHeight;
                height += maxHeight;
            }

        }
        if(height == 0)
            height = maxHeight;
        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = buf.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, buf.getWidth(), buf.getHeight());

        // TODO hab hier jez mal fix zeilenumbruch reingebaut...hier willste ja nur die maske zurückgeben wenn ich das
        // richtig verstanden habe. Da müsste man noch gucken, wenn man ne center prop hat, dass man oben berechnet wie weit für die jeweilige
        // line der initiale offset wert sein soll, damit man so schöne blöcke hat wie bei word beim zentrieren von Text. Huii
        int widthOffset = 0;
        int heightOffset = 0;
        for (int i = 0; i < str.length(); i++) {
            Letter let = getLetter(str.charAt(i));
            if(str.charAt(i) == '\n') {
                heightOffset += maxHeight;
                widthOffset = 0;
            }
            else {
                g2d.drawImage(OpenCVUtils.matToBufferedImage(let.getLetterMask()), widthOffset, heightOffset, null);
                widthOffset += let.getWidth() + (let.getHeight() / 25);
            }
        }

        return buf;
    }
}
