package multiplayer;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import player.Interval;

public class Packet {
	private Matrix4f playerMat;
	private Matrix4f crossbowMat;
	private int runFrame;
	private Interval x;
	private Interval y;
	private Interval z;
	
	public Packet(Matrix4f playerMat, Matrix4f crossbowMat, int runFrame, Interval x, Interval y, Interval z) {
		this.playerMat = playerMat;
		this.crossbowMat = crossbowMat;
		this.runFrame = runFrame;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		//first we put the matrices
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		//column major
		sb.append(playerMat.m00());
		sb.append(",");
		sb.append(playerMat.m10());
		sb.append(",");
		sb.append(playerMat.m20());
		sb.append(",");
		sb.append(playerMat.m30());
		sb.append(",");
		sb.append(playerMat.m01());
		sb.append(",");
		sb.append(playerMat.m11());
		sb.append(",");
		sb.append(playerMat.m21());
		sb.append(",");
		sb.append(playerMat.m31());
		sb.append(",");
		sb.append(playerMat.m02());
		sb.append(",");
		sb.append(playerMat.m12());
		sb.append(",");
		sb.append(playerMat.m22());
		sb.append(",");
		sb.append(playerMat.m32());
		sb.append(",");
		sb.append(playerMat.m03());
		sb.append(",");
		sb.append(playerMat.m13());
		sb.append(",");
		sb.append(playerMat.m23());
		sb.append(",");
		sb.append(playerMat.m33());
		//divider
		sb.append("|");
		//crossbow matrix
		sb.append(crossbowMat.m00());
		sb.append(",");
		sb.append(crossbowMat.m10());
		sb.append(",");
		sb.append(crossbowMat.m20());
		sb.append(",");
		sb.append(crossbowMat.m30());
		sb.append(",");
		sb.append(crossbowMat.m01());
		sb.append(",");
		sb.append(crossbowMat.m11());
		sb.append(",");
		sb.append(crossbowMat.m21());
		sb.append(",");
		sb.append(crossbowMat.m31());
		sb.append(",");
		sb.append(crossbowMat.m02());
		sb.append(",");
		sb.append(crossbowMat.m12());
		sb.append(",");
		sb.append(crossbowMat.m22());
		sb.append(",");
		sb.append(crossbowMat.m32());
		sb.append(",");
		sb.append(crossbowMat.m03());
		sb.append(",");
		sb.append(crossbowMat.m13());
		sb.append(",");
		sb.append(crossbowMat.m23());
		sb.append(",");
		sb.append(crossbowMat.m33());
		//divider
		sb.append("|");
		sb.append(runFrame);
		sb.append("|");
		sb.append(x.min);
		sb.append(",");
		sb.append(x.max);
		sb.append("|");
		sb.append(y.min);
		sb.append(",");
		sb.append(y.max);
		sb.append("|");
		sb.append(z.min);
		sb.append(",");
		sb.append(z.max);
		
		return sb.toString();
	}
	
	public static Packet interpret(String in) {
		String[] components = in.split("|");
		String[] playerMat = components[0].split(",");
		String[] crossbowMat = components[1].split(",");
		int runFrame = Integer.valueOf(components[3]);
		String[] x = components[4].split(",");
		String[] y = components[5].split(",");
		String[] z = components[6].split(",");
		Interval xx = new Interval(Float.valueOf(x[0]), Float.valueOf(x[1]));
		Interval yy = new Interval(Float.valueOf(y[0]), Float.valueOf(y[1]));
		Interval zz = new Interval(Float.valueOf(z[0]), Float.valueOf(z[1]));
		
		float[] playerValues = new float[16];
		for(int i = 0; i< playerValues.length; i++) {
			playerValues[i] = Float.valueOf(playerMat[i]);
		}
		FloatBuffer playerBuffer = MemoryUtil.memAllocFloat(16);
		playerBuffer.put(playerValues).flip();
		
		Matrix4f player = new Matrix4f(playerBuffer);
		
		float[] crossbowValues = new float[16];
		for(int i = 0; i< crossbowValues.length; i++) {
			crossbowValues[i] = Float.valueOf(crossbowMat[i]);
		}
		FloatBuffer crossbowBuffer = MemoryUtil.memAllocFloat(16);
		playerBuffer.put(playerValues).flip();
		
		Matrix4f crossbow = new Matrix4f(playerBuffer);
		MemoryUtil.memFree(crossbowBuffer);
		MemoryUtil.memFree(playerBuffer);
		return new Packet(player, crossbow, runFrame, xx, yy, zz);
	}
}
