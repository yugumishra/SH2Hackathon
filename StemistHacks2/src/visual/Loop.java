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
      //send light position
    	renderer.send3f("lightPos", new Vector3f(0, 400, 0));
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
    	

		//division by fps necessary for proper riemann sum
		float speed = 1.0f /fps;
		//angle from y axis to determine which direction the camera is moving in
		float angle = window.getCamera().getRotation().y;
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
    	for(int i = 0; i< keys.length; i++) {
    		boolean value = keys[i];
    		if(value == false) continue;
    		handleInput(speed, sin, cos, i);
    	}
    	
    }
    
    public void handleInput(float speed, float sin, float cos, int i) {
    	Vector3f velocity = new Vector3f(0,0,0);
    	if(i == 0) {
    		//w
    		velocity = new Vector3f(-speed * sin, 0, -speed * cos);
    	}
    	if(i == 1) {
    		//s
    		velocity = new Vector3f(speed * sin, 0, speed * cos);
    	}
    	if(i == 2) {
    		//a
    		velocity = new Vector3f(-speed * cos, 0, speed * sin);
    	}
    	if(i == 3) {
    		//d
    		velocity = new Vector3f(speed * cos, 0, - speed * sin);
    	}
    	if(i == 4) {
    		velocity = new Vector3f(0, speed, 0);
    	}
    	if(i == 5) {
    		velocity = new Vector3f(0, -speed, 0);
    	}
    	window.getCamera().addVelocity(velocity);
    	
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
            
            if(m.getName().equals("Suzanne")) {
            	m.addRot(new Vector3f(0.01f,0.01f,0));
            }
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
