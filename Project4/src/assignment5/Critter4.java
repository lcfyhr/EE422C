/*
 * CRITTERS Critter4.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * J. Michael Routh
 * JMR7567
 * 16190
 * Lars Fyhr
 * lcf597
 * 16195
 * Slip days used: <1>
 * Fall 2020
 */

package assignment5;

import java.util.List;

import assignment5.Critter.CritterShape;

/*
 *      Weird critter
 *      Key features: Walks in circles and they form generational lines, breeds after walking in a circle
 */

public class Critter4 extends Critter{
	
	@Override
    public String toString() {
        return "4";
    }

	private int dir;
	private int loops;

    public Critter4() {
        dir = 0;
        loops = 0;
    }

    public boolean fight(String opponent) {
	    String nextCritter = look(dir,true); //look 2 squares ahead of where you are fighting
	    if(!nextCritter.equals("@") || !nextCritter.equals("")) { //if dont see a clover or empty spot
	    	dir += 1; //change direction early
	    }
    	return opponent.equals("@");
    }

    @Override
    public void doTimeStep() {
        /* take one step forward */
        if (loops < 1) {
            walk(dir);
            dir += 1;
        }
        if (dir > 7) {
            dir = dir%8;
            loops++;
            reproduce(new Critter4(), 2);
        }
    }

    public static String runStats(List<Critter> weirds) {
        int totalloops = 0;
        String returnStr = "";
        for (Object obj: weirds) {
            Critter4 w = (Critter4) obj;
            totalloops += w.loops;
        }
        if (weirds.size() > 0) {
        	returnStr = "The " + weirds.size() + " weird critters alive have walked in " + totalloops + " circles";
        	return returnStr;
        } else {
        	returnStr = "There are no weird critters alive";
        	return returnStr;
        }
    }
    
    @Override
	public CritterShape viewShape() {
		CritterShape myShape = CritterShape.CIRCLE;
		return myShape;
	}
    
	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return javafx.scene.paint.Color.DARKRED;
	}
	
	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.INDIANRED;
	}
}
