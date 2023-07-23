package player;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import visual.AnimatedMesh;
import visual.Camera;
import visual.Startup;
import visual.Util;

public class Player {
	private Camera camera;
	private Weapon currentWeapon;
	private AnimatedMesh weapon;
	private int health;
	private int frame;
	private int attackStart;
	private int attackLength;
	
	public Player(Camera cam) {
		this.camera = cam;
		health = 20;
		attackStart = -1;
		currentWeapon = Weapon.CROSSBOW;
		attackLength = currentWeapon.getAttackLength();
		weapon = Util.getAnimation("Crossbow", "Assets\\models\\Crossbow.obj", attackLength);
		weapon.setRotation(camera.getRotation());
		Startup.getWorld().addDrawable(weapon);
		
		frame = 0;
		
	}
	
	public int getHealth() {
		return health;
	}
	
	public void update() {
		
		if(attackStart != -1 && frame - attackStart >= attackLength) {
			finishAttack();
		}
		
		camera.update();
		Vector3f weaponPos = new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
		float angle = camera.getRotation().y;
		weaponPos.y -= 1;
		weaponPos.z -= (float) (2 * Math.cos(angle));
		weapon.setPos(weaponPos);
		weapon.setRotation(new Vector3f(camera.getRotation().x *-2, camera.getRotation().y *-2, camera.getRotation().z * -2));
		
		frame++;
	}
	
	public void startAttack() {
		attackStart = frame;
		weapon.start();
	}
	
	public void finishAttack() {
		attackStart = -1;
		weapon.stop();
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
