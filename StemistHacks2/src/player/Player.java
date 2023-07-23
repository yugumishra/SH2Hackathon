package player;

import org.joml.Vector3f;
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
	
	public Player(Camera cam) {
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
}
