package visual;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface Drawable {
	public int getVao();
	public int getVertexCount();
	public int getIbo();
	public int getTexID();
	
	public Matrix4f getModelMat();
	
	public void init();
	public boolean isLoaded();
	public void cleanup();
	public void increment();
	
	public void addRot(Vector3f rot);
	public String getName();
}
