package Rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;
import java.util.stream.IntStream;
import javax.swing.JComponent;

public class Ticker extends JComponent {
    public final int LINEHEIGHT = 20;
    public final int FONTSIZE = 16;
    public final int PADDING = 20;
    public final int MARGIN = 8;
    
    public final int RESOLUTION = 30; // time steps in history graph
    public final int INTERVAL = 1000; // ms per time step
    public final int RULERS[] = new int[]{1, 15, 30, 60};
    
    public final int GRAPHWIDTH = 200;
    public final int GRAPHHEIGHT = 80;

    public int iterations = 0;
    public long timeAtStart = System.nanoTime();
    public long lastUpdate = System.nanoTime();
    public int lastIterations = 0;
    
    Map<String, Stringable> table = new HashMap<>();
    LinkedList<Integer> history = new LinkedList<>();
    
    public Ticker() {
        table.put("frame", () -> {
            return "" + ++iterations;
        });
        table.put("delta", () -> {
           long timeNow = System.nanoTime();
           long delta = timeNow - lastUpdate;
           lastUpdate = timeNow;
           return delta / 1000000L + "ms (" + (1000000000L / delta) + " FPS)";
        });
        table.put("time", () -> {
            long time = (long)(System.nanoTime() - timeAtStart);
            long ns = (time) % 1000000;
            long ms = (time /= 1000000) % 1000;
            long s = (time /= 1000) % 60;
            long m = (time /= 60) % 60;
            long h = (time /= 60);
            return
                (h > 0 ? h + ":" : "") +
                (m > 0 ? m + ":" : "") +
                (s > 0 ? s + "'" : "") +
                String.format("%3s\"",ms).replace(' ','0');
        });
        table.put("count", () -> {
            return history.getLast() + " FPS";
        });
        
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (history.size() >= RESOLUTION)
                    history.pop();
                history.add(iterations - lastIterations);
                lastIterations = iterations;
            }
        }, 0L, INTERVAL);
        
        setSize(new Dimension(GRAPHWIDTH + MARGIN * 2 + 40, getGraphY(0) + MARGIN));
        setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getSize().width, getSize().height);
        paintTable(g);
        paintGraph(g);
    }
    
    private void paintTable(Graphics g) {
        int columnWidth = table.keySet().stream()
                .map((x) -> x.length())
                .max((a, b) -> a - b)
                .orElse(0);
        
        g.setFont(new Font("Courier New", PLAIN, FONTSIZE));
        
        Iterator<String> it = table.keySet().iterator();
        IntStream.range(0, table.size()).forEach(idx -> {
            String k = it.next();
            String v = table.get(k).get();
            
            int height = (int)(MARGIN + LINEHEIGHT * (idx + 1) - (LINEHEIGHT - FONTSIZE) * 0.5);
            int cWidth = (int)(MARGIN + 0.62 * FONTSIZE * columnWidth + PADDING);
            
            g.setColor(Color.BLACK);
            g.drawString(k, MARGIN, height);
            g.drawString(v, cWidth, height);
        });
    }
    
    private int getGraphMax() {
        return Math.max(1, history.stream().max((a, b) -> a - b).orElse(0));
    }
    
    private int getGraphY(int value) {
        int maxValue = getGraphMax();
        return (int)(MARGIN * 3 + LINEHEIGHT * table.size() + (1 - value / (double)maxValue) * GRAPHHEIGHT);
    }
    
    private void paintGraph(Graphics g) {
        int offsetY = (int)(MARGIN * 3 + LINEHEIGHT * table.size());
        int offsetX = (int)(MARGIN);
        
        // get max value
        int maxValue = getGraphMax();
        
        // draw rulers
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < RULERS.length && RULERS[i] <= maxValue; i++) {
            g.drawLine(offsetX, getGraphY(RULERS[i]), offsetX + GRAPHWIDTH, getGraphY(RULERS[i]));
        }
         
        // draw ruler labels
        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier New", PLAIN, 12));
        for (int i = 0; i < RULERS.length && RULERS[i] <= maxValue; i++) {
            String str = "" + RULERS[i];
            if (i + 1 < RULERS.length) {
                if (RULERS[i + 1] > maxValue) 
                    str += "FPS";
            } else str += "FPS";
            g.drawString(str, offsetX + GRAPHWIDTH + MARGIN, getGraphY(RULERS[i]) + 4);
        }
        
        // draw axes
        g.setColor(Color.GRAY);
        g.drawLine(offsetX, getGraphY(0), offsetX, getGraphY(maxValue));
        g.drawLine(offsetX, getGraphY(0), offsetX + GRAPHWIDTH, getGraphY(0));
        
        // draw line
        g.setColor(Color.BLACK);
        int lastX = -1;
        int lastY = -1;
        Iterator<Integer> it = history.iterator();
        for (int i = 0; it.hasNext(); i++) {
            int pointX = (int)(offsetX + (1 - (history.size() - 1 - i) / (double)RESOLUTION) * GRAPHWIDTH);
            int pointY = getGraphY(it.next());
            
            if (i > 0)
                g.drawLine(lastX, lastY, pointX, pointY);
            
            lastX = pointX;
            lastY = pointY;
        }
    }
    
    public interface Stringable {
        public String get();
    }
}
