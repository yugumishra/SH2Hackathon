package player;

import visual.Mesh;

public class Hitbox {
	private Mesh object;
	private float minX;
	private float minY;
	private float minZ;
	private float maxX;
	private float maxY;
	private float maxZ;
	
	private float initMinX;
	private float initMinY;
	private float initMinZ;
	private float initMaxX;
	private float initMaxY;
	private float initMaxZ;
	
	public Hitbox(Mesh m) {
		object = m;
	}
	
	public void create() {
		float[] vertices = object.getVertices();
		initMinX = initMinY = initMinZ = Float.MAX_VALUE;
		initMaxX = initMaxY = initMaxZ = -Float.MAX_VALUE;
		for(int i =0; i< vertices.length; i+=9) {
			if(vertices[i] > initMaxX) {
				initMaxX = vertices[i];
			}
			if(vertices[i] < initMinX) {
				initMinX = vertices[i];
			}
			if(vertices[i+1] > initMaxY) {
				initMaxY = vertices[i+1];
			}
			if(vertices[i+1] < initMinY) {
				initMinY = vertices[i+1];
			}
			if(vertices[i+2] > initMaxZ) {
				initMaxZ = vertices[i+2];
			}
			if(vertices[i+2] < initMinZ) {
				initMinZ = vertices[i+2];
			}
		}
	}
}
