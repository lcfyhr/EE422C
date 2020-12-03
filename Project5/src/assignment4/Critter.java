/*
 * CRITTERS Critter.java
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* 
 * See the PDF for descriptions of the methods and fields in this
 * class. 
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    private int energy = 0;

    private int x_coord;
    private int y_coord;

    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();
    
    protected boolean hasMoved;

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name What kind of Critter is to be created
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
        try {
            Class<?> newCritter = Class.forName(myPackage + "." + critter_class_name);
            Constructor<?> newConstructor = newCritter.getConstructor();
            Object newObject = newConstructor.newInstance();        // try and instantiate new critter
            Critter Crit = (Critter) newObject;                     // with the given class name parameter

            Crit.energy = Params.START_ENERGY;
            Crit.x_coord = getRandomInt(Params.WORLD_WIDTH);        // generate it randomly in the world
            Crit.y_coord = getRandomInt(Params.WORLD_HEIGHT);

            population.add(Crit);
        } catch (Exception e) {
            throw new InvalidCritterException(critter_class_name);  // throw exception if bad class name given
        }
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
    	List<Critter> instancesOfType = new ArrayList<Critter>();//make a list
    	for(Critter critt : population) {//for each critter in population
    		Class<?> critt_class = null;
    		try {
				critt_class = Class.forName(myPackage + "." + critter_class_name);
			} catch (Exception e) {
				throw new InvalidCritterException(critter_class_name);
			}
			if (critt_class.isInstance(critt)) { //check if a critter is of a certain type
				instancesOfType.add(critt); //if so, add it to the new list
			} 	
    	}
        return instancesOfType;
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        population.clear();
        babies.clear();
    }
    /**
     * Calls createCritter, creating a clover a certain amount of times as defined in Params.REFRESH_CLOVER_COUNT
     */
    private static void genClovers() {
    	for (int i = 0; i < Params.REFRESH_CLOVER_COUNT; i++) {
			try {
				createCritter("Clover");
			} catch (InvalidCritterException e) {
				System.out.print("oops");
			}
		}
    }

    /**
     * Runs the time step in the world of Critters
     * Increments time in the world, then it makes all critters perform their given function for doTimeStep(), then it
     * runs the doEncounters() function, updates the rest energy of all Critters, runs genClovers(), moves the babies
     * into the regular population of Critters, culls all dead Critters, and finally updates the move tag on Critters.
     */

    public static void worldTimeStep() {
        // TODO: Complete this method
    	// 1. increment timestep; timestep++;
    	// 2. doTimeSteps();
    	for (Critter p : population) {
            p.doTimeStep();
        }
        // 3. Do the fights. doEncounters();
        doEncounters();
        // 4. updateRestEnergy();
        for (Critter p : population) {
            p.energy -= Params.REST_ENERGY_COST;
        }
        // 5. Generate clover genClover();
        genClovers();
        // 6. Move babies to general population and then empties the babies list.
        population.addAll(babies);
        babies.clear();

        // 7. Culls the dead critters by going through and adding all with no energy to the dead list
        List<Critter> dead = new ArrayList<Critter>();
        for (Critter p : population) {
            if (p.energy <= 0) {
                dead.add(p);
            }
        }
        population.removeAll(dead);

        // 8. updates the move tag for all Critters
        for (Critter p : population) {
            p.hasMoved = false;
        }
    }

    /**
     * Runs the compilation of all encounters in the world of Critters
     * If two Critters occupy the same position, then they must encounter each other in some fashion. The function will
     * run until there should be only one critter remaining in each position.
     */

    public static void doEncounters() {
        boolean encounter = false;
        for (Critter p1 : population) {
            for (Critter p2 : population) {
                if (!p1.equals(p2) && p1.x_coord == p2.x_coord && p1.y_coord == p2.y_coord) {
                    encounter = true;
                    boolean p1fights = p1.fight(p2.toString());
                    boolean p2fights = p2.fight(p1.toString());
                    if (p1.energy == 0 || p2.energy == 0) {
                        break;                                  // can't fight if they're dead
                    }
                    if (p1.x_coord == p2.x_coord && p1.y_coord == p2.y_coord) {
                        int p1roll = 0;
                        if (p1fights && p1.energy > 0) {
                            p1roll = getRandomInt(p1.energy);
                        }
                        int p2roll = 0;                     // randomly roll integers to find a winner of the encounter
                        if (p2fights && p2.energy > 0) {
                            p2roll = getRandomInt(p2.energy);
                        }
                        if (p1roll > p2roll) {
                            //p1 wins and takes 1/2 of p2's energy
                            p1.energy += Math.floor(p2.energy/2);
                            p2.energy = 0;
                        } else {
                            //p2 wins and takes 1/2 of p1's energy
                            p2.energy += Math.floor(p1.energy/2);
                            p1.energy = 0;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        List<Critter> losers = new ArrayList<Critter>();
        for (Critter p : population) {
            if (p.energy <= 0) {                // cull the losers of the encounter
                losers.add(p);
            }
        }
        population.removeAll(losers);

        if (encounter) {
            doEncounters();             // if there was at least one encounter run again to check if there
        }                               // is another encounter after the loser was removed
    }

    /**
     * Displays the world of Critters
     * If two Critters occupy the same position, one is overwritten in the display and cannot be seen.
     * The display is like a torus and wraps around at the edges.
     */

    public static void displayWorld() {
        System.out.print("+");
        for (int i = 0; i < Params.WORLD_WIDTH; i++) {
            System.out.print("-");
        }                                       // prints out top border
        System.out.print("+");
        System.out.println();
        for (int h = 0; h < Params.WORLD_HEIGHT; h++) {
            System.out.print("|");                              // print side border
            for (int w = 0; w < Params.WORLD_WIDTH; w++) {
                boolean found = false;
                for(Critter p : population) {
                    if (p.x_coord == w && p.y_coord == h) {
                        System.out.print(p);                        // print contents if Critter at (x,y) position
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print(" ");          // print empty if nothing there
                }
            }
            System.out.print("|");              // print side border
            System.out.println("");
        }
        System.out.print("+");
        for (int i = 0; i < Params.WORLD_WIDTH; i++) {
            System.out.print("-");                              // print bottom border
        }
        System.out.print("+");
        System.out.println("");
    }

    /**
     * Prints out how many Critters of each type there are on the
     * board.
     *
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }
    
	/**
	 * Private method to easily return the X coordinate by a certain number of steps.
	 * Accounts for world wrapping horizontally.
	 * Update the position value with the line 'x_coord = moveX(steps);'
	 * @param steps, how many tiles to move
	 * @return new X position value
	 */
	private final int moveX(int steps) {
		if ((steps + this.x_coord) < 0){//if negative wrap to right
			return (Params.WORLD_WIDTH - steps);
		} 
		else if ((steps + this.x_coord) > (Params.WORLD_WIDTH - 1)) {//if too far right, wrap to left
			return (steps - 1);
		}
		else {
			return this.x_coord + steps;
		}
	}

    /**
     * Private method to easily return the Y coordinate by a certain number of steps.
     * Accounts for world wrapping vertically.
     * Update the position value with the line 'y_coord = moveY(steps);'
     * @param steps, how many tiles to move
     * @return new Y position value
     */
    private final int moveY(int steps) {
        if ((this.y_coord + steps) < 0){ //if you go past bottom, wrap to top
            return (Params.WORLD_HEIGHT + (this.y_coord + steps));//can never go past bottom with a positive direction
        }
        else if ((this.y_coord + steps) >= Params.WORLD_HEIGHT) { //if you go too high, wrap to bottom
            return ((this.y_coord + steps) - Params.WORLD_HEIGHT); //can never go past height limit with a negative direction
        }
        else { //default case
            return this.y_coord + steps;
        }
    }
    
    /**
     * A protected method that uses direction to call move. Calls move in given direction with magnitude 2.
     * @param direction in which to walk
     */
    protected final void walk(int direction) {//Move 1 space in a direction and subtract walk energy
        if(hasMoved) {
        	energy -= Params.WALK_ENERGY_COST;  //two (or more) times within a single time step, you must
        	return;								//deduct the appropriate energy cost from the critter
        }
        switch(direction) {
        case 0: x_coord = moveX(1);//move right
        	break;
        case 1: x_coord = moveX(1);//move right-up
        y_coord = moveY(-1);
        break;
        case 2: y_coord = moveY(-1);//move up
        	break;
        case 3: x_coord = moveX(-1);//move left-up
        y_coord = moveY(-1);
        	break;
        case 4: x_coord = moveX(-1);//move left
        	break;
        case 5: x_coord = moveX(-1);//move left-down
        y_coord = moveY(1);
        break;
        case 6: y_coord = moveY(1);//move down
        	break;
        case 7: x_coord = moveX(1);//move right-down
        	y_coord = moveY(1);
        	break;
        default: //wrong direction
}
        energy -= Params.WALK_ENERGY_COST;
		hasMoved = true;
    }
    
    /**
     * A protected method that uses direction to call move. Calls move in given direction with magnitude 2.
     * @param direction in which to walk
     */
    protected final void run(int direction) {//like walk but moves 2 space and subtracts run energy
        if(hasMoved) {
        	energy -= Params.RUN_ENERGY_COST; 	//two (or more) times within a single time step, you must
        	return;								//deduct the appropriate energy cost from the critter
        }
        switch(direction) {
        case 0: x_coord = moveX(2);//move right
        	break;
        case 1: x_coord = moveX(2);//move right-up
        	y_coord = moveY(-2);
        	break;
        case 2: y_coord = moveY(-2);//move up
        	break;
        case 3: x_coord = moveX(-2);//move left-up
        	y_coord = moveY(-2);
        	break;
        case 4: x_coord = moveX(-2);//move left
        	break;
        case 5: x_coord = moveX(-2);//move left-down
        	y_coord = moveY(2);
        	break;
        case 6: y_coord = moveY(2);//move down
        	break;
        case 7: x_coord = moveX(2);//move right-down
        	y_coord = moveY(2);
        	break;
        default: //wrong direction
}
        energy -= Params.RUN_ENERGY_COST;
		hasMoved = true;
    }
    /**
     * A protected method is called by a critter. 
     * This method will set the coordinates and energy of the provided offspring.
     * This will add offspring to the babies list but not to the population yet
     * @param direction that offspring will be placed
     * @param offspring critter that will have its energy and position set
     */
    protected final void reproduce(Critter offspring, int direction) {
    	if(this.energy < Params.MIN_REPRODUCE_ENERGY) { //If not enough energy, dont reproduce
			return;
		}
    	
		offspring.energy = (this.energy/2); //Assign the child energy equal to $1/2$ of the parent's energy(rounding fractions down).
		energy = (int) Math.ceil(this.energy/2); // Reassign the parent so that it has $1/2$ of its energy (rounding fraction up).
		
    	switch(direction) { //Assign the child a position indicated by the parent's current position and the specified direction.
        case 0: offspring.x_coord = moveX(1);//move right
        		offspring.y_coord = this.y_coord;
        	break;
        case 1: offspring.x_coord = moveX(1);//move right-up
        		offspring.y_coord = moveY(-1);
        	break;
        case 2: offspring.x_coord = this.x_coord;
        		offspring.y_coord = moveY(-1);//move up
        	break;
        case 3: offspring.x_coord = moveX(-1);//move left-up
        		offspring.y_coord = moveY(-1);
        	break;
        case 4: offspring.x_coord = moveX(-1);
        		offspring.y_coord = this.y_coord;//move left
        	break;
        case 5: offspring.x_coord = moveX(-1);//move left-down
        		offspring.y_coord = moveY(1);
        	break;
        case 6: offspring.x_coord = this.x_coord; //move down
        		offspring.y_coord = moveY(1);
        	break;
        case 7: offspring.x_coord = moveX(1);//move right-down
        		offspring.y_coord = moveY(1);
        	break;
        default: //wrong direction
    	}
    	
    	babies.add(offspring);//Not added to world population 
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
    
}
