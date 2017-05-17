package rendering;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

public class Framework extends WindowAdapter {
    public int numWindows = 0;
    
    private Point lastLocation = null;
    private Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    private int padding = 40;
    private int maxX = screensize.width - padding;
    private int maxY = screensize.height - padding;
    
    public Window createWindow(boolean fullscreen) {
        Window window = new Window(this);
        numWindows++;
        
        if (fullscreen) {
            window.setLocation(0, 0);
            if (lastLocation == null) 
                lastLocation = window.getLocation();
            
            window.setSize(screensize);
            window.setUndecorated(true);
        } else {
            if (lastLocation != null) {
                lastLocation.translate(padding, padding);
                if (lastLocation.x > maxX) lastLocation.move(0, lastLocation.y);
                if (lastLocation.y > maxY) lastLocation.move(lastLocation.x, 0);
                window.setLocation(lastLocation);
            } else {
                lastLocation = window.getLocation();
            }
        }
        
        window.setResizable(!fullscreen);
        window.setVisible(true);
        return window;
    }
    
    public void quit(JFrame frame) {
        if (quitConfirmed(frame)) System.exit(0);
    }
    
    public boolean quitConfirmed(JFrame frame) {
        String quit = "Quit";
        String cancel = "Cancel";
        Object[] options = {quit, cancel};
        return JOptionPane.showOptionDialog(frame, 
                "Windows are still open.\nDo you really want to quit?",
                "Quit confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                quit) == JOptionPane.YES_OPTION;
    }
    
    @Override
    public void windowClosed(WindowEvent e) {
        numWindows --;
        if (numWindows <= 0) System.exit(0);
    }
}
