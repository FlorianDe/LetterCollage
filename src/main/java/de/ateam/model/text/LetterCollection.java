package main.java.de.ateam.model.text;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.OpenCVUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by viktorspadi on 15.11.15.
 */
public class LetterCollection extends CstmObservable{
    public static int SAMPLER_MIN = 5;
    public static int SAMPLER_MAX = 80;
    public int LETTER_SIZE = 1000; //TODO ANPASSBAR ENTW MAX IMGLOADER HEIGHT ODER FIX!
    public int SAMPLER_SIZE = 16;
    private HashMap<Character, Letter> letterMap;
    private FontMetrics metricsResultImage;
    private Graphics2D g2d;
    private Font fontResultImage;
    private static char[] characters;

    int lineAscResultImage;
    int lineAscCalculateImage;
    int lineDescResultImage;
    int lineDescCalculateImage;
    int widthResultImage;
    int widthCalculateImage;

    ICollageController controller;

    static {
        //TODO Sollte am Besten ersetzt werden, durch die Buchstaben, die benötigt werden!
        characters = "AEIOU".toCharArray();
    }



    protected LetterCollection(Font font, ICollageController controller) {
        this.letterMap = new HashMap<>();
        this.fontResultImage = font;
        this.controller = controller;
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
        this.fontResultImage = this.fontResultImage.deriveFont((float) LETTER_SIZE);
        Font fontCalculateImage = this.fontResultImage.deriveFont((float) SAMPLER_SIZE);
        this.metricsResultImage =  can.getFontMetrics(this.fontResultImage);
        FontMetrics metricsCalculateImage =  can.getFontMetrics(fontCalculateImage);

        lineAscResultImage = metricsResultImage.getMaxAscent();
        lineAscCalculateImage = metricsCalculateImage.getMaxAscent();
        lineDescResultImage = metricsResultImage.getDescent();
        lineDescCalculateImage = metricsCalculateImage.getDescent();

        //Wenn man das gleich entfernt, funktionieren einige Schriften nicht mehr so hübsch, jedoch alle anderen sehr gut!
        if((metricsResultImage.getMaxAscent()+ metricsResultImage.getDescent())>= metricsResultImage.getHeight()){
            lineAscResultImage = metricsResultImage.getHeight();
        }
        if((metricsCalculateImage.getMaxAscent()+ metricsCalculateImage.getDescent())>= metricsCalculateImage.getHeight()){
            lineAscCalculateImage = metricsCalculateImage.getHeight();
        }
        //System.out.printf("metricsResultImage.getAscent():%s\n, metricsResultImage.getDescent():%s\n, metricsResultImage.getHeight():%s\n, metricsResultImage.getLeading():%s\n, metricsResultImage.getMaxAscent():%s\n, metricsResultImage.getMaxDescent():%s\n", metricsResultImage.getAscent(), metricsResultImage.getDescent(), metricsResultImage.getHeight(), metricsResultImage.getLeading(), metricsResultImage.getMaxAscent(), metricsResultImage.getMaxDescent());


        //Das ist eigtl der rechenaufwändigste Part neben dem Algo später, dies sollte serialized werden ggfs. braucht für 72Buchstaben beim ersten Mal iwie lange, danach 50ms :D!!!!
        for(char c: characters) {
            // Workaround weil scheinbar unter linux viele schriften einfach 0 bei charWidth zurückgeben... Aber durch das croppen kommen dennoch verwertbare schrifen raus
            widthResultImage = this.metricsResultImage.charWidth(c);
            if(widthResultImage == 0){
                widthResultImage = height;
            }
            widthCalculateImage = metricsCalculateImage.charWidth(c);
            if(widthCalculateImage == 0){
                widthCalculateImage = height;
            }
            BufferedImage bufResultImage = new BufferedImage(widthResultImage, fontResultImage.getSize(), BufferedImage.TYPE_BYTE_GRAY);
            g2d = bufResultImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            g2d.setColor(Color.WHITE);
            g2d.setFont(this.fontResultImage);
            g2d.drawString(c+"", 0, lineAscResultImage - lineDescResultImage);
            g2d.dispose();

            BufferedImage bufOutlineResultImage  = drawBorderedLetter(c, Color.BLACK, 10,  widthResultImage, lineAscResultImage, lineDescResultImage);

            BufferedImage bufCalculateImage = new BufferedImage(widthCalculateImage, fontCalculateImage.getSize(), BufferedImage.TYPE_3BYTE_BGR);
            g2d = bufCalculateImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            g2d.setColor(Color.WHITE);
            g2d.setFont(fontCalculateImage);
            g2d.drawString(c+"", 0, lineAscCalculateImage - lineDescCalculateImage);
            g2d.dispose();


            tempLetters.put(c,new Letter(c,bufResultImage,bufCalculateImage, bufOutlineResultImage));
        }


        return tempLetters;
    }

    public BufferedImage calculateResultBorders(char c, Color color, int thickness){
        Canvas can = new Canvas();
        this.fontResultImage = this.fontResultImage.deriveFont((float) LETTER_SIZE);
        this.metricsResultImage =  can.getFontMetrics(this.fontResultImage);

        lineAscResultImage = metricsResultImage.getMaxAscent();
        lineDescResultImage = metricsResultImage.getDescent();

        //Wenn man das gleich entfernt, funktionieren einige Schriften nicht mehr so hübsch, jedoch alle anderen sehr gut!
        if((metricsResultImage.getMaxAscent()+ metricsResultImage.getDescent())>= metricsResultImage.getHeight()){
            lineAscResultImage = metricsResultImage.getHeight();
        }

        // Workaround weil scheinbar unter linux viele schriften einfach 0 bei charWidth zurückgeben... Aber durch das croppen kommen dennoch verwertbare schrifen raus
        widthResultImage = this.metricsResultImage.charWidth(c);
        if (widthResultImage == 0) {
            widthResultImage = LETTER_SIZE;
        }

        return drawBorderedLetter(c, color, thickness,  widthResultImage, lineAscResultImage, lineDescResultImage);
    }

    public BufferedImage drawBorderedLetter(char c, Color borderColor, int thickness, int widthResultImage, int lineAscResultImage, int lineDescResultImage){
        BufferedImage bufOutlineResultImage = new BufferedImage(widthResultImage, fontResultImage.getSize(), BufferedImage.TYPE_4BYTE_ABGR);
        g2d = bufOutlineResultImage.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, bufOutlineResultImage.getWidth(), bufOutlineResultImage.getHeight());
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setFont(this.fontResultImage);
        TextLayout tl = new TextLayout(c+"",this.fontResultImage, g2d.getFontRenderContext());
        Shape shape = tl.getOutline(null);
        g2d.translate(0,lineAscResultImage - lineDescResultImage);
        g2d.draw(shape);
        g2d.dispose();

        return bufOutlineResultImage;
    }

    public Letter getLetter(Character c) {
        if(this.letterMap.containsKey(c))
            return this.letterMap.get(c);
        this.letterMap.putAll(calculateLetters(new char[]{c}, LETTER_SIZE));
        return this.letterMap.get(c);
    }

    public BufferedImage drawBufFromString(String str){
        int maxWidth = 0;
        int width = 0;
        int maxHeight = 0;
        int height = 0;
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '\n') {
                if(height == 0)
                    height = maxHeight;
                height += maxHeight;
                width = 0;
            }
            Letter let = getLetter(str.charAt(i));
            width+= let.getResultMask().getWidth()+let.getResultMask().getHeight()/25;
            maxWidth = (width > maxWidth)?width:maxWidth;
            maxHeight = (maxHeight<let.getResultMask().getHeight())?let.getResultMask().getHeight():maxHeight;
        }
        if(height == 0)
            height = maxHeight;
        BufferedImage buf = new BufferedImage(maxWidth, height, BufferedImage.TYPE_3BYTE_BGR);

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
                g2d.drawImage(let.getResultMask(), widthOffset, heightOffset, null);
                widthOffset += let.getResultMask().getWidth() + (let.getResultMask().getHeight() / 25);
            }
        }

        return buf;
    }

    public FontMetrics getMetricsResultImage() {
        return metricsResultImage;
    }

    public Font getFontResultImage() {
        return fontResultImage;
    }

    public void setLetterSize(int letterSize) {
        if(letterSize!=LETTER_SIZE && letterSize>0) {
            LETTER_SIZE = letterSize;
            this.setChanged();
            this.notifyObservers(null);
        }
    }


    public void setSamplerSize(int samplerSize) {
        if(samplerSize!=SAMPLER_SIZE && samplerSize>0) {
            SAMPLER_SIZE = samplerSize;
            this.setChanged();
            this.notifyObservers(null);
        }
    }

    public int getLETTER_SIZE() {
        return LETTER_SIZE;
    }

    public int getSAMPLER_SIZE() {
        return SAMPLER_SIZE;
    }

    public HashMap<Character, Letter> getLetterMap() {
        return letterMap;
    }

    public int getLineAscResultImage() {
        return lineAscResultImage;
    }

    public int getLineAscCalculateImage() {
        return lineAscCalculateImage;
    }

    public int getLineDescResultImage() {
        return lineDescResultImage;
    }

    public int getLineDescCalculateImage() {
        return lineDescCalculateImage;
    }

    public int getWidthResultImage() {
        return widthResultImage;
    }

    public int getWidthCalculateImage() {
        return widthCalculateImage;
    }
}
