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
        for (Bubble b: bubbles) fixIntersections(b);
    }
    
    public List<Intersection> bubbleIntersections(int b){
        List<Intersection> result = new ArrayList<>();
        Bubble b1,b2;
        b1= bubbles.get(b);
        for (int i=0; i < bubbles.size(); i++){
            if (i == b) continue;
            b2 = bubbles.get(i);
            if (b1.position.distanceTo(b2.position)-(b1.radius + b2.radius) > EPS){
                Pair<Point, Point> p = circleIntersections(b1, b2);
                b1.intersections.add(new Intersection (b1, b2, new Line(p.first, p.second)));
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
        a /= 2d;
        double h = sqrt(pow(r1,2) - pow(a,2));
        Vector ab = new Vector (c2.getX() - c1.getX(), c2.getY() - c1.getY());
        ab.normalize();
        Vector v = new Vector(-ab.getY(), ab.getX());
        v = v.scale(h);
        ab = ab.scale(a);
        
        p1 = c1.plus(ab);
        p1 = p1.plus(v);
        
        p2 = c1.plus(ab);
        p2 = p2.minus(v);
        return new Pair(p1, p2);
    }
    
    public void fixIntersections(Bubble b1){
        Pair<Boolean, Point> pair;
        for (Intersection i: b1.intersections){
            for (Intersection in: b1.intersections){
                if (i.equals(in)) continue;
                pair = i.line.intersects(in.line);
                if (pair.first){
                    Bubble b;
                    Point p;
                    //fix i
                    b = i.b1.equals(in.b1) ? in.b2 : in.b1;
                    Point center = b.getPosition();
                    double dist1 = i.line.p1.distanceTo(center);
                    double dist2 = i.line.p2.distanceTo(center);
                    p = (dist1 - dist2) > EPS ? i.line.p1 : i.line.p2; 
                    i.line = new Line (p,pair.second);
                    //fix in
                    b = in.b1.equals(i.b1) ? i.b2 : i.b1;
                    center = b.getPosition();
                    dist1 = in.line.p1.distanceTo(center);
                    dist2 = in.line.p2.distanceTo(center);
                    p = (dist1 - dist2) > EPS ? in.line.p1 : in.line.p2; 
                    in.line = new Line (p,pair.second);
                } 
            }
        }
    }
    
    public List<Bubble> getBubbles() {
            return bubbles;
    }
    
}
