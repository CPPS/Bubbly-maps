import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Physics extends Thread {
    private List<Bubble> bubbles;
    
    private boolean running;
    private boolean shouldPause;
    private Runnable onTick;
    
    public Physics(List<Bubble> bubbles) {
        this.bubbles = bubbles;
        
        running = false;
        shouldPause = false;
    }
    
    public void requestPause() {
        shouldPause = true;
    }
    
    public void requestStart() {
        shouldPause = false;
        if (!running) run();
    }
    
    Iterator<Double> dbl = new Random(0).doubles().iterator();
        
    @Override
    public void run() {
        running = true;
        
        while (!shouldPause) {
            singlePass();
            onTick.run();
        }
        
        running = false;
    }
    
    public void singlePass() {
        for (int i = 0; i < bubbles.size(); i++)
            bubbles.get(i).moveBubble(new utility.Vector(dbl.next() * 2 - 1, dbl.next() * 2 - 1));
    }
    
    public void onTick(Runnable onTick) {
        this.onTick = onTick;
    }
}
