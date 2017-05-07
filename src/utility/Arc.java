package utility;


import utility.Point;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pantea
 */
public class Arc extends Edge{
    Point p1, p2, c;
    double angle, radius;
    
    public Arc (Point p1, Point p2, double a, Point s){
        super(p1, p2);
        this.angle = a;
    }
    
    public Arc (Point p1, Point p2, Point c){
        super(p1, p2);
        this.c = c;
    }
    
    private Point findCenter(){
        Point c;
        return null;
    }


    @Override
    public Pair<Boolean, Point> intersects(Edge e) {
        Arc a = (Arc) e;
        return new Pair (false, null);
    }

    @Override
    public double getArea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
