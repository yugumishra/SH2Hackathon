package visual;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Vector3f position;
	private Vector3f velocity;
	private Vector3f rotation;
	
	//initialize everything to 0 (almost)
	public Camera() {
		position = new Vector3f(0,0,0);
		velocity = new Vector3f(0,0,0);
		rotation = new Vector3f(0,0,0);
	}
	
	public void addVelocity(Vector3f v) {
			velocity.add(v);
	}
	
	public void update() {
		position.add(velocity);
		velocity.mul(0.9f);
	}
	
	public Matrix4f getViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
}
