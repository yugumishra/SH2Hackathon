package visual;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.joml.Vector3f;

import multiplayer.Comms;
import player.Player;

public class Startup extends Thread{
	public static int portNum = 5000;
	
    private static Window window;
    private static World world;
    private static Renderer renderer;
    private static Platform platforms;
    private static Player p;
    private static Comms comms;
    
    public static void main(String[] args) {
        comms = new Comms();
        comms.start();
        
        Startup sup = new Startup();
        sup.start();
    }
    
    public static Comms getComms() {
    	return comms;
    }
    
    public void run() {
    	int width = 0;
        int height = 0;

        //use toolkit to get the primary monitor width and height
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();

        //create window instance
        window = new Window("SH2", width, height, true);

        //create world and add meshes
        world = new World();
        
      //create player that represents this player
        p = new Player(window.getCamera());
        
        platforms = new Platform();
        
        //floor mesh
        Terrain t = new Terrain();
        Mesh terrain = t.genTerrain();
        world.addDrawable(terrain);
      
        Mesh arena = Util.readObjFile("Assets\\models\\floor.obj");
        platforms.addPlatform(arena);
        
        arena.setPos(new Vector3f(0, 50, 0));
        world.addDrawable(arena);
        

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
    
    public static Platform getPlatform() {
    	return platforms;
    }
    
    public static Player getPlayer() {
    	return p;
    }
}