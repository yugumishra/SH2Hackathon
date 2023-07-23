package player;

import visual.Mesh;

public class Interval implements Comparable<Interval>{
	public float min;
	public float max;
	public Mesh o;
	
	public Interval(Mesh o) {
		min = Float.MAX_VALUE;
		max = -Float.MAX_VALUE;
		this.o = o;
	}
	
	public Interval(float min, float max, Mesh o) {
		this.min = min;
		this.max = max;
		this.o = o;
	}
	
	public Interval(float min, float max) {
		this.min = min;
		this.max = max;
		this.o = null;
	}
	
	public static boolean overlap(Interval i, Interval i2) {
		return (i.inRange(i2.min) || i.inRange(i2.max) || i2.inRange(i.min) || i2.inRange(i.max));   
	}
	
	public void setMin(float m) {
		min = m;
	}
	
	public void setMax(float m) {
		max = m;
	}
	
	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}
	
	public boolean inRange(float val) {
		return val >= min && val <= max;
	}

	@Override
	public int compareTo(Interval o) {
		if(o.min == this.min) {
			return 0;
		}else if(o.min < this.min) {
			return -1;
		}else {
			return 1;
		}
	}
	
	@Override
	public String toString() {
		return "Min: " + min + ", Max: " + max;
	}
}
