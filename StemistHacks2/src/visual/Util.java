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
		} while (lineheader.compareTo("mtllib") != 0);
		String mtlName = scan.next();
		String[] filepaths = getTextures(mtlName);
		scan.next();
		float matID = 0;
		String name = scan.next();

		// initialize lists
		ArrayList<Float> positions = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Float> textureCoords = new ArrayList<Float>();
		ArrayList<Vertex> uniqueVertices = new ArrayList<Vertex>();
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if(line.contains("usemtl")) {
				String[] parts = line.split(" ");
				if(parts[1].equals("Material")) {
					matID = 0.0f;
					continue;
				}else {
					parts[1] = parts[1].substring(parts[1].length() -3);
					matID = Float.valueOf(parts[1]);
					continue;
				}
				
			}
			String[] components = line.split(" ");
			if (components[0].compareTo("v") == 0) {
				// positions
				positions.add(Float.valueOf(components[1]));
				positions.add(Float.valueOf(components[2]));
				positions.add(Float.valueOf(components[3]));
				continue;
			}
			if (components[0].compareTo("vn") == 0) {
				// normals
				normals.add(Float.valueOf(components[1]));
				normals.add(Float.valueOf(components[2]));
				normals.add(Float.valueOf(components[3]));
				continue;
			}
			if (components[0].compareTo("vt") == 0) {
				// texture coords
				textureCoords.add(Float.valueOf(components[1]));
				textureCoords.add(Float.valueOf(components[2]));
				continue;
			}
			if (components[0].compareTo("f") == 0) {
				// faces section
				String[] parts = line.substring(2).split(" ");
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
							matID);
					int index = uniqueVertices.indexOf(v);
					if(index == -1) {
						//not contained need to add
						indexes.add(uniqueVertices.size());
						uniqueVertices.add(v);
					}else {
						indexes.add(index);
					}
					continue;
				}
				
			}
			
		}
		
		float[] vertices = new float[uniqueVertices.size() * 9];
		for(int i = 0; i< uniqueVertices.size(); i++) {
			Vertex v = uniqueVertices.get(i);
			vertices[i*9] = v.getX();
			vertices[i*9 + 1] = v.getY();
			vertices[i*9 + 2] = v.getZ();
			
			vertices[i*9 + 3] = v.getNorX();
			vertices[i*9 + 4] = v.getNorY();
			vertices[i*9 + 5] = v.getNorZ();
			
			
			vertices[i*9 + 6] = v.getTexX();
			vertices[i*9 + 7] = v.getTexY();
			vertices[i*9 + 8] = v.getTexZ();
		}
		
		int[] indices = new int[indexes.size()];
		for(int i = 0; i< indices.length; i++) {
			indices[i] = indexes.get(i);
		}
		Mesh m = new Mesh(name, vertices, indices, filepaths);
		return m;
	}
	
	public static String[] getTextures(String mtlName) {
		// open scanner
		File f = new File("Assets\\models\\" + mtlName);
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("We couldn't find the .obj file");
		}
		
		String response = "";
		do {
			response = scan.next();
		}while(response.compareTo("File:") != 0);
		response = scan.next();
		if(response.compareTo("'None'") == 0) {
			//.obj has no mtl file and therefore no textures
			String[] path = {"Assets\\textures\\test_texture.png"};
			return path;
		}
		//flush the newline character
		scan.nextLine();
		ArrayList<String> paths = new ArrayList<String>();
		while(scan.hasNext()) {
			response = scan.next();
			if(response.compareTo("map_Kd") == 0) {
				//we have a file path incoming
				paths.add(scan.next());
			}
		}
		String[] pathss = new String[paths.size()];
		for(int i = 0; i< pathss.length;i++) {
			pathss[i] = paths.get(i);
		}
		return pathss;
	}
	
	public static AnimatedMesh getAnimation(String name, String filename, int animationSize) {
		AnimatedMesh am = new AnimatedMesh(name);
		for(int i = 1; i<= animationSize; i++) { 
			String path = filename.substring(0, filename.length()-4) + String.valueOf(i) + ".obj";
			Mesh mesh = readObjFile(path);
			am.addMesh(mesh);
		}
		return am;
	}
}
