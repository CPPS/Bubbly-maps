package utility;

import java.util.ArrayList;
import java.util.List;
import utility.Line;

public class Util {

    public double calcSegmentArea(double angle, double radius) {
        double a = Math.pow(angle, 2) * (angle - Math.sin(angle));
        return a / 2.0;
    }

    public double calcPolygonArea(List<Edge> sides) {
        if (sides.size() < 2) {
            return 0;
        }
        ArrayList<Point> points = new ArrayList<>();
        points.add(sides.get(0).p1);
        points.add(sides.get(0).p2);
        Point first, second;
        Point p2 = sides.get(1).p1;
        Point p3 = sides.get(1).p2;
        if (points.get(0).equals(p2) || points.get(0).equals(p3)) {
            first = points.get(1);
            second = points.get(0);
        } else {
            first = points.get(0);
            second = points.get(1);
        }

        double sum = 0;
        for (int i = 1; i < sides.size(); i++) {
            sum += (first.getX() + second.getX()) * (second.getY() - first.getX());
            if (i < sides.size() - 1) {
                first = second;
                second = sides.get(i).p1.equals(second) ? sides.get(i).p2 : sides.get(i).p1;
            }
        }
        return Math.abs(sum)/2.0;
    }
}
