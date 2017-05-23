/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Line extends Edge{
    Vector direction;
    private final double EPS = 0.000001;
    
    
    public Line(Point point1, Point point2){
        super(point1, point2);
        direction = new Vector (p2.getX()-p1.getX(), p2.getY()-p1.getY());
    }
    
    public double getLength(){
        return (sqrt(pow(p1.getX() - p2.getX(), 2) + pow(p1.getY() - p2.getY(), 2)));
    }

    @Override
    public Point intersects(Edge e) {
        if (e instanceof Line) {
            Line l = (Line)e;

            Vector  v1 = p1.vectorTo(p2),
                    v2 = l.p1.vectorTo(l.p2);

            Vector  c1 = p1.vectorTo(l.p1),
                    c2 = l.p1.vectorTo(p1);

            double  t1 = c1.perpDot(v2) / v1.perpDot(v2),
                    t2 = c2.perpDot(v1) / v2.perpDot(v1);

            boolean b1 = -EPS < t1 && t1 < 1 + EPS,
                    b2 = -EPS < t2 && t2 < 1 + EPS;

            if (b1 && b2) {
                Point r1 = p1.plus(v1.scale(t1)),
                        r2 = l.p1.plus(v2.scale(t2));

                if (r1.vectorTo(r2).getLength() <= EPS) {
                    return r1;
                } else {
                    System.out.println(r1.vectorTo(r2).getLength());
                    throw new UnsupportedOperationException("Something smells fucky");
                }
            } else return null;
        } else throw new UnsupportedOperationException("cannot intersect Line and Arc");
    }

    @Override
    public double getArea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
