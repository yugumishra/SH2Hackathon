package visual;

public class Vertex {
	private float x;
	private float y;
	private float z;
	private float norX;
	private float norY;
	private float norZ;
	private float texX;
	private float texY;
	private float texZ;
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	public float getNorX() {
		return norX;
	}
	public float getNorY() {
		return norY;
	}
	public float getNorZ() {
		return norZ;
	}
	public float getTexX() {
		return texX;
	}
	public float getTexY() {
		return texY;
	}
	public float getTexZ() {
		return texZ;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(norX);
		result = prime * result + Float.floatToIntBits(norY);
		result = prime * result + Float.floatToIntBits(norZ);
		result = prime * result + Float.floatToIntBits(texX);
		result = prime * result + Float.floatToIntBits(texY);
		result = prime * result + Float.floatToIntBits(texZ);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Vertex other = (Vertex) obj;
		if (Float.floatToIntBits(norX) != Float.floatToIntBits(other.norX))
			return false;
		if (Float.floatToIntBits(norY) != Float.floatToIntBits(other.norY))
			return false;
		if (Float.floatToIntBits(norZ) != Float.floatToIntBits(other.norZ))
			return false;
		if (Float.floatToIntBits(texX) != Float.floatToIntBits(other.texX))
			return false;
		if (Float.floatToIntBits(texY) != Float.floatToIntBits(other.texY))
			return false;
		if (Float.floatToIntBits(texZ) != Float.floatToIntBits(other.texZ))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
	public Vertex(float x, float y, float z, float norX, float norY, float norZ, float texX, float texY, float texZ) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.norX = norX;
		this.norY = norY;
		this.norZ = norZ;
		this.texX = texX;
		this.texY = texY;
		this.texZ = texZ;
	}
	
	
}
