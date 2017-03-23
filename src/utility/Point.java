/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import static java.lang.Math.*;

/**
 *
 * @author Pantea
 */
public class Point {
    private double x;
    private double y;
    private final double EPS = 0.000001;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public Point plus (Vector v){
        return new Point (this.x + v.getX() , this.y+ v.getY());
    }
    
    public Point minus (Vector v){
        return new Point (this.x - v.getX(), this.y - v.getY());
    }
    
    public Vector makeVector (Point p){
        return new Vector (p.x - this.x, p.y - this.y);
    }
    
    public Vector vectorTo(Point p){
        return new Vector(p.x - this.x, p.y - this.y);
    }
    
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    public double distanceTo (Point p){
        return (sqrt(pow(x - p.getX(), 2) + pow(y - p.y, 2)));
    }
    @Override
    public boolean equals(Object o){
        Point p = (Point)o;
        return (Math.abs(p.getX()-this.x) <= EPS) && (Math.abs(p.getY()-this.y)<= EPS);
    }
}
