package visual;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.joml.Vector3f;

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
        
        Mesh suzanne = Util.readObjFile("Assets\\models\\untitled.obj");
        suzanne.setPos(new Vector3f(0, 1, 0));
        world.addMesh(suzanne);
        Mesh floor = Util.readObjFile("Assets\\models\\floor.obj");
        world.addMesh(floor);

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