import java.util.ArrayList;
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
    //ArrayList<Pair<Bubble, Line, >> intersections;
    

    public Bubble(double x, double y, double radius, double area){
        this.position = new Point(x, y);
        this.radius = radius;
        this.area = area;
        this.velocity = new Vector(0,0);
        //this.intersections = new ArrayList<>();
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
}
