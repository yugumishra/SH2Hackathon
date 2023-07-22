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
