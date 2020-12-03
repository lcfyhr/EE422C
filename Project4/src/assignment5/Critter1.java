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

package assignment5;

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
        	String lookStr = look(dir, false);
        	if(lookStr.equals(null) || lookStr.equals("@")) //critter will look ahead one walk space
        		walk(dir); //if spot is unoccupied or a clover, critter will walk ahead
        	else {
        		int random2 = Critter.getRandomInt(2);
        		if(random2 == 1) {
        			dir += 2; //critter will change diagonals
        			if(dir == 9)
        				dir = 1; //if overlapped, change to 1
        			walk(dir); //and will walk without checking
        		}
        		else {
        			dir -= 2; //critter will change diagonals
        			if(dir == -1)
        				dir = 7; //if underlapped change to 7
        			walk(dir); //and will walk without checking
        		}
        	}	
        }
    }

    public static String runStats(List<Critter> diagonals) {
    	int dNum = 0;
    	String returnStr = "";
        if (diagonals.size() > 0) {
            for (Object obj : diagonals) {
                dNum++;
            }
            returnStr = "There are " + dNum + " diagonals alive";
            return returnStr;
        } 
        else {
            returnStr = "There are no diagonals alive";
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
		return javafx.scene.paint.Color.AQUA;
	}
	
	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.GREY;
	}
}
