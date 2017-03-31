
import utility.Line;

public class Intersection {

Bubble b1, b2;
Line line;
//double startR;
//double endR;

    public Intersection(Bubble b1, Bubble b2, Line l){
        this.b1 = b1;
        this.b2 = b2;
        this.line = l;
//        this.startR = start;
//        this.endR = end;
    }
    
    @Override
    public boolean equals(Object o){
        Intersection i = (Intersection) o;
        boolean r = this.b1.equals(i.b1) && this.b2.equals(i.b2);
        r = r ||(this.b1.equals(i.b2) && this.b2.equals(i.b1));
        return r;
    }
}
