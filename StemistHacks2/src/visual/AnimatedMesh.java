package visual;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AnimatedMesh implements Drawable{
	private ArrayList<Mesh> meshes;
	private boolean start;
	private boolean loaded;
	private int resting = 30;
	private int current = 30;
	private String name;
	
	public AnimatedMesh(String name) {
		this.name = name;
		meshes = new ArrayList<Mesh>();
		loaded = false;
	}
	
	public void addMesh(Mesh m) {
		meshes.add(m);
	}
	
	public int getVao() {
		return meshes.get(current).getVao();
	}
	
	public int getIbo() {
		return meshes.get(current).getIbo();
	}
	
	public int getVertexCount() {
		return meshes.get(current).getVertexCount();
	}
	
	public int getTexID() {
		return meshes.get(current).getTexID();
	}
	
	public Matrix4f getModelMat() {
		return meshes.get(current).getModelMat();
	}
	
	public void increment() {
		if(start) current++;
		if(current >= meshes.size()) {
			current = 0;
		}
		if(current == resting) {
			stop();
		}
	}
	
	public void start() {
		start =true;
	}
	
	public void stop() {
		start = false;
	}
	
	public void init() {
		for(int i = 0;i < meshes.size(); i++) {
			meshes.get(i).init();
		}
		loaded = true;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public void cleanup() {
		for(Mesh m: meshes) {
			m.cleanup();
		}
	}
	
	public void setPos(Vector3f po) {
		for(Mesh m: meshes) {
			m.setPos(po);
		}
	}
	
	public void addRot(Vector3f rot) {
		for(Mesh m: meshes) {
			m.addRot(rot);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setRotation(Vector3f other) {
		for(Mesh m:meshes) {
			m.setRot(other);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + current;
		result = prime * result + (loaded ? 1231 : 1237);
		result = prime * result + ((meshes == null) ? 0 : meshes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + resting;
		result = prime * result + (start ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnimatedMesh other = (AnimatedMesh) obj;
		if (current != other.current)
			return false;
		if (loaded != other.loaded)
			return false;
		if (meshes == null) {
			if (other.meshes != null)
				return false;
		} else if (!meshes.equals(other.meshes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (resting != other.resting)
			return false;
		if (start != other.start)
			return false;
		return true;
	}
	
	
}
