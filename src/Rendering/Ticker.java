package rendering;

import java.awt.Color;
import java.awt.Font;
import static java.awt.Font.PLAIN;
import java.awt.Graphics;
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
            System.out.println("it " + iteration + ": " + x);
            iteration ++;
            return x;
        }
        
    };
    
    public int NONE = 0;
    public int DELTA = 1;
    public int LINTV = 2;
    public int TIME = 4;
    public int TICK = 8;
    private int display = NONE|DELTA|LINTV|TIME|TICK;
    private String unit;
    
    Map<String, TableEntry> table = new HashMap<>();
    LinkedList<Long> intervals = new LinkedList<>();
    
    // tick = frame, delta = delta, intv = count, time = time
    public Ticker(String tick, String delta, String lintv, String time, String unit) {
        this.unit = unit;
        table.put("tick", new TableEntry(TICK, tick, () -> {
            return "" + counter;
        }));
        table.put("delta", new TableEntry(DELTA, delta, () -> {
           long d = deltaLastCounted;
           return d / 1000000L + "ms (" + (1000000000L / d) + " " + unit + ")";
        }));
        table.put("time", new TableEntry(TIME, time, () -> {
            long t = (long)(System.nanoTime() - timeAtStart);
            long ns = (t) % 1000000;
            long ms = (t /= 1000000) % 1000;
            long s = (t /= 1000) % 60;
            long m = (t /= 60) % 60;
            long h = (t /= 60);
            return
                (h > 0 ? h + ":" : "") +
                (m > 0 ? m + ":" : "") +
                (s > 0 ? s + "'" : "") +
                String.format("%3s\"",ms).replace(' ','0');
        }));
        table.put("lintv", new TableEntry(LINTV, lintv, () -> {
            return intervals.getLast() + " " + unit;
        }));
        
        resolution = 30;
        intervals.add(0L);
        timeAtStart = System.nanoTime();
        timeLastCounted = timeAtStart;
        
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
    
    public void display(int options) {
        display = options;
    }
    
    @Override 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.WHITE);
        Rectangle bounds = getBounds();
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
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
            
            g.drawString(label, MARGIN, height);
            g.drawString(value, cWidth, height);
        }
        
        return MARGIN * 2 + LINEHEIGHT * i;
    }

    private void paintGraph(Graphics g, int offsetY) {
        int offsetX = MARGIN;
        offsetY += offsetX;
        
        Iterator<Long> it;
        Rectangle graph = new Rectangle(offsetX, offsetY, GRAPHWIDTH, GRAPHHEIGHT);
        long maxValue = (long)Math.max(1, intervals.stream().max((a, b) -> (int)(a - b)).orElse(0L));
        
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
            g.drawLine(graph.x, graph.y + graphY, graph.x + graph.width, graph.y + graphY);
            
            g.setColor(Color.BLACK);
            g.drawString(str, graph.x + graph.width + MARGIN, graph.y + graphY + 4);
            
            System.out.println("ruler " + ruler);
            if (ruler == next) break;
        }
        
        // draw axes
        g.setColor(Color.GRAY);
        g.drawLine(graph.x, graph.y + graph.height, graph.x, graph.y);
        g.drawLine(graph.x, graph.y + graph.height, graph.x + graph.width, graph.y + graph.height);
        
        // draw data
        g.setColor(Color.BLACK);
        int lastX = -1;
        int lastY = -1;
        it = intervals.iterator();
        for (int i = 0; it.hasNext(); i++) {
            int pointX = (int)(graph.x + (1 - (intervals.size() - 1 - i) / (double)resolution) * graph.width);
            int pointY = graph.y + getGraphY(it.next());
            
            if (i > 0) g.drawLine(lastX, lastY, pointX, pointY);
            
            lastX = pointX;
            lastY = pointY;
        }
    }
    
    private int getGraphY(long value) {
        long max = (long)Math.max(1, intervals.stream().max((a, b) -> (int)(a - b)).orElse(0L));
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
    
    public interface Stringable {
        public String get();
    }
}
