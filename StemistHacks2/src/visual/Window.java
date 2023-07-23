package visual;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
    //fov constant
    public final float FOV = (float) Math.toRadians(90);

    //variables for window
    public int width;
    public int height;
    public String title;
    //window id
    public long window;
    
    //vsync variable
    private boolean vSync;
    
    //boolean array containing whether or not each key is pressed or not
    boolean[] keys;
    
    //Camera instance
    private Camera cam;

    //constructor
    public Window(String title, int width, int height, boolean vSync) {
        //set values
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        cam = new Camera(width, height);
        keys = new boolean[6];
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
        
        //create framebuffer resize callback to send the new width and height to the camera
        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
        	this.width = width;
        	this.height = height;
        	cam.updateDimensions(width, height);
        });

        //create key callback to determine when keys are pressed
        //key callback now edits the keys array depending on the action
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
            boolean value = true;
            if(action == GLFW.GLFW_RELEASE) {
            	value = false;
            }
            
            Startup.getPlayer().numberPressed(key);
            
            if(key == GLFW.GLFW_KEY_W) {
            	keys[0] = value;
            }
            if(key == GLFW.GLFW_KEY_A) {
            	keys[1] = value;
            }
            if(key == GLFW.GLFW_KEY_S) {
            	keys[2] = value;
            }
            if(key == GLFW.GLFW_KEY_D) {
            	keys[3] = value;
            }
            if(key == GLFW.GLFW_KEY_SPACE) {
            	keys[4] = value;
            }
            if(key == GLFW.GLFW_KEY_LEFT_SHIFT) {
            	keys[5] = value;
            }
        });
        
        //set the input mode for the mouse (disabled)
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        //set to middle just in case not in middle already
        GLFW.glfwSetCursorPos(window, width/2, height/2);
        //set callback for the cursor position moving
        //we'll use this to augment the rotation of the camera
        GLFW.glfwSetCursorPosCallback(window, (window, x, y) -> {
        	cam.processMouseMoved((int) x, (int) y);
        	//set the cursor position back into the middle to prevent too much transformation
        	GLFW.glfwSetCursorPos(window, width/2, height/2);
        });
        
        GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
        	if(action == GLFW.GLFW_RELEASE && button == GLFW.GLFW_MOUSE_BUTTON_1) {
        		//we did an attack
        		Startup.getPlayer().startAttack();
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
    
    public boolean[] getKeys() {
    	return keys;
    }
    
    public Camera getCamera() {
    	return cam;
    }
}
