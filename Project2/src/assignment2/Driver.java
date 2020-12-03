/*
 * Mastermind
 * Sep 21, 2020
 *
 * EE422C Project 2 (Mastermind) submission by
 * Lars Fyhr
 * lcf597
 * Slip days used: <1>
 * Fall 2020
 */

package assignment2;

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        // Use this for your testing.  We will not be calling this method.
        String ca[] = {"B","G","O","P","R","Y"};
        GameConfiguration gc = new GameConfiguration(12, ca, 4);
        SecretCodeGenerator scg = new SecretCodeGenerator(gc);
        start(true, gc, scg);
    }

    public static void start(Boolean isTesting, GameConfiguration config, SecretCodeGenerator generator) {
        // TODO: complete this method
		// We will call this method from our JUnit test cases.
        System.out.println("Welcome to Mastermind.");
        Game g = new Game(isTesting, config, generator);
        g.runGame();
    }
}
