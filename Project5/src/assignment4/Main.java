/*
 * CRITTERS Main.java
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.List;
import java.lang.reflect.Method;

/*
 * Usage: java <pkg name>.Main <input file> test input file is
 * optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main {

    /* Scanner connected to keyboard input, or input file */
    static Scanner kb;

    /* Input file, used instead of keyboard input if specified */
    private static String inputFile;

    /* If test specified, holds all console output */
    static ByteArrayOutputStream testOutputString;

    /* Use it or not, as you wish! */
    private static boolean DEBUG = false;

    /* if you want to restore output to console */
    static PrintStream old = System.out;

    /* Gets the package name.  The usage assumes that Critter and its
       subclasses are all in the same package. */
    private static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     *
     * @param args args can be empty.  If not empty, provide two
     *             parameters -- the first is a file name, and the
     *             second is test (for test output, where all output
     *             to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
            }
            if (args.length >= 2) {
                /* If the word "test" is the second argument to java */
                if (args[1].equals("test")) {
                    /* Create a stream to hold the output */
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    /* Save the old System.out. */
                    old = System.out;
                    /* Tell Java to use the special stream; all
                     * console output will be redirected here from
                     * now */
                    System.setOut(ps);
                }
            }
        } else { // If no arguments to main
            kb = new Scanner(System.in); // Use keyboard and console
        }
        commandInterpreter(kb);

        System.out.flush();
    }

    /* Do not alter the code above for your submission. */

    /**
     * Gets an integer conversion from an entered string (String integer must be >= 0)
     *
     * @param str What kind of String entry is to be converted.
     *        Unqualified integer.
     * @return Integer converted from given String entry if valid or -1 if invalid.
     */

    public static long getInt(String str) {
        boolean isInt = true;
        long num = 0;
        for (int i = 0; i < str.length(); i++) {
            if(((str.charAt(i) - '0') >= 0) && ((str.charAt(i) - '0') <= 9)) {
                num += ((long) Math.pow(10, str.length()-i-1)) * (str.charAt(i) - '0');
            } else {
                isInt = false;
            }
        }
        if (isInt) {
            return num;
        }
        return -1;
    }

    /**
     * Gets the specific entered command from the keyboard line while program is running.
     * What kind of command is to be executed and then Critter class functions to be ran.
     * Prints out "error processing: [entry]" if entry is invalid, otherwise it performs
     * the commands given function.
     */

    private static void commandInterpreter(Scanner kb) {
        System.out.println("critters>");
        boolean running = true;
        String in;
        while (running) {
            in = kb.nextLine();
            in = in.trim();
            String [] ins = in.split(" ");
            if (ins[0].equals("quit")) {                // quits the program
                if (ins.length == 1) {
                    running = false;
                } else {
                    System.out.println("error processing: " + in);
                }
            } else if (ins[0].equals("show")) {         // shows the world of Critters
                if (ins.length == 1) {
                    Critter.displayWorld();
                } else {
                    System.out.println("error processing: " + in);
                }
            } else if (ins[0].equals("clear")) {        // clears the world
                if (ins.length == 1) {
                    Critter.clearWorld();
                } else {
                    System.out.println("error processing: " + in);
                }
            }
            // step command with optional count argument (performs the time passing of the world)
            else if (ins[0].equals("step")) {
                if (ins.length == 1) {
                    Critter.worldTimeStep();
                } else if (ins[1] != null && ins.length == 2) {
                    long num = getInt(ins[1]);
                    if (num >= 0) {
                        for (int s = 0; s < num; s++) {
                            Critter.worldTimeStep();
                        }
                    } else {
                        System.out.println("error processing: " + in);
                    }
                } else if (ins.length > 2) {
                    System.out.println("error processing: " + in);
                }
            }
            // stats command (gets unique statistics about a passed Critter)
            else if (ins[0].equals("stats")) {
                if (ins.length == 2) {
                    try {
                        List<Critter> my_list = Critter.getInstances(ins[1]);
                        Class<?> c = Class.forName(myPackage + "." + ins[1]);
                        Method stats = c.getMethod("runStats", List.class);
                        stats.invoke(null, my_list);

                    } catch (Exception e) {
                        System.out.println("error processing: " + in);
                    }
                } else {
                    System.out.println("error processing: " + in);
                }
            }
            // create command (creates a Critter of the passed instance, and creates a # of them if specified)
            else if (ins[0].equals("create")) {
                if (ins.length == 3 || ins.length == 2) {
                    long num = 0;
                    if (ins.length == 3) {
                        num = getInt(ins[2]);
                    } else {
                        num = 1;
                    }
                    if (num < 0) {
                        System.out.println("error processing: " + in);
                    }
                    for (int c = 0; c < num; c++) {
                        try {
                            Critter.createCritter(ins[1]);
                        } catch (InvalidCritterException e) {
                            System.out.println("error processing: " + in);
                            break;
                        }
                    }

                } else {
                    System.out.println("error processing: " + in);
                }
            }
            // seed command (if just 'seed' then use a seed of 0)
            else if (ins[0].equals("seed")) {
                if (ins.length == 1) {
                    Critter.setSeed(0);
                } else if (ins.length == 2) {
                    long num = getInt(ins[1]);
                    if (num >= 0) {
                        Critter.setSeed(num);
                    } else {
                        System.out.println("error processing: " + in);
                    }
                } else {
                    System.out.println("error processing: " + in);
                }
            } else {
                System.out.println("invalid command: " + in);
            }
        }
    }
}
