package visual;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    private int program;
    
    //intializes the shaders & shader program
    public void init() {
        //get the shaders as strings
        String vertexShader = Util.scanShader("vertex.vs");
        String fragmentShader = Util.scanShader("fragment.fs");

        //generate vertex shader
        int vid = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vid, vertexShader);

        //compile check
        GL20.glCompileShader(vid);
        int compileStatus = GL20.glGetShaderi(vid, GL20.GL_COMPILE_STATUS);
        if(compileStatus == GL20.GL_FALSE) {
            //shader failed to compile
            System.err.println("Vertex shader failed to compile.");
            System.err.println(GL20.glGetShaderInfoLog(vid, 1000));
            GL20.glDeleteShader(vid);
            System.exit(0);
        }

        //generate fragment shader
        int fid = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fid, fragmentShader);

        //compile check
        GL20.glCompileShader(fid);
        if(GL20.glGetShaderi(vid, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            //shader failed to compile
            System.err.println("Fragment shader failed to compile");
            System.err.println(GL20.glGetShaderInfoLog(fid, 1000));
            GL20.glDeleteShader(fid);
            System.exit(0);
        }

        //link the shaders to a program
        //create program
        program = GL20.glCreateProgram();
        //attach shaders
        GL20.glAttachShader(program, vid);
        GL20.glAttachShader(program, fid);

        //check for link errors
        GL20.glLinkProgram(program);
        int linkStatus = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS);
        if(linkStatus == GL20.GL_FALSE) {
            //program didn't link
            System.err.println("Program failed to link");
            System.err.println(GL20.glGetProgramInfoLog(program, 1000));
            GL20.glDeleteShader(vid);
            GL20.glDeleteShader(fid);
            GL20.glDeleteProgram(program);
            System.exit(0);
        }
        //delete shaders (not necessary anymore)
        GL20.glValidateProgram(program);
        GL20.glDeleteShader(vid);
        GL20.glDeleteShader(fid);
        GL20.glUseProgram(program);
    }

    public void clearColor() {
        //clear the color buffer and the depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Mesh m) {
        if(m.isLoaded() == false) {
            //object not loaded yet
            m.loadMesh();
        }
        
        //bind the mesh's vertex array object (vao is kind of a pointer to the mesh's vbo) so opengl knows what we're trying to draw
        GL30.glBindVertexArray(m.getVao());
        //enable the formatting (index is which format you're enabling)
        //so 0 means we're enabling the positions
        GL20.glEnableVertexAttribArray(0);

        //draw it
        GL20.glDrawArrays(GL11.GL_TRIANGLES, 0, m.getVertexCount());

        //unbind and disable
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        
      //error check
      		int error = GL11.glGetError();
      		if (error != GL11.GL_NO_ERROR) {
      		    System.out.println("OpenGL Error: " + error);
      		}
    }
    
    public void cleanup() {
    	GL20.glDeleteProgram(program);
    }
}
