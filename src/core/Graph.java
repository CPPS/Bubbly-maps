package core;

import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;
import utility.*;



public class Graph {
    private List<Bubble> bubbles;
    private final double EPS = 0.000001;
    
   
    public Graph (List bubbles){
     this.bubbles = bubbles;   
     findIntersections();
    }
 
    public void findIntersections(){
        for (int i=0 ; i < bubbles.size(); i++){
           bubbles.get(i).intersections = bubbleIntersections(i); 
        }
        for (int i=0 ; i < bubbles.size(); i++){
            if (i != 0){
                Math.abs(1);
            }
            fixIntersections(bubbles.get(i));
        }

//        fixIntersections(bubbles.get(2));
    }
    
    public List<Intersection> bubbleIntersections(int b){
        List<Intersection> result = new ArrayList<>();
        Bubble b1,b2;
        b1= bubbles.get(b);
        for (int i=0; i < bubbles.size(); i++){
            if (i == b) continue;
            b2 = bubbles.get(i);
            double d = b1.position.distanceTo(b2.position);
            if (d - (b1.radius + b2.radius) < EPS && d - abs(b1.radius - b2.radius) > EPS){
                Pair<Point, Point> p = circleIntersections(b1, b2);
                result.add(new Intersection (b1, b2, new Line(p.first, p.second)));
            }
        }
        return result;
    }
    
    public Pair<Point, Point> circleIntersections(Bubble b1, Bubble b2){
        Point p1,p2,c1,c2;
        double r1, r2;
        if(b1.radius > b2.radius){
            r1 = b1.radius;
            c1 = b1.position;
            r2 = b2.radius;
            c2 = b2.position;
        } else {
            r2 = b1.radius;
            c2 = b1.position;
            r1 = b2.radius;
            c1 = b2.position;
        }
        
        double d = c1.distanceTo(c2);
        double a = pow(r1,2) - pow(r2,2) + pow(d,2);
        a /= (2*d);
        double h = sqrt(pow(r1,2) - pow(a,2));
        Vector ab = new Vector (c2.getX() - c1.getX(), c2.getY() - c1.getY());
        ab = ab.normalize();
        Vector v = new Vector(-ab.getY(), ab.getX());
        v = v.scale(h);
        ab = ab.scale(a);
        
        p1 = c1.plus(ab);
        p1 = p1.plus(v);
        
        p2 = c1.plus(ab);
        p2 = p2.minus(v);
        return new Pair(p1, p2);
    }
    
    public void fixIntersections(Bubble bubble){
        Point intersection;
        for (int k = 0; k < bubble.intersections.size(); k++ ){
            Intersection i = bubble.intersections.get(k);
            for (int z =k+1; z < bubble.intersections.size(); z++){
                Intersection in = bubble.intersections.get(z);
                intersection = i.line.intersects(in.line);
                if (intersection != null || true){
                    Bubble b;
                    Point p;
                    //fix i
                    b = i.b1.equals(bubble) ? in.b2 : in.b1;
                    Line l = new Line (bubble.position, i.line.p1);
                    Line ll = new Line (bubble.position, i.line.p2);
                    Point dummy1, dummy2;
                    dummy1 = l.intersects(in.line);
                    dummy2 = ll.intersects(in.line);
                    if ( dummy1!= null && dummy2!= null){
                        bubble.intersections.remove(k--);
                    }
                    else {
                        if (intersection != null) {
                            if (dummy2 != null) {
                                i.line = new Line(i.line.p1, intersection);
                            } else {
                                i.line = new Line(i.line.p2, intersection);
                            }
                        }
                    }
                    //fix in
                    b = in.b1.equals(bubble) ? i.b2 : i.b1;
                    l = new Line (bubble.position, in.line.p1);
                    ll = new Line (bubble.position, in.line.p2);
                    dummy1 = l.intersects(i.line);
                    dummy2 = ll.intersects(i.line);
                    if (dummy1 != null && dummy2 != null){
                        bubble.intersections.remove(z--);
                    }
                    else {
                        if (intersection != null) {
                            if (dummy2 != null){
                                in.line = new Line(in.line.p1, intersection);
                            } else {
                                in.line = new Line(in.line.p2, intersection);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public List<Bubble> getBubbles() {
            return bubbles;
    }
    
}
