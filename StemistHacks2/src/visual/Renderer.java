package visual;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Renderer {

    private int program;
    
    private HashMap<String, Integer> uniforms;
    
    public Renderer() {
    	uniforms = new HashMap<String, Integer>();
    }
    
    public void addUniform(String u) {
    	int location = GL20.glGetUniformLocation(program, u);
    	if(location < 0) {
    		System.err.println(u + " uniform not found");
    		return;
    	}
    	uniforms.put(u, location);
    }
    
    public void sendMat4(String u, Matrix4f val) {
    	try {
    		FloatBuffer fb = MemoryUtil.memAllocFloat(16);
    		val.get(fb);
    		GL20.glUniformMatrix4fv(uniforms.get(u), false, fb);
    	}catch(Exception e) {
    		System.err.println("We couldn't send the matrix");
    	}
    }
    
    public void send1i(String u, int val) {
    	try {
    		GL20.glUniform1i(uniforms.get(u), val);
    	}catch(Exception e) {
    		System.err.println("We couldn't send the texture");
    	}
    }
    
    public void send3f(String u, Vector3f val) {
    	try {
    		GL20.glUniform3f(program, val.x, val.y, val.z);
    	}catch(Exception e) {
    		System.err.println("We couldn't send the light position");
    	}
    }
    
    //intializes the shaders & shader program
    public void init() {
        //get the shaders as strings
        String vertexShader = Util.scanShader("Assets\\shaders\\vertex.vs");
        String fragmentShader = Util.scanShader("Assets\\shaders\\fragment.fs");

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
            System.err.println(GL20.glGetShaderInfoLog(fid, 1000));
            System.err.println(GL20.glGetShaderInfoLog(vid, 1000));
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
        
        //create uniforms
        addUniform("projectionMatrix");
        addUniform("viewMatrix");
        addUniform("texture");
        addUniform("lightPos");
        addUniform("modelMatrix");
    }

    public void clearColor() {
        //clear the color buffer and the depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Drawable m) {
        if(m.isLoaded() == false) {
            //object not loaded yet
            m.init();
        }
        
        //load model matrix
        Matrix4f mat = m.getModelMat();
        sendMat4("modelMatrix", mat);
        
        //bind the mesh's vertex array object (vao is kind of a pointer to the mesh's vbo) so opengl knows what we're trying to draw
        GL30.glBindVertexArray(m.getVao());
        //enable the formatting (index is which format you're enabling)
        //so 0 means we're enabling the positions
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        
        //bind ibo
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, m.getIbo());
        
        //bind texture unit
        send1i("texture", 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, m.getTexID());

        //draw it
       	GL20.glDrawElements(GL11.GL_TRIANGLES, m.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
       
        

        //unbind and disable
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        
      //error check
      		int error = GL11.glGetError();
      		if (error != GL11.GL_NO_ERROR) {
      		    System.out.println("OpenGL Error Render check: " + error);
      		}
    }
    
    public void cleanup() {
    	GL20.glDeleteProgram(program);
    }
}
