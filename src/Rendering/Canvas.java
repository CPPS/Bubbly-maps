package rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

public class Canvas extends JPanel {
    public JLayeredPane layeredPane;
    
    public Canvas() {
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {}

            @Override
            public void mousePressed(MouseEvent me) {}

            @Override
            public void mouseReleased(MouseEvent me) {}

            @Override
            public void mouseEntered(MouseEvent me) {}

            @Override
            public void mouseExited(MouseEvent me) {}
        });
        
        addMouseWheelListener((MouseWheelEvent mwe) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });
        
        setBackground(Color.WHITE);
        add(layeredPane);
    }
    
    public void addLayer(int level, JComponent cmp) {
        layeredPane.add(cmp, level);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // draw background
        Dimension size = getSize();
        g.clearRect(0, 0, size.width, size.height);
    }
}
