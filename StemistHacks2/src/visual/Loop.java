package visual;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Loop {
	//references to important game objects
    private Window window;
    private World world;
    private Renderer renderer;

    //internal statistics
    private int frame;
    private long time;
    private float fps;
    
    //isrunning variable
    private boolean isRunning;

    public Loop() {
    	//get from startup
        window = Startup.getWindow();
        world = Startup.getWorld();
        renderer = Startup.getRenderer();
        
        //intialize to right now
        time = System.currentTimeMillis();
        frame = 0;
        //as a default
        fps = 60.0f;
    }

    public void init() {
        window.init();
        renderer.init();
        world.init();
        isRunning = true;
    }

    public void start() {
        init();
        //run forever until the window should close
        while(isRunning) {
            //increment frames
            frame++;
            //update fps every 10 frames
            if(frame % 10 == 0) {
                //calculate fps again
                calculateFPS();
                frame = 0;
            }

            //important functions
            update();
            input();
            render();
            //update window (inputs)
            window.update();
            //check for closing
            if(window.closed()) {
            	stop();
            }
        }
        
        cleanup();
    }
    
    public void stop() {
    	if(isRunning == false) {
    		return;
    	}
    	isRunning = false;
    }

    public void calculateFPS() {
        long current = System.currentTimeMillis();
        int diff = (int) (current - time);
        time = current;
        fps = (10.0f) / ((float) diff);
        fps *= 1000.0f;
    }

    public void update() {
        window.getCamera().update();
    }
    //process inputs
    public void input() {
    	//get values
    	boolean[] keys = window.getKeys();
    	//iterate through values
    	for(int i = 0; i< keys.length; i++) {
    		boolean value = keys[i];
    		//switch
    		Vector3f velocity = new Vector3f(0,0,0);
    		float speed = 1.0f /fps;
    		switch(i) {
    		case 0:
    			//w
    			if(value) velocity.add(new Vector3f(0, 0, -speed));
    			break;
    		case 1:
    			//a
    			if(value) velocity.add(new Vector3f(-speed, 0, 0));
    			break;
    		case 2:
    			//s
    			if(value) velocity.add(new Vector3f(0, 0, speed));
    			break;
    		case 3:
    			//d
    			if(value)  velocity.add(new Vector3f(speed, 0, 0));
    			break;
    		case 4:
    			//space
    			if(value) velocity.add(new Vector3f(0, speed, 0));
    			break;
    		case 5:
    			//left shift
    			if(value) velocity.add(new Vector3f(0, -speed, 0));
    			break;
    		default:
    			break;
    		}
    		window.getCamera().addVelocity(velocity);
    	}
    }

    //here we render the meshes that are in the world
    public void render() {
    	//send projection matrix
    	float aspectRatio = (float) window.getWidth() / window.getHeight();
    	Matrix4f projectionMatrix = Util.getProjectionMatrix(aspectRatio, window.FOV);
    	renderer.sendMat4("projectionMatrix", projectionMatrix);
    	
    	//send view matrix
    	Matrix4f viewMatrix = window.getCamera().getViewMatrix();
    	renderer.sendMat4("viewMatrix", viewMatrix);
    	
        renderer.clearColor();
        for(int i = 0; i< world.getMeshCount(); i++) {
            Mesh m = world.getMesh(i);
            renderer.render(m);
        }
    }

    public void cleanup() {
        window.cleanup();
        for(int i = 0; i< world.getMeshCount(); i++) {
        	Mesh m = world.getMesh(i);
        	m.cleanup();
        }
    }
}
