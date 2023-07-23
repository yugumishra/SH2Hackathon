package visual;

import java.util.ArrayList;
import java.util.List;

public class Platform {
	private List<Mesh> platforms;
	
	public Platform() {
		platforms = new ArrayList<Mesh>();
	}
	
	public void addPlatform(Mesh m) {
		platforms.add(m);
	}
	
	public float lowestHeight() {
		float lowest = 100000000.0f;
		for(Mesh m: platforms) {
			if(m.getPosition().y < lowest) {
				lowest = m.getPosition().y;
			}
		}
		return lowest;
	}
}
