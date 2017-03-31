package rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.PLAIN;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JComponent;

public class Ticker extends JComponent {
    public final int LINEHEIGHT = 20;
    public final int FONTSIZE = 16;
    public final int PADDING = 20;
    public final int MARGIN = 8;
    
    public final int GRAPHWIDTH = 200;
    public final int GRAPHHEIGHT = 80;
    
    private boolean paused;
    private long timePaused;
    
    private long counter = 0;
    private long timeAtStart = 0;
    
    private long timeLastCounted;
    private long deltaLastCounted;
    private long countLastInterval;
    private int resolution;
    private Iterable<Long> rulers = () -> new Iterator() {
        long rulers[] = new long[]{1, 15};
        int iteration = 0;
        
        @Override
        public boolean hasNext() {
            return true;
        }
        
        @Override
        public Object next() {
            long x = (iteration < rulers.length) 
                    ? rulers[iteration]
                    : rulers[rulers.length - 1] * (long)Math.pow(2, (iteration - rulers.length + 1));
            iteration ++;
            return x;
        }
    };
    
    public int NONE = 0;
    public int DELTA = 1;
    public int LINTV = 2;
    public int TIME = 4;
    public int TICK = 8;
    public int GRAPH = 16;
    private int display = NONE|DELTA|LINTV|TIME|TICK|GRAPH;
    private String unit;
    
    private Point origin;
    
    Map<String, TableEntry> table = new HashMap<>();
    LinkedList<Long> intervals = new LinkedList<>();
    
    // tick = frame, delta = delta, intv = count, time = time
    public Ticker(Point origin, String tick, String delta, String lintv, String time, String unit) {
        this(origin, tick, delta, lintv, time, unit, false);
    }
    
    public Ticker(Point origin, String tick, String delta, String lintv, String time, String unit, boolean startPaused) {
        this.origin = origin;
        this.unit = unit;
        table.put("tick", new TableEntry(TICK, tick, () -> {
            return "" + counter;
        }));
        table.put("delta", new TableEntry(DELTA, delta, () -> {
           long d = deltaLastCounted;
           return d / 1000000L + "ms (" + (d > 0 ? (1000000000L / d) : "Infinite") + " " + unit + ")";
        }));
        table.put("time", new TableEntry(TIME, time, () -> {
            long t = (long)((paused ? timePaused : System.nanoTime()) - timeAtStart);
            long ns = (t) % 1000000;
            long ms = (t /= 1000000) % 1000;
            long s = (t /= 1000) % 60;
            long m = (t /= 60) % 60;
            long h = (t /= 60);
            return
                (h > 0 ? h + ":" : "") +
                (m > 0 ? m + ":" : "") +
                (s > 0 ? s + "'" : "") +
                String.format("%3s\"",ms).replace(' ','0') + 
                String.format("%6s",ns).replace(' ','0');
        }));
        table.put("lintv", new TableEntry(LINTV, lintv, () -> {
            return intervals.getLast() + " " + unit;
        }));
        
        resolution = 30;
        intervals.add(0L);
        timeAtStart = System.nanoTime();
        timeLastCounted = timeAtStart;
        
        if (startPaused) {
            timePaused = timeAtStart;
            paused = true;
        }
        
//        setSize(new Dimension(GRAPHWIDTH + MARGIN * 2 + 40, getGraphY(0) + MARGIN));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
    
    public void count() {
        counter ++;
        long now = System.nanoTime();
        deltaLastCounted = now - timeLastCounted;
        timeLastCounted = now;
    }
    
    public void interval() {
        if (intervals.size() >= resolution)
            intervals.pop();
        intervals.add(counter - countLastInterval);
        countLastInterval = counter;
    }
    
    public void setResolution(int r) {
        resolution = r;
    }
    
    public void setOrigin(Point origin) {
        this.origin = origin;
    }
    
    public void display(int options) {
        display = options;
    }
    
    public void pause() {
        if (paused) return;
        paused = true;
        timePaused = System.nanoTime();
    }
    
    public void resume() {
        if (!paused) return;
        paused = false;
        long deltaPaused = System.nanoTime() - timePaused;
        
        timeAtStart += deltaPaused;
        timeLastCounted += deltaPaused;
    }
    
    private LinkedList<Long> getIntervals() {
        return ((LinkedList<Long>)intervals.clone());
    }
    
    @Override 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // background
        g.setColor(Color.WHITE);
        Rectangle bounds = getDisplayBounds();
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // border
        g.setColor(Color.BLACK);
//        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        paintGraph(g, paintTable(g));
    }

    private int paintTable(Graphics g) {
        List<TableEntry> displayables = table.values().stream()
                .filter(x -> (x.FLAG & display) > 0)
                .collect(Collectors.toList());
        
        int columnWidth = displayables.stream()
                .map(x -> x.label.length())
                .max((a, b) -> a - b)
                .orElse(0);
        
        int i = 0;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier New", PLAIN, FONTSIZE));
        for (TableEntry entry : displayables) { i++;
            String label = entry.label;
            String value = entry.value.get();
            
            int height = (int)(MARGIN + LINEHEIGHT * i - (LINEHEIGHT - FONTSIZE) * 0.5);
            int cWidth = (int)(MARGIN + 0.62 * FONTSIZE * columnWidth + PADDING);
            
            g.drawString(label, origin.x + MARGIN, origin.y + height);
            g.drawString(value, origin.x + cWidth, origin.y + height);
        }
        
        return MARGIN * 2 + LINEHEIGHT * i;
    }

    private void paintGraph(Graphics g, int offsetY) {
        if ((display & GRAPH) == 0) return;
        
        int offsetX = MARGIN;
        offsetY += offsetX;
        
        Iterator<Long> it;
        LinkedList<Long> itvs = getIntervals();
        Rectangle graph = new Rectangle(offsetX, offsetY, GRAPHWIDTH, GRAPHHEIGHT);
        long maxValue = (long)Math.max(1, itvs.stream().max((a, b) -> (int)(a - b)).orElse(0L));
        
        // draw rulers
        g.setColor(Color.LIGHT_GRAY);
        it = rulers.iterator();
        g.setFont(new Font("Courier New", Font.PLAIN, 12));
        for (long ruler = it.next(), next = -1; ruler <= maxValue; ruler = next) {
            if (getGraphY(ruler) > getGraphY((long)(maxValue * 0.1))) {
                next = it.next();
                continue;
            }
            
            String str = "" + ruler;
            if (it.hasNext()) {
                next = it.next();
                if (next > maxValue) 
                    str += unit;
            } else str += unit;
            
            int graphY = getGraphY(ruler);
            
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(
                origin.x + graph.x, origin.y + graph.y + graphY, 
                origin.x + graph.x + graph.width, origin.y + graph.y + graphY
            );
            
            g.setColor(Color.BLACK);
            g.drawString(str, 
                origin.x + graph.x + graph.width + MARGIN, 
                origin.y + graph.y + graphY + 4
            );
            
            if (ruler == next) break;
        }
        
        // draw axes
        g.setColor(Color.GRAY);
        g.drawLine(
            origin.x + graph.x, origin.y + graph.y + graph.height, 
            origin.x + graph.x, origin.y + graph.y
        );
        g.drawLine(
            origin.x + graph.x, origin.y + graph.y + graph.height, 
            origin.x + graph.x + graph.width, origin.y + graph.y + graph.height
        );
        
        // draw data
        g.setColor(Color.BLACK);
        int lastX = -1;
        int lastY = -1;
        it = itvs.iterator();
        for (int i = 0; it.hasNext(); i++) {
            int pointX = (int)(graph.x + (1 - (itvs.size() - 1 - i) / (double)resolution) * graph.width);
            int pointY = graph.y + getGraphY(it.next());
            
            if (i > 0) g.drawLine(
                origin.x + lastX, origin.y + lastY, 
                origin.x + pointX, origin.y + pointY
            );
            
            lastX = pointX;
            lastY = pointY;
        }
    }
    
    private int getGraphY(long value) {
        long max = (long)Math.max(1, getIntervals().stream().max((a, b) -> (int)(a - b)).orElse(0L));
        return (int)((1 - value / (double)max) * GRAPHHEIGHT);
    }
    
    private class TableEntry {
        public int FLAG;
        public String label;
        public Stringable value;
        
        public TableEntry(int flag, String label, Stringable value) {
            this.FLAG = flag;
            this.label = label;
            this.value = value;
        }
    }
    
    public Rectangle getDisplayBounds() {
        int height = 0;
        if ((display & (DELTA|LINTV|TIME|TICK)) > 0) {
            height += 2 * MARGIN;
            for (int i : new int[]{DELTA, LINTV, TIME, TICK})
                if ((display & i) > 0) height += LINEHEIGHT;
        }
        if ((display & GRAPH) > 0)
            height += 2 * MARGIN + GRAPHHEIGHT;
        
        int width = 0;
        List<TableEntry> displayables = table.values().stream()
                .filter(x -> (x.FLAG & display) > 0)
                .collect(Collectors.toList());
        int column0Width = displayables.stream()
                .map(x -> x.label.length())
                .max((a, b) -> a - b)
                .orElse(0);
        int column1Width = displayables.stream()
                .map(x -> x.value.get().length())
                .max((a, b) -> a - b)
                .orElse(0);
        width = Math.max(width, (int)(MARGIN * 2 + 0.62 * FONTSIZE * (column0Width + column1Width) + PADDING));
        
        int rulerWidth = 0;
        long maxValue = getIntervals().stream()
                .map(x -> ("" + x).length())
                .max((a, b) -> a - b)
                .orElse(0);
        Iterator<Long> it = rulers.iterator();
        for (long next = it.next(); next < maxValue; next = it.next())
            rulerWidth = Math.max(rulerWidth, ("" + next).length());
        width = Math.max(width, (int)(MARGIN * 3 + GRAPHWIDTH + 12 * (rulerWidth + unit.length())));
        
        return new Rectangle(origin, new Dimension(width, height));
    }
    
    public interface Stringable {
        public String get();
    }
}
