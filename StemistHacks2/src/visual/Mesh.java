package visual;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

public class Mesh implements Drawable{
    //the data of the mesh the vertices
    private float[] vertices;
    private int[] indices;
    private String[] filepaths;
    //name
    private String name;

    //variables for the vertex buffer object and the vertex array object
    private int vao;
    private int vbo;
    //variables for index buffer object and texture object
    private int ibo;
    private int texId;
    //num vertices
    private int vertexCount;

    //boolean to indicate whether loaded
    public boolean loaded;
    
    //position and rotation of the mesh in world space
    private Vector3f position;
    private Vector3f rotation;

    //constructor
    public Mesh(String name, float[] vertices, int[] indices, String[] paths) {
        this.vertices = vertices;
        this.name = name;
        this.indices = indices;
        loaded = false;
        this.filepaths = paths;
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    //method that will load the mesh into gpu memory
    public void init() {
    	loadTextures();
        //create a float buffer to hold the vertices
        FloatBuffer fb = MemoryUtil.memAllocFloat(vertices.length);
        fb.put(vertices).flip();
        
        //create a int buffer to hold the indices
        IntBuffer ib = MemoryUtil.memAllocInt(indices.length);
        ib.put(indices).flip();

        //generate vertex array object
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        
        //generate vertex buffer object
        vbo = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
        //buffer the vertex data
        GL20.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL20.GL_STATIC_DRAW);
        
        //generate index buffer object
        ibo = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, ibo);
        //buffer the index data
        GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, ib, GL20.GL_STATIC_DRAW);

        //specifing the format of the data in the gpu
        //position - index 0, 3 numbers, float, not normalized, vertex size 8 floats, internal offset 0
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * (3 + 3 + 3),  0);
        // normals - index 1, 3 numbers, float, not normalized, vertex size 8 floats, internal offset 3 floats
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, Float.BYTES * (3 + 3 + 3), Float.BYTES * (3));
        // texture - index 2, 2 numbers, float, not normalized, vertex size 8 floats, internal offset 6 floats
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Float.BYTES * (3 + 3 + 3), Float.BYTES * (3 + 3));

        GL30.glBindVertexArray(0);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);

        //enables depth testing (necessary for multiple fragments stacked on top of each other and need to know which one to render)
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        MemoryUtil.memFree(fb);
        MemoryUtil.memFree(ib);

        //set the vertex count (necessary for draw call)
        vertexCount = indices.length;
        loaded = true;
    }
    
    public void loadTextures() {
    	if(filepaths == null) {
    		return;
    	}
		// gen id
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		texId = GL11.glGenTextures();
		
		// bind
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texId);
		
		//read image data
		List<ByteBuffer> imageBuffers = new ArrayList<ByteBuffer>();
		int maxW = 0, maxH = 0;
		// load data
		STBImage.stbi_set_flip_vertically_on_load(true);
		for(int i = 0; i< filepaths.length; i++) {
			int[] width = new int[1];
			int[] height = new int[1];
			int[] channels = new int[1];
			imageBuffers.add(STBImage.stbi_load(filepaths[i], width, height, channels, 4));
			if(width[0] > maxW) {
				maxW = width[0];
			}
			if(height[0] > maxH) {
				maxH = height[0];
			}
		}
		
		ByteBuffer imageBuffer = combine(imageBuffers);
		// send to gpu
		int numMipmapLevels = (int) (Math.floor(Math.log(Math.max(maxW, maxH)) / Math.log(2)) + 1);
		GL42.glTexStorage3D(GL42.GL_TEXTURE_2D_ARRAY, numMipmapLevels, GL42.GL_RGBA8, maxW, maxH, filepaths.length);
		
		// error check
		int error = GL11.glGetError();
		if (error != GL11.GL_NO_ERROR) {
			System.out.println("OpenGL Error check 1: " + error);
		}
		
		GL30.glTexSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0, maxW, maxH, filepaths.length, GL42.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
		
		// mipmap time
		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D_ARRAY);
		
		// parameters
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_S, GL13.GL_REPEAT);
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_T, GL13.GL_REPEAT);
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		// unbind
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);

		// free buffer
		for(int i = 0; i< imageBuffers.size(); i++) {
			MemoryUtil.memFree(imageBuffers.get(i));
		}
		MemoryUtil.memFree(imageBuffer);
		
		// error check
		error = GL11.glGetError();
		if (error != GL11.GL_NO_ERROR) {
			System.out.println("OpenGL Error check 2: " + error);
		}
    }
    
    private ByteBuffer combine(List<ByteBuffer> buffers) {
		int length = 0;
		for(ByteBuffer bb: buffers) {
			bb.rewind();
			length += bb.remaining();
		}
		ByteBuffer buff = MemoryUtil.memAlloc(length);
		for(ByteBuffer bb: buffers) {
			bb.rewind();
			buff.put(bb);
		}
		buff.rewind();
		return buff;
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
    
    //get the index buffer object
    public int getIbo() {
    	return ibo;
    }
    
    //get the texture object
    public int getTexID() {
    	return texId;
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
    
    public Matrix4f getModelMat() {
    	Matrix4f modelMat = new Matrix4f();
    	modelMat.translate(position)
    	.rotate(rotation.x, new Vector3f(1,0,0))
    	.rotate(rotation.y, new Vector3f(0,1,0))
    	.rotate(rotation.z, new Vector3f(0,0,1));
    	return modelMat;
    }
    public void setPos(Vector3f n) {
    	position = n;
    }
    
    public String[] getPaths() {
    	return filepaths;
    }
    
    public void increment() {
    	
    }

	@Override
	public void addRot(Vector3f rot) {
		// TODO Auto-generated method stub
		rotation.add(rot);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setRot(Vector3f other) {
		rotation = other;
	}
}
