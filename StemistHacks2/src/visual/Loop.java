package visual;

public class Loop {
    private Window window;
    private World world;
    private Renderer renderer;

    private int frame;
    private long time;
    private float fps;
    
    private boolean isRunning;

    public Loop() {
        window = Startup.getWindow();
        world = Startup.getWorld();
        renderer = Startup.getRenderer();

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
                System.out.println(fps);
                frame = 0;
            }

            //important functions
            update();
            input();
            render();
            window.update();
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
        
    }

    public void input() {
    	
    }

    //here we render the meshes that are in the world
    public void render() {
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
