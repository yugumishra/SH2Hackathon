package visual;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
    //fov constant
    public static final float FOV = (float) Math.toRadians(60);

    //variables for window
    public int width;
    public int height;
    public String title;
    //window id
    public long window;
    
    //vsync variable
    private boolean vSync;

    //constructor
    public Window(String title, int width, int height, boolean vSync) {
        //set values
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    //init method
    //initializes glfw and window 
    //also creates gl capabilities
    //meaning after this method is called we can use opengl stuff
    public void init() {
        //set the printstream for glfw errors
        //helps with debugging
        GLFWErrorCallback.createPrint(System.err).set();

        //check if glfw initialized
        if(!GLFW.glfwInit()) {
            System.err.println("Unable to initialize GLFW");
            System.exit(0);
        }
        //window hints
        GLFW.glfwDefaultWindowHints();
        //visible resizeable false (for now)
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        //opengl version
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        //establish core profile
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        //maximized window hint set to true so the game will maximize every time
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GL11.GL_TRUE);

        //create window
        //used memoryutil.null because it returns the long version of null (not object)
        window = GLFW.glfwCreateWindow(width, height, title, GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);

        //check if window failed to create
        if(window == MemoryUtil.NULL) {
            System.err.println("Failed to create GLFW window");
            System.exit(0);
        }

        //create key callback to determine when keys are pressed
        //for now a very basic key callback to close the window when escape is pressed
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });
        
       

        //maximize the window (even though we set the hint)
        GLFW.glfwMaximizeWindow(window);
        //make the context 
        GLFW.glfwMakeContextCurrent(window);
        
        //check for vysnc
        if(vSync) {
			GLFW.glfwSwapInterval(1);
		}

        //time to show
        GLFW.glfwShowWindow(window);
        GLFW.glfwSetWindowShouldClose(window, false);

        //create gl capabilities
        GL.createCapabilities();

        //set the clear color (bg color)
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    //update method
    //basically looks for any action events & swaps the buffers
    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    //cleanup
    //cleans up the window and destroys it
    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }

    //returns whether or not the window should be closed yet
    public boolean closed() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
