package core;

import java.util.ArrayList;
import java.util.List;
import utility.*;

public class Bubble {
    Point position;
    double radius;
    double area;
    Vector velocity;
    List<Intersection> intersections;
    List<List<Edge>> polygons;

    public Bubble(double x, double y, double radius, double area) {
        this.position = new Point(x, y);
        this.radius = radius;
        this.area = area;
        this.velocity = new Vector(0,0);
        this.intersections = new ArrayList<>();
        this.polygons = new ArrayList<>();
    }

	public Point getPosition(){
		return this.position;
	}

	public double getRadius() {
	    return this.radius;
    }

	public Vector getVelocity(){
		return this.velocity;
	}

	public List<Intersection> getIntersections() {
	    return this.intersections;
    }

    public void setVelocity(double x, double y){
        this.velocity = new Vector(x, y);
    }

	public void setVelocity(Vector vel){
		this.velocity = vel;
	}

    public void moveBubble(Vector v){
        this.position = this.position.plus(v);
    }

    public void relocateBubble(Point p){
        this.position = p;
    }
    @Override
    public int hashCode(){
        return (int)(this.position.getX() * 7 + this.position.getY() * 19);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Bubble) {
            Bubble b = (Bubble) o;
            return this == b;
        }
        return false;
    }

    public double bubbleArea() {
        double area = Math.PI * Math.pow(radius, 2);

        for (int i = 0; i < polygons.size(); i++) {
            List<Edge> polygon = polygons.get(i);

            // calculating polygon area
            double cum = 0;
            Edge prev = polygon.get(0);
            Point first = null, second = null, last = null;
            for (int j = 0; j < polygon.size(); j++) {
                Edge next = polygon.get(j + 1 % polygon.size());
                Point   pp1 = prev.p1,
                        pp2 = prev.p2,
                        np1 = next.p1;

                // finding common point
                Point common;                       // fixing missing data
                if (pp1 == np1) {common = pp1;      if (j == 0) {last = pp2; first = pp2; second = pp1;}}
                else            {common = pp2;      if (j == 0) {last = pp1; first = pp1; second = pp2;}}

                cum += last.determinant(common);
                last = common;
            }

            // check whether the polygon is whole
            if (last != first) {
                // finish the polygon if not
                cum += last.determinant(first);

                // calculating segment area
                double angle = position.vectorTo(first).angleBetween(position.vectorTo(last)),
                        rr = Math.pow(radius, 2),
                        areaCircle = Math.PI * rr,
                        areaSegment = 0.5 * rr * (angle - Math.sin(angle));

                // see what side of the circle should be discarded;
                boolean angleGreaterThan180Deg =
                        first.vectorTo(last).sameSide(first, second, position);

                // discard the correct bit
                area -= angleGreaterThan180Deg
                        ? areaCircle - areaSegment
                        : areaSegment;

                // then add the area of the polygon
                area += 0.5 * Math.abs(cum);
            } else {
                // if the polygon is whole, it must be the entire bubble area
                area = 0.5 * Math.abs(cum);
                break;
            }
        }

        return area;
    }
}
