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
        		-0.5f, 0.5f, -1.5f,
        		0.5f, 0.5f, -2.5f,
        		-0.5f, -0.5f, -1.5f,
        		0.5f, -0.5f, -1.5f
        };
        
        int[] indices = {
        		0,1,3,
        		0,2,3
        };
        Mesh m = new Mesh("Rectangle", quadVertices, indices);
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