import java.awt.EventQueue;
import rendering.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }
    
    public final long preferredFPS = 60; // 1 to 1000
    
    public Framework framework;
    public JFrame window;
    public Canvas canvas;
    
    public Physics physics;
    public List<Bubble> bubbles;
    
    public Environment environment;
    public Ticker ticker;
    
    public void run() {
        bubbles = getRandomBubbles(10);
//        bubbles = getInputBubbles(System.in);
        
        long preferredInterval = 
                preferredFPS > 0 && preferredFPS <= 1000 
                    ? 1000L / preferredFPS 
                    : 1L;
        
        EventQueue.invokeLater(() -> {
            framework = new Framework();
            window = framework.createWindow(true);
            canvas = new Canvas();
            
            environment = new Environment(new ArrayList<>(bubbles));
            ticker = new Ticker();
            
            canvas.addLayer(0, environment);
            canvas.addLayer(2, ticker);
            
            window.setContentPane(canvas);
            window.validate();
            window.repaint();
            
            java.util.Timer timer = new java.util.Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    canvas.repaint();
                }
            }, 0L, preferredInterval);
            
            physics = new Physics(bubbles);
            physics.start();
        });
    }
    
    public List<Bubble> getRandomBubbles(int nr) {
        List<Bubble> result = new ArrayList<>();
        Random r = new Random(0);
        Iterator<Double> dbl = r.doubles().iterator();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        
        for (int i = 0; i < nr; i++)
            result.add(new Bubble(screen.width * dbl.next(), screen.height * dbl.next(), 100 * dbl.next(), 200 * dbl.next()));
        
        return result;
    }
    
    public List<Bubble> getInputBubbles(InputStream stream) {
        InputReader in = new InputReader(stream);
        List<Bubble> result = new ArrayList<>();
        int size = in.nextInt();
        
        for (int i = 0; i < size; i++)
            result.add(new Bubble(in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble()));
        
        return result;
    }
    
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }
    }
}
