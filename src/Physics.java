import java.util.List;
import utility.Vector;

public class Physics {
	public static void singlePass(Map map) {
        Graph environment = map.getEnvironment();
		List<Bubble> bubbles = environment.getBubbles();
//		List<Edge> edges = environment.getEdges();

		for (Bubble bubble : bubbles) {
			bubble.setVelocity(0.0, 0.0);
		}

//		for (Edge edge : edges) {
//			double distance = edge.getDistance();
//			double length = edge.getLength();
//			double delta = length - distance;
//
//			for (int i = 0; i < 2; i++) {
//				Bubble bubble = edge.get(i);
//				Vector difference = edge.other(bubble).getPosition().makeVector(bubble.getPosition());
//				bubble.setVelocity(bubble.getVelocity().plus(difference.scale(0.001 * delta)));
//			}
//		}
 
		for (Bubble bubble : bubbles) {
			bubble.moveBubble(bubble.getVelocity());
			bubble.setVelocity(0.0, 0.0);
		}
	}
}
