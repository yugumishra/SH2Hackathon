package visual;

import java.awt.Toolkit;
import java.awt.Dimension;

public class Startup {
    private static Window window;
    private static World world;
    private static Renderer renderer;
    public static void main(String[] args) {
        int width = 0;
        int height = 0;

        //use toolkit to get the primary monitor width and height
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();

        //create window instance
        window = new Window("SH2", width, height, true);

        //create world and add meshes for testing
        world = new World();
        float[] quadVertices = {
        			    // first triangle
        			     0.5f,  0.5f, 0.0f,  // top right
        			     0.5f, -0.5f, 0.0f,  // bottom right
        			    -0.5f,  0.5f, 0.0f,  // top left 
        			    // second triangle
        			     0.5f, -0.5f, 0.0f,  // bottom right
        			    -0.5f, -0.5f, 0.0f,  // bottom left
        			    -0.5f,  0.5f, 0.0f   // top left
        			
        };
        Mesh m = new Mesh("Rectangle", quadVertices);
        world.addMesh(m);

        //create Renderer instance
        renderer = new Renderer();

        //create loop
        Loop loop = new Loop();

        //start the game loop
        loop.start();
    }

    public static Window getWindow() {
        return window;
    }

    public static World getWorld() {
        return world;
    }

    public static Renderer getRenderer() {
        return renderer;
    }
}