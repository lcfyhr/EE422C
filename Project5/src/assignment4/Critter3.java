/*
 * CRITTERS Critter3.java
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
 *      Scary critter
 *      Key features: Runs in lines in a random direction for 5 turns, Great fighter, Can't reproduce and doesnt eat clovers
 */

public class Critter3 extends Critter{
	
	@Override
    public String toString() {
        return "3";
    }

	private int dir;
	private int runTimes;
	
    public Critter3() {
		dir = Critter.getRandomInt(8);
		runTimes = 0;
    }

    public boolean fight(String opponent) { //fights everything but clovers
    	return (!opponent.equals("@"));
    }

    @Override
    public void doTimeStep() {
        /* take one step forward */
    	run(dir);
    	runTimes++;
    }

    public static void runStats(List<Critter> scary) {
    	int scaryNum = 0;
    	int totalRunTimes = 0;
        if (scary.size() > 0) {
            for (Object obj : scary) {
                Critter3 scaryC = (Critter3) obj;
                totalRunTimes += scaryC.runTimes;
                scaryNum++;
            }
            System.out.println("There are " + scaryNum + " alive that have ran " + totalRunTimes + "times.");
        } 
        else {
            System.out.println("There are no scary critters alive");
        }
    }
}
