package main.java.de.ateam.model.text;


import main.java.de.ateam.controller.ICollageController;
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
    private Graphics2D graphics;
    private Font font;
    private static char[] characters;

    static {
        String chars = "#1234567890qwertzuiopüasdfghjklöäyxcvbnmQWERTZUIOPÜASDFGHJKLÖÄYXCVBNM?! -";
        characters = chars.toCharArray();
    }

    protected LetterCollection(Font font) {
        this.letterMap = new HashMap<>();
        this.font = font;
        long start = System.currentTimeMillis();
        this.letterMap.putAll(calculateLetters(characters, LETTER_SIZE));
        System.out.printf("Time to calculate %s letters : %sms\n", letterMap.size(), (System.currentTimeMillis() - start));



        //TESTING PURPOSE!

        /*

        JFrame f = new JFrame();
        CJP p = new CJP();
        f.setLayout(new BorderLayout());
        f.add(p, BorderLayout.CENTER);
        f.setSize(this.metrics.getMaxAscent() * text.length(), this.metrics.getMaxAscent());
        f.setVisible(true);

        for (int i = 0; i < text.length(); i++) {
            p.drawImage(OpenCVUtils.matToBufferedImage(this.getLetter(text.charAt(i)).getLetterMask()));
        }
        */

    }

    public HashMap<Character, Letter> calculateLetters(char[] characters, int height) {
        HashMap<Character, Letter> tempLetters = new HashMap<>();
        Canvas can = new Canvas();
        this.font = this.font.deriveFont((float) height);
        this.metrics =  can.getFontMetrics(this.font);
        int lineAsc = metrics.getMaxAscent();
        //Wenn man das gleich entfernt, funktionieren einige Schriften nicht mehr so hübsch, jedoch alle anderen sehr gut!
        if((metrics.getMaxAscent()+metrics.getDescent())>=metrics.getHeight()){
            lineAsc = metrics.getHeight();
        }
        int lineDesc = metrics.getDescent();
        System.out.printf("metrics.getAscent():%s\n, metrics.getDescent():%s\n, metrics.getHeight():%s\n, metrics.getLeading():%s\n, metrics.getMaxAscent():%s\n, metrics.getMaxDescent():%s\n", metrics.getAscent(), metrics.getDescent(), metrics.getHeight(), metrics.getLeading(), metrics.getMaxAscent(), metrics.getMaxDescent());


        //Das ist eigtl der rechenaufwändigste Part neben dem Algo später, dies sollte serialized werden ggfs. braucht für 72Buchstaben beim ersten Mal iwie lange, danach 50ms :D!!!!
        for(char c: characters) {
            BufferedImage tempBuf = new BufferedImage(this.metrics.charWidth(c), lineAsc, BufferedImage.TYPE_3BYTE_BGR);
            graphics = tempBuf.createGraphics();
            graphics.setFont(this.font);
            graphics.setColor(Color.WHITE);
            graphics.drawString(c+"", 0, lineAsc - lineDesc);
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
        for(int i = 0; i < str.length(); i++){
            Letter let = getLetter(str.charAt(i));
            width+= let.getWidth()+let.getHeight()/25;
            maxHeight = (maxHeight<let.getHeight())?let.getHeight():maxHeight;
        }
        BufferedImage buf = new BufferedImage(width, maxHeight, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = buf.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, buf.getWidth(), buf.getHeight());

        int widhtOffset = 0;
        for (int i = 0; i < str.length(); i++) {
            Letter let = getLetter(str.charAt(i));
            g2d.drawImage(OpenCVUtils.matToBufferedImage(let.getLetterMask()), widhtOffset, 0, null);
            widhtOffset += let.getWidth() + (let.getHeight()/25);
        }

        return buf;
    }




    class CJP extends JPanel{
            private int widhtOffset;
            private ArrayList<BufferedImage> buffered;
            public CJP(){
                super();
                buffered = new ArrayList<>();
                widhtOffset = 0;
            }

            public void drawImage(BufferedImage buff){
                this.buffered.add(buff);
                this.setSize(buff.getWidth(), buff.getHeight());
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(buffered != null && buffered.size()>0) {
                    for(BufferedImage buf : buffered) {
                        g.drawImage(buf, widhtOffset, 0, null);
                        widhtOffset += buf.getWidth() + (buf.getHeight()/25);
                    }
                    widhtOffset = 0;
                }
            }
        }

}
