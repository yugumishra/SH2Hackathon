package visual;

import java.util.ArrayList;

public class World {
    private ArrayList<Mesh> meshes;
    public World() {
        meshes = new ArrayList<Mesh>();
    }

    public void addMesh(Mesh m) {
        meshes.add(m);
    }
    
    public void init() {
    	for(Mesh m: meshes) {
    		m.loadMesh();
    	}
    }

    public Mesh getMesh(int i) {
        return meshes.get(i);
    }

    public int getMeshCount() {
        return meshes.size();
    }
}
