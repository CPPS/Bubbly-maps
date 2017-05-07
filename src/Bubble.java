import java.util.ArrayList;
import java.util.List;
import utility.*;

public class Bubble {
    Point position;
    double radius;
    double area;
    Vector velocity;
    ArrayList<Intersection> intersections;
    ArrayList<ArrayList<Edge>> polygons = new ArrayList<ArrayList<Edge>>();
    Util util = new Util();

    public Bubble(double x, double y, double radius, double area){
        this.position = new Point(x, y);
        this.radius = radius;
        this.area = area;
        this.velocity = new Vector(0,0);
        this.intersections = new ArrayList<>();
    }

	public Point getPosition (){
		return this.position;
	}

	public Vector getVelocity (){
		return this.velocity;
	}

    public void setVelocity (double x, double y){
        this.velocity = new Vector(x, y);
    }

	public void setVelocity (Vector vel){
		this.velocity = vel;
	}

    public void moveBubble (Vector v){
        this.position = this.position.plus(v);
    }

    public void relocateBubble (Point p){
        this.position = p;
    }
    @Override
    public int hashCode(){
        return (int)(this.position.getX()*7 + this.position.getY()*19);
    }
    
    @Override
    public boolean equals (Object o){
        Bubble b = (Bubble) o;
        return this.position.equals(b.position);
    }
    
    double findLostArea (List<Edge> edges){
        Point first, last;
        if (edges.size() == 1){
            first = edges.get(0).p1;
            last = edges.get(0).p2;
        } else {
            int size = edges.size();
            if (edges.get(0).p1.equals(edges.get(1).p1) || edges.get(0).p1.equals(edges.get(1).p2)){
                first = edges.get(0).p2;
            } else {
                first = edges.get(0).p1;
            }
            if (edges.get(size - 1).p1.equals(edges.get(size - 2).p1) || edges.get(size - 1).p1.equals(edges.get(size - 2).p2)){
                last = edges.get(size).p2;
            } else {
                last = edges.get(0).p1;
            }
        }
        Vector v1 = new Vector(this.position, edges.get(0).p1);
        Vector v2 = new Vector(this.position, edges.get(0).p2);
        double dot = v1.dot(v2);
        dot /= v1.getLength();
        dot /= v2.getLength();
        return util.calcSegmentArea(Math.acos(dot), this.radius) - util.calcPolygonArea(edges);
    }
    
    double buubbleArea(){
        double area = Math.PI* Math.pow(radius, 2);
        for (ArrayList p: polygons){
            area -= findLostArea(p);
            for (Object o: p){
                Edge e = (Edge)o;
                area += e.getArea();
            }
        }
        return area;
    }
}
