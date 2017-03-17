import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Graph {
	private Map<Bubble, Integer> map = new Map<>();
	private List<Bubble> bubbles = new ArrayList<>();
	private List<Edge> edges = new ArrayList<>();

	public Graph(double[][] distances) {
		int width = distances.length;
		int height = distances[0].length;

		if (width != height)
			throw new Error("not a square table");

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (distances[i][j] != distances[j][i])
					throw new Error("distances don't match");

		for (int i = 0; i < width; i++) {
			Bubble bubble = new Bubble(Math.random(), Math.random(), 0.0, 0.0);

			bubbles.add(bubble);
			map.put(bubble, i);

			int offset = 0;
			for (int j = i; j >= 0; j--) {
				offset += j;
				edges.add(offset, new Edge(bubbles.get(j), bubble, distances[i][j]));
			}
		}
	}

	public List<Bubble> getBubbles() {
		return bubbles;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Edge> getEdges(Bubble bubble) {
		return edges.stream()
				.filter((Edge e) -> e.connects(bubble))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public Edge getEdge(Bubble bubble1, Bubble bubble2) {
		return edges.stream()
				.filter((Edge e) -> e.connects(bubble1, bubble2))
				.findFirst().orElse(null);
	}

	public List<Edge> getOverlapping() {
		return edges.stream()
				.filter((Edge e) -> e.get(0).radius + e.get(1).radius >= e.getLength())
				.collect(Collectors.toCollection(ArrayList::new));
	}
}
