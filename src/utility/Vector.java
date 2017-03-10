package utility;
import static java.lang.Math.*;
/**
 *
 * @author Pantea
 */
public class Vector{
    double x;
    double y;
    
    public Vector(double x, double y) {        
        this.x = x;
        this.y = y;
    }
    
    public double cross(Vector v){
        return this.x*v.y - this.y*v.x;
    }
    
    public double dot(Vector v){
        return this.x*v.x + this.y*v.y;
    }
    
    public double getLength(){
        return (sqrt(pow(x, 2) + pow(y, 2)));
    }
    
    public Vector plus (Vector v){
        return new Vector (this.x + v.x , this.y+ v.y);
    }
    
    public Vector minus (Vector v){
        return new Vector (this.x - v.x, this.y - v.y);
    }
}
