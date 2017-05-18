import java.awt.EventQueue;

import core.Bubble;
import core.Graph;
import core.Physics;
import rendering.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
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
import rendering.menuhelpers.RadioGroup;
import rendering.menuhelpers.RadioGroup.State;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }
    
    public final long preferredFPS = 60; // 1 to 1000
    
    public Framework framework;
    public JFrame window;
    public Canvas canvas;

    public Graph graph;
    public Physics physics;
    public List<Bubble> bubbles;

    public Environment environment;
    public Ticker tickerFPS;
    public Ticker tickerCPF;
    public State physicsState;
    
    public void run() {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        
        bubbles = getRandomBubbles(10);
//        bubbles = getInputBubbles(System.in);

        graph = new Graph(bubbles);
        
        long preferredInterval = 
                preferredFPS > 0 && preferredFPS <= 1000 
                    ? 1000L / preferredFPS 
                    : 1L;
        
        EventQueue.invokeLater(() -> {
            framework = new Framework();
            window = framework.createWindow(true);
//            window.setSize(1400, 800);
            window.setBounds(30, 30, 1400, 800);
            canvas = new Canvas();
            
            environment = new Environment(graph);
            
            Point origin;
            origin = new Point(0, 0);
            tickerFPS = new Ticker(origin, "frame", "delta", "count", "time", "FPS");
            
            Rectangle bounds = tickerFPS.getDisplayBounds();
            origin = new Point(bounds.x, bounds.y + bounds.height);
            tickerCPF = new Ticker(origin, "tick", "delta", "count", "time", "CPF", true);
            tickerCPF.setResolution(120);
            
            canvas.addLayer(1, tickerCPF);
            canvas.addLayer(1, tickerFPS);
            canvas.addLayer(2, environment);
            
            window.setContentPane(canvas);
            window.validate();
            window.repaint();
            
            physics = new Physics(environment);
            physics.onTick(() -> {
                tickerCPF.count();
            });
            physics.onPause(() -> {
                tickerCPF.pause();
            });
            physics.onResume(() -> {
                tickerCPF.resume();
            });

            JMenu menu = physics.getMenu();
            RadioGroup rg = new RadioGroup();
            physicsState = rg.getStateObject();
            physicsState.setListener(i -> {
                if (i == 0) {
                    physics.requestResume();
                } else {
                    physics.requestPause();
                    if (i == 3) {
                        physics.requestSingleRun();
                        rg.setActive(4);
                    }
                }
            });
            rg.addChoice(0, "Continuous");
            rg.addChoice(1, "Once per frame");
            rg.addChoice(2, "Once per second");
            rg.addChoice(3, "Single run");
            rg.addChoice(4, "Idle", true);
            rg.addTo(menu);
            physics.start();
            
            window.getJMenuBar().add(menu);
            
            java.util.Timer timer = new java.util.Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    tickerFPS.count();
                    tickerCPF.interval();
                    if (physicsState.getState() == 1) 
                        physics.requestSingleRun();
                    canvas.repaint();
                }
            }, 0L, preferredInterval);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (physicsState.getState() == 2)
                        physics.requestSingleRun();
                    tickerFPS.interval();
                }
            }, 0L, 1000L);
        });
    }
    
    public List<Bubble> getRandomBubbles(int nr) {
        List<Bubble> result = new ArrayList<>();
        Random r = new Random(0);
        Iterator<Double> dbl = r.doubles().iterator();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
//        result.add(new Bubble(900, 600, 120, 45240));
//        result.add(new Bubble(-Math.sqrt(3)/2*100 + 900, -50+500, 120, 45240));
//        result.add(new Bubble(Math.sqrt(3)/2*100 + 900,  -50 + 500, 120, 45240));
        for (int i = 0; i < nr; i++)
            result.add(new Bubble(screen.width * dbl.next(), screen.height * dbl.next(), 100 * dbl.next(), 10000 * dbl.next()*2));
        
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
