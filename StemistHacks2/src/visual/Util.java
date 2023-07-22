package visual;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.joml.Matrix4f;

public class Util {
	public static String scanShader(String filename) {
		File f = new File(filename);
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("The shader file " + filename + " was not found");
		}

		StringBuilder sb = new StringBuilder();
		while (scan.hasNextLine()) {
			sb.append(scan.nextLine());
			sb.append("\n");
		}
		return sb.toString();
	}

	public static Matrix4f getProjectionMatrix(float aspectRatio, float fov) {
		Matrix4f projectionMatrix = new Matrix4f();
		float near = 0.01f;
		float far = 1000.0f;
		float top = (float) Math.tan(fov / 2) * near;
		float bottom = -top;
		float right = top * aspectRatio;
		float left = -right;
		projectionMatrix.frustum(left, right, bottom, top, near, far);
		return projectionMatrix;
	}

	public static Mesh readObjFile(String filename) {
		// open scanner
		File f = new File(filename);
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("We couldn't find the .obj file");
		}
		// scan until name is found
		String lineheader = "";
		do {
			lineheader = scan.next();
		} while (lineheader.compareTo("o") != 0);
		String name = scan.next();

		// initialize lists
		ArrayList<Float> positions = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Float> textureCoords = new ArrayList<Float>();
		ArrayList<Vertex> uniqueVertices = new ArrayList<Vertex>();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		while (scan.hasNext()) {
			lineheader = scan.next();
			if (lineheader.compareTo("v") == 0) {
				// positions
				positions.add(scan.nextFloat());
				positions.add(scan.nextFloat());
				positions.add(scan.nextFloat());
			}
			if (lineheader.compareTo("vn") == 0) {
				// normals
				normals.add(scan.nextFloat());
				normals.add(scan.nextFloat());
				normals.add(scan.nextFloat());
			}
			if (lineheader.compareTo("vt") == 0) {
				// texture coords
				textureCoords.add(scan.nextFloat());
				textureCoords.add(scan.nextFloat());
			}
			if (lineheader.compareTo("f") == 0) {
				// faces section
				String line = scan.nextLine();
				String[] parts = line.substring(1).split(" ");
				for (String vertex : parts) {
					String[] inds = vertex.split("/");
					int pind = Integer.valueOf(inds[0]);
					int nind = Integer.valueOf(inds[2]);
					int tind = Integer.valueOf(inds[1]);
					pind--;
					tind--;
					nind--;
					
					pind *= 3;
					nind *= 3;
					tind *= 2;
					
					Vertex v = new Vertex(positions.get(pind), positions.get(pind+ 1),
							positions.get(pind+ 2), normals.get(nind), normals.get(nind + 1),
							normals.get(nind + 2), textureCoords.get(tind), textureCoords.get(tind + 1),
							1.0f);
					int index = uniqueVertices.indexOf(v);
					if(index == -1) {
						//not contained need to add
						indexes.add(uniqueVertices.size());
						uniqueVertices.add(v);
					}else {
						indexes.add(index);
					}
				}
			}
		}
		
		float[] vertices = new float[uniqueVertices.size() * 8];
		for(int i = 0; i< uniqueVertices.size(); i++) {
			Vertex v = uniqueVertices.get(i);
			vertices[i*8] = v.getX();
			vertices[i*8 + 1] = v.getY();
			vertices[i*8 + 2] = v.getZ();
			
			vertices[i*8 + 3] = v.getNorX();
			vertices[i*8 + 4] = v.getNorY();
			vertices[i*8 + 5] = v.getNorZ();
			
			vertices[i*8 + 6] = v.getTexX();
			vertices[i*8 + 7] = v.getTexY();
		}
		
		int[] indices = new int[indexes.size()];
		for(int i = 0; i< indices.length; i++) {
			indices[i] = indexes.get(i);
		}
		Mesh m = new Mesh(name, vertices, indices);
		return m;
	}
}
