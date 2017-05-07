package utility;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import utility.Point;

public abstract class Edge {
    public Point p1,p2;
    
    public Edge(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public abstract Pair<Boolean, Point> intersects(Edge e);
    public abstract double getArea();
}
