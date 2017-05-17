package core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;


public class Physics extends Thread {
    private List<Bubble> bubbles;
    
    private boolean running;
    private boolean shouldStop;
    private boolean shouldPause;
    private int shouldRun;
    
    private boolean actRandom = false;
    private boolean actOnDistance = true;
    private boolean actOnBubblePhysics = true;
    
    private Runnable onTick;
    private Runnable onStop;
    private Runnable onPause;
    private Runnable onResume;
    
    public Physics(List<Bubble> bubbles) {
        this.bubbles = bubbles;
        
        running = false;
        shouldStop = false;
        shouldPause = false;
        shouldRun = 0;
    }
    
    public void requestStop() {
        shouldStop = true;
    }
    
    public void requestPause() {
        shouldPause = true;
    }
    
    public void requestResume() {
        shouldPause = false;
    }
    
    public void requestSingleRun() {
        shouldRun ++;
    }
    
    long runtime = 0;
    Iterator<Double> dbl = new Random(0).doubles().iterator();
        
    @Override
    public void run() {
        running = true;
        while (!shouldStop) {
            if (shouldPause) {
                if (running) {
                    running = false;
                    onPause.run();
                }
                
                if (shouldRun > 0) 
                    shouldRun --;
                else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Physics.class.getName()).log(Level.SEVERE, null, ex);
                    } 

                    continue;
                }
            }
            if (!running) {
                running = true;
                onResume.run();
            }
            
            long time = System.nanoTime();
            
            singlePass();
            onTick.run();
            
            long delta = System.nanoTime() - time;
            runtime += delta;
        }
        onStop.run();
    }
    
    public void singlePass() {
        if (actRandom)
            bubbles.stream().forEach(b -> b.moveBubble(new utility.Vector(dbl.next() * 2 - 1, dbl.next() * 2 - 1)));
        else {
            if (actOnDistance) {
                // TODO: add distance metric interactions
            }
            if (actOnBubblePhysics) {
                // TODO: add bubble physics interactions
            }
        }
    }
    
    public void onTick(Runnable onTick) {
        this.onTick = onTick;
    }
    
    public void onStop(Runnable onStop) {
        this.onStop = onStop;
    }
    
    public void onPause(Runnable onPause) {
        this.onPause = onPause;
    }
    
    public void onResume(Runnable onResume) {
        this.onResume = onResume;
    }
    
    JMenu menu;
    public JMenu getMenu() {
        if (menu == null) {
            menu = new JMenu("core.Physics");
            menu.setMnemonic(KeyEvent.VK_P);
            ButtonGroup bg = new ButtonGroup();
            JMenuItem item, itemDM, itemBP;
            
            itemDM = new JCheckBoxMenuItem("Distance metric", true);
            itemDM.setMnemonic(KeyEvent.VK_D);
            itemDM.addActionListener((ActionEvent e) -> {
                actOnDistance = !actOnDistance;
            });
            
            itemBP = new JCheckBoxMenuItem("core.Bubble physics", true);
            itemBP.setMnemonic(KeyEvent.VK_B);
            itemBP.addActionListener((ActionEvent e) -> {
                actOnBubblePhysics = !actOnBubblePhysics;
            });
            
            item = new JRadioButtonMenuItem("Random");
            item.setMnemonic(KeyEvent.VK_R);
            item.addActionListener((ActionEvent e) -> {
                actRandom = true;
                itemDM.setEnabled(false);
                itemBP.setEnabled(false);
            });
            menu.add(item);
            bg.add(item);
            item.doClick();
            
            item = new JRadioButtonMenuItem("Deterministic");
            item.setMnemonic(KeyEvent.VK_D);
            item.addActionListener((ActionEvent e) -> {
                actRandom = false;
                itemDM.setEnabled(true);
                itemBP.setEnabled(true);
            });
            menu.add(item);
            bg.add(item);
            
            menu.add(itemDM);
            menu.add(itemBP);
        }
        return menu;
    }
}
