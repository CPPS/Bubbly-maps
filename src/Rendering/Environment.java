package rendering;

import java.awt.Color;
import java.awt.Font;
import static java.awt.Font.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
    
    List<Object> bubbles = new ArrayList<>();
    
    public Environment(List<Object> bubbles) {
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
        
        Random r = new Random(0);
        bubbles.stream().forEach((b) -> {
            Class c = b.getClass();
            try { // because Bubble is in default package.
                Field field;
                field = c.getDeclaredField("position");
                field.setAccessible(true);
                utility.Point pos = (utility.Point)field.get(b);
                
                field = c.getDeclaredField("radius");
                field.setAccessible(true);
                double rad = (double)field.get(b);
                
                String str = "b_" + r.ints(10000, 99999).iterator().next();
                
                g.drawArc((int)pos.getX() - (int)rad, (int)pos.getY() - (int)rad, (int)(rad * 2), (int)(rad * 2), 0, 360);
                g.drawString(str, (int)pos.getX() + 5, (int)pos.getY() - 5);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
