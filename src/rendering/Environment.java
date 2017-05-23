package rendering;

import core.Bubble;
import core.Graph;
import core.Intersection;
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
    
    Graph graph;

    public Environment(Graph graph) {
        this.graph = graph;
        
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        translate = new Point(0, 0);
        scale = new Point(1, 1);
        rotate = 0;
        
        setVisible(true);
    }

    public Graph getGraph() {
        return this.graph;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setFont(new Font("Courier New", PLAIN, 12));
        g.setColor(Color.BLACK);
        
        Iterator<Integer> ids = new Random(0).ints(10000, 99999).iterator();

        graph.getBubbles().forEach((b) -> {
            Point pos = b.getPosition();
            double rad = b.getRadius();

            // concurrent modification because render loop is separate and may conflict with physics loop
            // TODO: fix concurrency between renderer and physics.
            List<Intersection> kevin = b.getIntersections();
            for (int j = 0; j < kevin.size(); j++) {
                Intersection i = kevin.get(j);
                g.drawLine((int) i.line.p1.getX(), (int) i.line.p1.getY(), (int) i.line.p2.getX(), (int) i.line.p2.getY());
            }

            g.drawArc((int)(pos.getX() - rad), (int)(pos.getY() - rad), (int)(rad * 2), (int)(rad * 2), 0, 360);
            g.drawString("b_" + ids.next(), (int)pos.getX() + 5, (int)pos.getY() - 5);
        });
    }
}
