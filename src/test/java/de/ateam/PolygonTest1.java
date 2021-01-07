package de.ateam;

import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class PolygonTest1 extends JPanel{

    private Shape s;
    private Rectangle2D rect;

    public PolygonTest1() {
        Font f = getFont().deriveFont(Font.BOLD, 70);
        GlyphVector v = f.createGlyphVector(getFontMetrics(f).getFontRenderContext(), "Hello VIKTOR");
        s = v.getOutline();
        
        rect = new Rectangle(100,200);
        Area areaA = new Area(s);
        areaA.intersect(new Area(rect));
        
        
        setPreferredSize(new Dimension(700,500));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(100, 150);
        g2.rotate(0.4);
        g2.setPaint(Color.red);
        g2.fill(s);
    g2.setPaint(Color.black);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1, new float[]{1,0.4f,1.5f}, 0));
        g2.draw(s);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("ScrollTest");
        Component c = new PolygonTest1();
        f.getContentPane().add(c);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}