/*
 * CRITTERS Critter1.java
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
 *      Diagonal critter
 *      Key Features: Walks in only diagonals, pacifist but will eat clovers
 */

public class Critter1 extends Critter {
	
	@Override
    public String toString() {
        return "1";
    }
	
	private int dir;
	
    public Critter1() {
    	int diagonal = Critter.getRandomInt(4);
    	dir = diagonal*2 + 1;
    }

    public boolean fight(String opponent) { // Diagonals only fight clovers
    	return opponent.equals("@");
    }

    @Override
    public void doTimeStep() {
        /* take one step forward */
    	if (getEnergy() > 30) {
             reproduce(new Critter1(), Critter.getRandomInt(8));
        } else {
        	walk(dir);
        }
    }

    public static void runStats(List<Critter> diagonals) {
    	int dNum = 0;
    	
        if (diagonals.size() > 0) {
            for (Object obj : diagonals) {
                dNum++;
            }
            System.out.println("There are " + dNum + " diagonals alive,");
        } 
        else {
            System.out.println("There are no diagonals alive");
        }
    }
    
}
