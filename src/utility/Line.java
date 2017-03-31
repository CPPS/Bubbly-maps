/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author Pantea
 */
public class Line {
    public Point p1;
    public Point p2;
    Vector direction;
    private final double EPS = 0.000001;
    
    public Line (Point point1, Point point2){
        this.p1 = point1;
        this.p2 = point2;
        direction = new Vector (p2.getX()-p1.getX(), p2.getY()-p1.getY());
    }
    
    public double getLength(){
        return (sqrt(pow(p1.getX() - p2.getX(), 2) + pow(p1.getY() - p2.getY(), 2)));
    }
    
    public Pair<Boolean, Point> intersects(Line l){
        double m1 = direction.getY()/direction.getX();
        double m2 = l.direction.getY()/l.direction.getX();
        if (Math.abs(m1- m2) <= EPS)    return new Pair(false,null);
        if (p1.equals(l.p1) || p1.equals(l.p2) || 
            p2.equals(l.p1)|| p2.equals(l.p2)) return new Pair(false, null);
            
        double b1 = p1.getY() - (m1*p1.getX());
        double b2 = l.p1.getY() - (m2*l.p1.getX());
        double x = (b1-b2)/(m2-m1);
        double y = m1*x+b1;
        Point intsec = new Point (x,y);
        double dist1 = intsec.distanceTo(p1);
        double dist2 = intsec.distanceTo(p2);
        double dist3 = intsec.distanceTo(l.p1);
        double dist4 = intsec.distanceTo(l.p2);
        if ((dist1+dist2) - this.getLength() > EPS ||
            (dist3+dist4) - l.getLength() > EPS){
            return new Pair(false, null);
        }
        return new Pair(true, new Point(x,y));
    }
}
