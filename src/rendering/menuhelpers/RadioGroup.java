package rendering.menuhelpers;

import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;

public class RadioGroup {
    State s = new State();
    List<JMenuItem> items = new ArrayList<>();
    ButtonGroup group = new ButtonGroup();

    public void addChoice(int key, String value) {
        addChoice(key, value, false);
    }
    
    public void addChoice(int key, String value, boolean active) {
        JMenuItem item = new JRadioButtonMenuItem(value);
        item.addActionListener((ActionEvent e) -> {
            s.setState(key);
        });
        group.add(item);
        items.add(item);
        
        if (active) item.doClick();
    }

    public void addTo(JMenu menu) {
        if (menu.getItemCount() > 0) menu.addSeparator();
        items.stream().forEach(item -> {
            menu.add(item);
        });
    }
    
    public void setActive(int i) {
        items.get(i).doClick();
    }
    
    public State getStateObject() { return s; }
    
    public class State {
        private int state = 0;
        private Listener listener;
        
        public int getState() { 
            return state; 
        }
        public void setState(int i) { 
            state = i; 
            if (listener != null) 
                listener.onEvent(i); 
        }
        public void setListener(Listener l) { listener = l; }
    }
    
    public interface Listener {
        public void onEvent(int i);
    }
}
