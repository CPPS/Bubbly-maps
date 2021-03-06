package core;

import rendering.Environment;
import utility.Vector;

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
    private Environment environment;
    
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
    
    public Physics(Environment environment) {
        this.environment = environment;
        
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
        List<Bubble> bubbles = environment.getGraph().getBubbles();

        if (actRandom)
            bubbles.stream().forEach(b -> b.setVelocity(new utility.Vector(dbl.next() * 2 - 1, dbl.next() * 2 - 1)));
        else {
            environment.getGraph().findIntersections();
            bubbles.forEach((b) -> b.setVelocity(0, 0));

            double effect = 0.001;

            if (actOnDistance) {
                for (int i = 0; i < bubbles.size(); i++) {
                    Bubble b1 = bubbles.get(i);

                    for (int j = i + 1; j < bubbles.size(); j++) {
                        Bubble b2 = bubbles.get(j);

                        double  distance = b1.position.distanceTo(b2.position),
                                delta = distance - 150; // TODO: set actual distance target

                        Vector diff = b1.getPosition().vectorTo(b2.getPosition()).normalize().scale(0.5 * delta).scale(effect);

                        b1.setVelocity(b1.getVelocity().plus(diff));
                        b2.setVelocity(b2.getVelocity().plus(diff.scale(-1)));
                    }
                }
            }
            if (actOnBubblePhysics) {
                // minimize edges
                bubbles.forEach((b) -> b.getIntersections().forEach(intersection -> {
                    Bubble b1 = intersection.b1,
                            b2 = intersection.b2;

                    double distance = intersection.b1.position.distanceTo(intersection.b2.position),
                            r1 = intersection.b1.radius,
                            r2 = intersection.b2.radius,
                            loc = Math.sqrt(r1*r1 + r1*r2 + r2*r2),
                            delta = distance - loc;

                    Vector diff = b1.position.vectorTo(b2.position).normalize().scale(0.5 * delta).scale(effect);

                    b1.setVelocity(b1.getVelocity().plus(diff));
                }));

                // preserve area;
                bubbles.forEach((b) -> {
                    double actualArea = b.bubbleArea();
                    double preferArea = b.area;

                    b.radius += ((Math.signum(preferArea - actualArea) * Math.sqrt(Math.abs(preferArea - actualArea))) * effect);
                });
            }
        }

        bubbles.forEach(b -> b.moveBubble(b.getVelocity()));
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
