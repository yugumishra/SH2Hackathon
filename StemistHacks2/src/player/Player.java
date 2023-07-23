package player;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import visual.AnimatedMesh;
import visual.Camera;
import visual.Mesh;
import visual.Startup;
import visual.Util;

public class Player {
	private Camera camera;
	private Weapon currentWeapon;
	private AnimatedMesh weapon;
	private Mesh projectile;
	private int health;
	private int frame;
	private int attackStart;
	private int attackLength;
	private boolean projectileFree;
	private Vector3f projectileVelocity;
	private float yAccel = -0.016f;
	private int runFrame;
	private Interval x;
	private Interval y;
	private Interval z;
	private Interval boxX;
	private Interval boxY;
	private Interval boxZ;
	
	public Player(Camera cam) {
		runFrame = 0;
		
		this.camera = cam;
		health = 20;
		attackStart = -1;
		currentWeapon = Weapon.CROSSBOW;
		attackLength = currentWeapon.getAttackLength();
		weapon = Util.getAnimation("Crossbow", "Assets\\models\\Crossbow.obj", attackLength);
		weapon.setRotation(camera.getRotation());
		projectile = Util.readObjFile("Assets\\models\\dart.obj");
		Startup.getWorld().addDrawable(projectile);
		
		Startup.getWorld().addDrawable(weapon);
		
		projectileFree = false;
		frame = 0;
		projectileVelocity = new Vector3f(0,0,0);
		
		initIntervals();
	}
	
	public void initIntervals() {
		x.setMax(-Float.MAX_VALUE);
		x.setMin(Float.MAX_VALUE);
		y.setMax(-Float.MAX_VALUE);
		y.setMin(Float.MAX_VALUE);
		z.setMax(-Float.MAX_VALUE);
		z.setMin(Float.MAX_VALUE);
		float[] vertices = projectile.getVertices();
		
		for(int i =0; i< vertices.length; i+=9) {
			float xx = vertices[i*9];
			float yy = vertices[i*9 + 1];
			float zz = vertices[i*9 + 2];
			
			if(xx < x.min) {
				x.min = xx;
			}
			if(xx > x.max) {
				x.max = xx;
			}
			if(yy < y.min) {
				y.min = yy;
			}
			if(yy > y.min) {
				y.max = yy;
			}
			if(zz < z.min) {
				z.min = zz;
			}
			if(zz > z.max) { 
				z.max = zz;
			}
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public void update() {
		
		if(attackStart != -1 && frame - attackStart >= attackLength) {
			finishAttack();
		}
		
		camera.update();
		
		Vector3f change = new Vector3f(0, -1, -2);
		change.rotateAxis(camera.getRotation().x, 1, 0, 0);
		change.rotateAxis(camera.getRotation().y, 0, 1, 0);
		Vector3f weaponPos = new Vector3f(camera.getPosition().x + change.x, camera.getPosition().y + change.y, camera.getPosition().z + change.z);
		
		weapon.setPos(weaponPos);
		
		Vector3f weaponRot = new Vector3f(camera.getRotation().x *-2, camera.getRotation().y *-2, camera.getRotation().z * -2);
		weapon.setRotation(weaponRot);
		
		
		
		if(projectileFree) {
			projectile.getPosition().add(projectileVelocity);
			projectileVelocity.add(0, yAccel, 0);
			if(projectile.getPosition().y <= 0.0f) {
				projectileFree = false;
			}
		}else if(attackStart != -1) {
			
			switch(frame - attackStart) {
			case 0:
				break;
			case 1:
				change = new Vector3f(0.35f, 0.1f, 0.75f);
				change.rotateAxis(weaponRot.x, 1, 0, 0);
				change.rotateAxis(weaponRot.y, 0, 1, 0);
				change.rotateAxis(weaponRot.z, 0, 0, 1);
				projectile.setPos(new Vector3f(weaponPos.x + change.x, weaponPos.y + change.y, weaponPos.z + change.z));
				break;
			case 2:
				change = new Vector3f(0.35f, 0.1f, 0.5f);
				change.rotateAxis(weaponRot.x, 1, 0, 0);
				change.rotateAxis(weaponRot.y, 0, 1, 0);
				change.rotateAxis(weaponRot.z, 0, 0, 1);
				projectile.setPos(new Vector3f(weaponPos.x + change.x, weaponPos.y + change.y, weaponPos.z + change.z));
				break;
			case 3:
				change = new Vector3f(0.35f, 0.1f, 0.25f);
				change.rotateAxis(weaponRot.x, 1, 0, 0);
				change.rotateAxis(weaponRot.y, 0, 1, 0);
				change.rotateAxis(weaponRot.z, 0, 0, 1);
				projectile.setPos(new Vector3f(weaponPos.x + change.x, weaponPos.y + change.y, weaponPos.z + change.z));
				break;
			case 4:
				change = new Vector3f(0.35f, 0.1f, 0.0f);
				change.rotateAxis(weaponRot.x, 1, 0, 0);
				change.rotateAxis(weaponRot.y, 0, 1, 0);
				change.rotateAxis(weaponRot.z, 0, 0, 1);
				projectile.setPos(new Vector3f(weaponPos.x + change.x, weaponPos.y + change.y, weaponPos.z + change.z));
				freeProjectile();
				break;
			default:
				break;
			}
		}else{
			projectile.setRot(weaponRot);
			change = new Vector3f(0.35f, 0.1f, 1.0f);
			change.rotateAxis(weaponRot.x, 1, 0, 0);
			change.rotateAxis(weaponRot.y, 0, 1, 0);
			change.rotateAxis(weaponRot.z, 0, 0, 1);
			projectile.setPos(new Vector3f(weaponPos.x + change.x, weaponPos.y + change.y, weaponPos.z + change.z));
		}
		
		//send information to other player
		//data formatting
		//first 16 floats, player's model matrix (position, rotation)
		//second 16 floats, current's crossbow's model matrix (position, rotation)
		//1 number representing which frame of the player animation we are on
		//24 floats representing the bounding box of the crossbow projectile
		
    	Matrix4f playerMat = new Matrix4f();
    	playerMat.translate(camera.getPosition())
    	.rotate(camera.getRotation().x, new Vector3f(1,0,0))
    	.rotate(camera.getRotation().y, new Vector3f(0,1,0))
    	.rotate(camera.getRotation().z, new Vector3f(0,0,1));
    	
    	Matrix4f crossbowMat = weapon.getModelMat();
    	
    	//get the 3 ranges that define the crossbow's bounding box
    	if(projectileFree) {
    		Vector4f[] vecs = new Vector4f[8];
    		vecs[0] = new Vector4f(x.min, y.min, z.min, 1.0f);
    		vecs[1] = new Vector4f(x.min, y.min, z.max, 1.0f);
    		vecs[2] = new Vector4f(x.min, y.max, z.min, 1.0f);
    		vecs[3] = new Vector4f(x.min, y.max, z.max, 1.0f);
    		vecs[4] = new Vector4f(x.max, y.min, z.min, 1.0f);
    		vecs[5] = new Vector4f(x.max, y.min, z.max, 1.0f);
    		vecs[6] = new Vector4f(x.max, y.max, z.min, 1.0f);
    		vecs[7] = new Vector4f(x.max, y.max, z.max, 1.0f);
    		for(Vector4f v: vecs) {
    			v.mul(crossbowMat);
    		}
    		
    		boxX.setMax(-Float.MAX_VALUE);
    		boxX.setMin(Float.MAX_VALUE);
    		boxY.setMax(-Float.MAX_VALUE);
    		boxY.setMin(Float.MAX_VALUE);
    		boxZ.setMax(-Float.MAX_VALUE);
    		boxZ.setMin(Float.MAX_VALUE);
    		
    		for(Vector4f v: vecs) {
    			float xx = v.x;
    			float yy = v.y;
    			float zz = v.z;
    			
    			if(xx < boxX.min) {
    				boxX.min = xx;
    			}
    			if(xx > boxX.max) {
    				boxX.max = xx;
    			}
    			if(yy < boxY.min) {
    				boxY.min = yy;
    			}
    			if(yy > boxY.min) {
    				boxY.max = yy;
    			}
    			if(zz < boxZ.min) {
    				boxZ.min = zz;
    			}
    			if(zz > boxZ.max) { 
    				boxZ.max = zz;
    			}
    		}
    	}else {
    		boxX.min = 0.0f;
    		boxX.max = 0.0f;
    		boxY.max = 0.0f;
    		boxY.min = 0.0f;
    		boxZ.min = 0.0f;
    		boxZ.max = 0.0f;
    	}
    	
		
		frame++;
	}
	
	public void startAttack() {
		attackStart = frame;
		weapon.start();
		
	}
	
	public void finishAttack() {
		attackStart = -1;
		weapon.stop();
		freeProjectile();
	}
	
	public void freeProjectile() {
		projectileFree = true;
		Vector3f orientation = projectile.getRotation();
		float hypotenuse = (float) (Math.cos(orientation.x)) * 1;
		projectileVelocity.y = (float) (Math.sin(orientation.x)) * 1;
		projectileVelocity.x = (float) (-1.0 * Math.sin(orientation.y)) * hypotenuse;
		projectileVelocity.z = (float) (-1.0 * Math.cos(orientation.y)) * hypotenuse; 
	}
	
	public void switchWeapon(int damage) {
		if(attackStart != -1) return;
		currentWeapon = Weapon.getWeapon(damage);
		attackLength = currentWeapon.getAttackLength();
		weapon.cleanup();
		weapon = Util.getAnimation(Weapon.getName(damage), "Assets\\models\\" + Weapon.getName(damage) + ".obj", attackLength);
		weapon.init();
	}
	
	public void numberPressed(int keycode) {
		switch(keycode) {
			case GLFW.GLFW_KEY_1:
				//crossbow
				switchWeapon(3);
				break;
			case GLFW.GLFW_KEY_2:
				//sword
				switchWeapon(4);
				break;
			case GLFW.GLFW_KEY_3:
				//sword
				switchWeapon(6);
				break;
			default:
				break;
		}
	}
	
	public void incrementRunFrames() {
		if(runFrame + 1 >= 20) {
			runFrame = 0;
		}
		runFrame++;
	}
}
