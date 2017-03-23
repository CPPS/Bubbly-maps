import java.util.ArrayList;

public class Map {
    private Graph environment;

	public Map(Graph environment) {
		this.environment = environment;
	}

	public Graph getEnvironment() {
		return environment;
	}
}
