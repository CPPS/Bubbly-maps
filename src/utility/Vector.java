package utility;
import static java.lang.Math.*;
/**
 *
 * @author Pantea
 */
public class Vector{
    private double x;
    private double y;
    private final double EPS = 0.000001;
    
    public Vector(double x, double y) {        

        this.x = x;
        this.y = y;
    }
    
    public Vector(Point p1, Point p2) {
        this.x = p2.getX() - p1.getX();
        this.y  = p2.getY() - p1.getY();
    }

    public double cross(Vector v){
        return this.x*v.y - this.y*v.x;
    }

    public double dot(Vector v){
        return this.x*v.x + this.y*v.y;
    }

    public double perpDot(Vector v) {
        return x*v.y - y*v.x;
    }

    public double getLength(){
        return (sqrt(pow(x, 2) + pow(y, 2)));
    }

    public Vector plus(Vector v){
        return new Vector (this.x + v.x , this.y+ v.y);
    }

    public Vector minus(Vector v){
        return new Vector (this.x - v.x, this.y - v.y);
    }
    
    public Vector normalize() {
        return this.scale(1.0/this.getLength());
    }

    public double angleBetween(Vector v) {
        Vector  v1 = normalize(),
                v2 = v.normalize();
        return Math.acos(v1.dot(v2));
    }

    public double angleTo(Vector v) {
        double angle = Math.atan2(v.y, v.x) - Math.atan2(y, x);
        if (angle < Math.PI) angle += 2 * Math.PI;
        if (angle > Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    public boolean sameSide(Point base, Point p1, Point p2) {
        double  a1 = angleTo(base.vectorTo(p1)),
                a2 = angleTo(base.vectorTo(p2));
        return Math.signum(a1) == Math.signum(a2);
    }

    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    @Override
    public boolean equals(Object o) {
        Vector v = (Vector)o;
        return (Math.abs(v.getX()-this.x) <= EPS) && (Math.abs(v.getY()-this.y)<= EPS);
    }

    public Vector scale(double x){
            return scale(x, x);
    }

    public Vector scale(double x, double y){
            return new Vector (this.x * x, this.y * y);
    }
}
