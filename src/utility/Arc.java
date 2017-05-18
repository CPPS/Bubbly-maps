package utility;
import utility.Point;

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
    public  Point intersects(Edge e) {
        Arc a = (Arc) e;
        return null;
    }

    @Override
    public double getArea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
