package player;

public enum Weapon {
	CROSSBOW(3),
	SWORD(6),
	STAFF(4);
	
	private int damage;
	private int attackLength;
	private Weapon(int value) {
		damage = value;
		setupAttackLength();
	}
	
	private void setupAttackLength() {
		switch(damage) {
		case 3:
			//crossbow
			attackLength = 41;
			break;
		case 6:
			//sword
			break;
		case 4:
			//staff
			break;
		}
	}
	
	public int getAttackLength() {
		return attackLength;
	}
	
	public static Weapon getWeapon(int damage) {
		switch(damage) {
		case 3:
			return CROSSBOW;
		case 4:
			return STAFF;
		case 6:
			return SWORD;
		default:
			return CROSSBOW;
		}
	}
	
	public static String getName(int damage) {
		switch(damage) {
		case 3:
			return "Crossbow";
		case 4:
			return "Staff";
		case 5:
			return "Sword";
		default:
			return "Crossbow";
		}
	}
}
