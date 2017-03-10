/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author Pantea
 */
public class Point {
    double x;
    double y;
    
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public Point plus (Vector v){
        return new Point (this.x + v.x , this.y+ v.y);
    }
    
    public Point minus (Vector v){
        return new Point (this.x - v.x, this.y - v.y);
    }
    
    public Vector makeVector (Point p){
        return new Vector (p.x - this.x, p.y - this.y);
    }
}
