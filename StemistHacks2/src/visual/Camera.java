package visual;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f rotation;
	
	private int width;
	private int height;
	
	private float mouseSensitivity;
	
	//initialize everything to 0 (almost)
	public Camera(int width, int height) {
		position = new Vector3f(0,0,0);
		velocity = new Vector3f(0,0,0);
		rotation = new Vector3f(0,0,0);
		
		mouseSensitivity = 0.8f;
		this.width = width;
		this.height = height;
	}
	
	public void addVelocity(Vector3f v) {
			velocity.add(v);
	}
	
	public void update() {
		position.add(velocity);
		velocity.mul(0.9f);
	}
	
	public void updateDimensions(int w, int h) {
		width =w;
		height = h;
	}
	
	public void processMouseMoved(int x, int y) {
		int dx = x - width/2;
		int dy = y - height/2;

		float scale = (float) Math.toRadians(60)/width;
		Vector3f change = new Vector3f(dy * scale * mouseSensitivity, dx * scale * mouseSensitivity, 0.0f);

		rotation.add(change);
		rotation.x %= Math.PI * 2;
		rotation.y %= Math.PI * 2;
		rotation.z %= Math.PI * 2;
	}
	
	public Matrix4f getViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix
		.rotate(rotation.x, new Vector3f(1,0,0))
		.rotate(rotation.y, new Vector3f(0,1,0))
		.rotate(rotation.z, new Vector3f(0,0,1))
		.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
}
