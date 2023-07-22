package visual;

public class Loop {
    private Window window;

    private int frame;
    private long time;
    private float fps;

    public Loop() {
        window = Startup.getWindow();
        time = System.currentTimeMillis();
        frame = 0;
        //as a default
        fps = 60.0f;
    }

    public void init() {
        window.init();
    }

    public void start() {
        init();
        //run forever until the window should close
        while(window.closed() == false) {
            //increment frames
            frame++;
            //update fps every 10 frames
            if(frame % 10 == 0) {
                //calculate fps again
                calculateFPS();
            }

            //important functions
            update();
            input();
            render();
        }
        cleanup();
    }

    public void calculateFPS() {
        long current = System.currentTimeMillis();
        int diff = (int) (current - time);
        time = current;
        fps = (10.0f) / ((float) diff);
    }

    public void update() {
        window.update();
    }

    public void input() {

    }

    public void render() {

    }

    public void cleanup() {
        window.cleanup();
    }
}
