import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Edge {
    private final Bubble bubbles[] = new Bubble[2];
	private double distance;

	public Edge(Bubble bubble1, Bubble bubble2, double dist) {
		bubbles[0] = bubble1;
		bubbles[1] = bubble2;
		distance = dist;
	}

	public Bubble get(int i) {
		if (i >= 0 && i <= 1) {
			return bubbles[i];
		}
		return null;
	}

	public Bubble other(Bubble bubble) {
		if (connects(bubble)) {
			if (bubbles[0] == bubble) {
				return bubbles[1];
			} else {
				return bubbles[0];
			}
		}
		return null;
	}

	public double getDistance() {
		return distance;
	}

	public double getLength() {
		return bubbles[0].position.makeVector(bubbles[1].position).getLength();
	}

	public boolean connects(Bubble bubble) {
		return bubbles[0] == bubble || bubbles[1] == bubble;
	}

	public boolean connects(Bubble bubble1, Bubble bubble2) {
		return connects(bubble1) && connects(bubble2);
	}
}
