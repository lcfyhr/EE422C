/*
 * CRITTERS Critter2.java
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
 *      Breeder critter
 *      Key features: Runs, Likes to breed a lot, Pacifist but will eat Clovers
 */

public class Critter2 extends Critter {
	
	@Override
    public String toString() {
        return "2";
    }

    private int kids;

    public Critter2() {
        kids = 0;
    }

    public boolean fight(String opponent) {
	    return opponent.equals("@");
    }

    @Override
    public void doTimeStep() {
        /* take one step forward */
        if (getEnergy() > 30) {
            reproduce(new Critter2(), Critter.getRandomInt(8));
            kids++;
        } else {
            run(Critter.getRandomInt(8));
        }
    }

    public static void runStats(List<Critter> breeders) {
        int brnum = 0;
        if (breeders.size() > 0) {
            for (Object obj : breeders) {
                Critter2 br = (Critter2) obj;
                System.out.println("Breeder" + brnum + " has " + br.kids + " children");
                brnum++;
            }
        } else {
            System.out.println("There are no breeders alive");
        }
    }
}
