package visual;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AnimatedMesh implements Drawable{
	private ArrayList<Mesh> meshes;
	private boolean start;
	private boolean loaded;
	private int resting = 0;
	private int current = 0;
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
		if(current == meshes.size() ) {
			current = resting;
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
}
