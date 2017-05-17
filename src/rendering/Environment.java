package rendering;

import core.Bubble;
import utility.Point;

import java.awt.Color;
import java.awt.Font;
import static java.awt.Font.*;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

public class Environment extends JComponent {
    public Point translate;
    public Point scale;
    public double rotate;
    
    List<Bubble> bubbles = new ArrayList<>();
    
    public Environment(List<Bubble> bubbles) {
        this.bubbles = bubbles;
        
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        translate = new Point(0, 0);
        scale = new Point(1, 1);
        rotate = 0;
        
        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setFont(new Font("Courier New", PLAIN, 12));
        g.setColor(Color.BLACK);
        
        Iterator<Integer> ids = new Random(0).ints(10000, 99999).iterator();

        bubbles.stream().forEach((b) -> {
            Point pos = b.getPosition();
            double rad = b.getRadius();

            g.drawArc((int)(pos.getX() - rad), (int)(pos.getY() - rad), (int)(rad * 2), (int)(rad * 2), 0, 360);
            g.drawString("b_" + ids.next(), (int)pos.getX() + 5, (int)pos.getY() - 5);
        });
    }
}
