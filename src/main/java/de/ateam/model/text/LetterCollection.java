package main.java.de.ateam.model.text;


import javax.swing.*;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
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
        class CJP extends JPanel{
            private BufferedImage buff;
            public CJP(){
                super();
            }

            public void drawImage(BufferedImage buff){
                this.buff = buff;
                this.setSize(buff.getWidth(), buff.getHeight());
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(buff != null)
                    g.drawImage(buff, 0, 0, null);
            }
        }


        Canvas can = new Canvas();
        this.font = this.font.deriveFont((float)height);
        this.metrics =  can.getFontMetrics(this.font);

        JFrame f = new JFrame();
        CJP p = new CJP();
        f.setLayout(new BorderLayout());
        f.add(p, BorderLayout.CENTER);
        f.setSize(this.metrics.getMaxAscent()*4,this.metrics.getMaxAscent());
        f.setVisible(true);

        BufferedImage temp = new BufferedImage(this.metrics.getMaxAscent()*4, this.metrics.getMaxAscent(), BufferedImage.TYPE_3BYTE_BGR);
        graphics = temp.createGraphics();
        graphics.setFont(this.font);
        metrics = graphics.getFontMetrics();
        Rectangle2D rect;
        int counter = 0;
        for(char c: new char[]{'A', 'g', 'q'}) {

            //graphics.setColor(Color.BLACK);
            //graphics.fillRect(0,0,height,height);
            graphics.setColor(Color.WHITE);
            graphics.drawString(""+c, counter,this.metrics.getAscent()-this.metrics.getDescent());
            counter += this.metrics.charWidth(c);
            p.drawImage(temp);

            //this.letterMap.put(c,new Letter(temp,c));
        }
    }

    public Letter getLetter(Character c) {
        return this.letterMap.get(c);
    }
}
