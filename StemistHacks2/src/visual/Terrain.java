package visual;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class Terrain {

	// interpolation differences
	private Vector3f greyGreenDiff;
	private Vector3f snowGreyDiff;

	public Terrain() {
		greyGreenDiff = (new Vector3f(0.5f, 0.5f, 0.5f)).sub(0, 154.0f / 255.0f, 23.0f / 255.0f);
		snowGreyDiff = (new Vector3f(243.0f / 255.0f, 246.0f / 255.0f, 251.0f / 255.0f)).sub(0.5f, 0.5f, 0.5f);
	}

	// for terrain we will not be mapping textures to it
	// they will be solid colors
	// so the texture coordinates will be replaced by rgb values in the shader
	public Mesh genTerrain() {
		List<Float> positions = new ArrayList<Float>();
		List<Float> colors = new ArrayList<Float>();
		List<Integer> indices = new ArrayList<Integer>();
		PerlinNoise perlin = new PerlinNoise();
		for(float x = -159; x< 160; x+=1) {
			for(float z = -159; z < 160; z += 1) {
				float normalizedX = (float) x / 320.0f;
				float normalizedY = (float) z / 320.0f;
				float y = perlin.perlinNoise(normalizedX, normalizedY);
				y*= 7.5f;
				y+= 50f;
				if((x < -78*2 || x > 78*2 || z > 78*2 || z < -78*2)) y = -50.0f;
				positions.add(x);
				positions.add(y);
				positions.add(z);
				
				Vector3f color = getColor(y);
				colors.add(color.x);
				colors.add(color.y);
				colors.add(color.z);
			}
		}
		for(int i = 0; i< 159*2+3; i++) {
			for(int j = 0; j< 159*2+3; j++) {
				int index = i * 160*2 + j;
				indices.add(index);
				indices.add(index+1);
				indices.add(index+160*2);
				indices.add(index+1);
				indices.add(index + 160*2);
				indices.add(index + 160 *2 + 1);
			}
		}
		float[] vertices = new float[positions.size() * 3];
		for(int i =0; i< vertices.length/9; i++) {
			vertices[i*9 + 0] = positions.get(i*3);
			vertices[i*9 + 1] = positions.get(i*3+1);
			vertices[i*9 + 2] = positions.get(i*3+2);
			
			vertices[i*9 + 3] = 0.0f;
			vertices[i*9 + 4] = 0.0f;
			vertices[i*9 + 5] = 0.0f;
			
			vertices[i*9 + 6] = colors.get(i*3);
			vertices[i*9 + 7] = colors.get(i*3+1);
			vertices[i*9 + 8] = colors.get(i*3+2);
		}
		int[] indexes = new int[indices.size()];
		for(int i = 0; i< indices.size(); i++) {
			indexes[i] = indices.get(i);
		}
		Mesh terrain = new Mesh("Terrain", vertices, indexes, null);
		return terrain;
	}

	public Vector3f getColor(float height) {
		// interpolate from height of 0 to height of 15
		// 0 height means completely green
		// 12 height means rockish grey
		// 15 height means snowish

		if (height < 7.5f-35.0f) {
			// grass to grey interpolation
			height /= 27.5f;
			return new Vector3f(greyGreenDiff.x * height, greyGreenDiff.y * height, greyGreenDiff.z * height);
		} else if (height >= 7.5f-35.0f) {
			height /= 27.5f;
			return new Vector3f(snowGreyDiff.z * height, snowGreyDiff.y * height, snowGreyDiff.z * height);
		}else {
			return new Vector3f(0.4f, 0.4f, 0.4f);
		}
	}
}
