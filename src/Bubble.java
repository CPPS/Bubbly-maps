import utility.*;
/**
 *
 * @author Pantea
 */
public class Bubble {
    Point position;
    double radius;
    double area;
    Vector velocity;
    
    public Bubble(double x, double y, double radius, double area){
        this.position = new Point(x, y);
        this.radius = radius;
        this.area = area;
        this.velocity = new Vector(0,0);
    }
    
    public void setVelocity (double x, double y){
        this.velocity = new Vector(x, y);
    }
    
    public void moveBubble (Vector v){
        this.position = this.position.plus(v);
    }
    
    public void relocateBubble (Point p){
        this.position = p;
    }
}
