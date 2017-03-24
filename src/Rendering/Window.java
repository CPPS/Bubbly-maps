package rendering;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class Window extends JFrame {
    protected Dimension defaultSize = new Dimension(800, 600);
    protected Framework framework = null;
    
    public Window(Framework controller) {
        super("Bubbly-maps");
        framework = controller;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(framework);
        
        createDefaultMenu();
        
        setSize(defaultSize);
    }
    
    protected class MenuItem {
        public String label;
        public int key;
        public ActionListener listener;
        
        public MenuItem(String lb, int k, ActionListener ls) {
            label = lb;
            key = k;
            listener = ls;
        }
    }
    
    private void createDefaultMenu() {
        JMenu menu = new JMenu("Window");
        menu.setMnemonic(KeyEvent.VK_W);
        JMenuItem item;
        
        item = new JMenuItem("Close window");
        item.setMnemonic(KeyEvent.VK_C);
        item.addActionListener((ActionEvent e) -> {
            Window.this.setVisible(false);
            Window.this.dispose();
        });
        menu.add(item);
        
        item = new JMenuItem("New window");
        item.setMnemonic(KeyEvent.VK_N);
        item.addActionListener((ActionEvent e) -> {
            framework.createWindow(false);
        });
        menu.add(item);
        
        menu.addSeparator();
        item = new JMenuItem("Quit");
        item.setMnemonic(KeyEvent.VK_Q);
        item.addActionListener((ActionEvent e) -> {
            framework.quit(Window.this);
        });
        menu.add(item);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
}