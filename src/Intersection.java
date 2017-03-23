
import utility.Line;

public class Intersection {

Bubble bubble;
Line line;
double startR;
double endR;

    public Intersection(Bubble b, Line l, double start, double end){
        this.bubble=b;
        this.line = l;
        this.startR = start;
        this.endR = end;
    }
}
