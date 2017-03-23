
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {

    private static InputReader in;
    private static PrintWriter out;
    private static double distances[][];
    
    public static void main(String args[]) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);
        Main m = new Main();
        m.getInput();
    }
    
    void getInput(){
        int size = in.nextInt();
        
        for (int i = 0; i < size; i++){
            for (int j=0; j < size; j++){
                distances[i][j]= in.nextDouble();
            }
        }
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
            return Long.parseLong (next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }
    }
}
