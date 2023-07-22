package visual;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.joml.Matrix4f;

public class Util {
    public static String scanShader(String filename) {
        File f = new File(filename);
        Scanner scan = null;
        try {
           scan = new Scanner(f); 
        }catch (FileNotFoundException e) {
            System.err.println("The shader file " + filename + " was not found");
        }

        StringBuilder sb = new StringBuilder();
        while(scan.hasNextLine()) {
            sb.append(scan.nextLine());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static Matrix4f getProjectionMatrix(float aspectRatio, float fov) {
    	Matrix4f projectionMatrix = new Matrix4f();
    	float near = 0.01f;
    	float far = 1000.0f;
    	float top = (float) Math.tan(fov/2) * near;
    	float bottom = -top;
    	float right = top * aspectRatio;
    	float left = -right;
    	projectionMatrix.frustum(left, right, bottom, top, near, far);
    	return projectionMatrix;
    }
    
    
}
