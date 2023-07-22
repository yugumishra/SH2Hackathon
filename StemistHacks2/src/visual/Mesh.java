package visual;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
    //the data of the mesh the vertices
    private float[] vertices;
    //name
    private String name;

    //variables for the vertex buffer object and the vertex array object
    private int vao;
    private int vbo;
    //num vertices
    private int vertexCount;

    //boolean to indicate whether loaded
    public boolean loaded;

    //constructor
    public Mesh(String name, float[] vertices) {
        this.vertices = vertices;
        this.name = name;
        loaded = false;
    }

    //method that will load the mesh into gpu memory
    public void loadMesh() {
        //create a float buffer to hold the vertices
        FloatBuffer fb = MemoryUtil.memAllocFloat(vertices.length);
        fb.put(vertices).flip();

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
        GL20.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL20.GL_STATIC_DRAW);

        //specifing the format of the data in the gpu
        //position - index 0, 2 numbers, float, not normalized, vertex size 2 floats, internal offset 0
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * (3),  0);

        GL30.glBindVertexArray(0);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);

        //enables depth testing (necessary for multiple fragments stacked on top of each other and need to know which one to render)
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        MemoryUtil.memFree(fb);

        //set the vertex count (necessary for draw call)
        vertexCount = vertices.length/3;
        loaded = true;
        System.out.println(vao + ": " + vbo);
    }

    //get whether this mesh is loaded or not
    public boolean isLoaded() {
        return loaded;
    }

    //get the vertex array object
    public int getVao() {
        return vao;
    }

    //get the vertex buffer object
    public int getVbo() {
        return vbo;
    }

    //getter for the mesh vertex count
    public int getVertexCount() {
        return vertexCount;
    }
    
    //getter for name used for debugging
    public String getName() {
    	return name;
    }

    //clean up method for this mesh
    //cleans up buffers and vao
    public void cleanup() {
        GL30.glDisableVertexAttribArray(vao);

        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        
        GL20.glDeleteBuffers(vbo);
        
        GL30.glBindVertexArray(0);
    }
}
