
import static java.lang.Math.*;
import java.util.ArrayList;
import utility.*;



public class Graph {
    private ArrayList<Bubble> bubbles;
    private final double EPS = 0.000001;
    public Graph (ArrayList bubbles){
     this.bubbles = bubbles;   
    }
 
    public void  findIntersecions(){
        for (int i=0 ; i < bubbles.size(); i++){
           bubbles.get(i).intersections = bubbleIntersections(i); 
        }
    }
    
    public ArrayList<Pair<Bubble, Line>> bubbleIntersections(int b){
        ArrayList<Pair<Bubble, Line>> result = new ArrayList<>();
        Bubble b1,b2;
        b1= bubbles.get(b);
        for (int i=0; i < bubbles.size(); i++){
            if (i == b){
                continue;
            }
            b2 = bubbles.get(i);
            if (b1.position.distanceTo(b2.position)-(b1.radius+b2.radius) > EPS){
                
            }
        }
        return result;
    }
    
    public Pair<Point, Point> circleIntersections(Bubble b1, Bubble b2){
        Point p1,p2,c1,c2;
        if(b1.radius > b2.radius){
            
        }
        double r1 = Math.max(b1.radius, b2.radius);
        double r2 = Math.min(b1.radius, b2.radius);
        double d = b1.position.distanceTo(b2.position);
        double a = pow(r1,2) - pow(r2,2) + pow(d,2);
        a /= 2d;
        double h = sqrt(pow(r1,2) - pow(a,2));
        return new Pair(null, null);
    }
}
