package visual;

import java.util.ArrayList;

public class World {
    private ArrayList<Drawable> Drawablees;
    public World() {
        Drawablees = new ArrayList<Drawable>();
    }

    public void addDrawable(Drawable m) {
        Drawablees.add(m);
    }
    
    public void removeDrawable(Drawable m) {
    	for(Drawable d: Drawablees) {
    		if(d.equals(m)) {
    			Drawablees.remove(m);
    			return;
    		}
    	}
    }
    
    public void init() {
    	for(Drawable m: Drawablees) {
    		m.init();
    	}
    }

    public Drawable getDrawable(int i) {
        return Drawablees.get(i);
    }

    public int getDrawableCount() {
        return Drawablees.size();
    }
}
