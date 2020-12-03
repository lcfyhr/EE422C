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

package assignment4;

import java.util.List;

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

    public static void runStats(List<Critter> weirds) {
        int totalloops = 0;
        for (Object obj: weirds) {
            Critter4 w = (Critter4) obj;
            totalloops += w.loops;
        }
        if (weirds.size() > 0) {
            System.out.println("The " + weirds.size() + " weird critters alive have walked in " + totalloops + " circles");
        } else {
            System.out.println("There are no weird critters alive");
        }
    }
}
