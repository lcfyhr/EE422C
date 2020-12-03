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

//Game class: this class will serve as the structure for the game with different methods to serve as different areas
//of the game.

public class Game {

    Scanner input;
    boolean isTesting;
    GameConfiguration gc;
    SecretCodeGenerator scg;
    Code answer;
    Board b;
    boolean won;

    Game(boolean isTesting, GameConfiguration gc, SecretCodeGenerator scg) {
        this.isTesting = isTesting;
        this.gc = gc;
        this.scg = scg;
        input = new Scanner(System.in);
        answer = new Code(scg.getNewSecretCode());
        b = new Board(gc.guessNumber, answer);
        won = false;
    }

    //runGame():
    //Void function that starts the initial request to play the game.
    public void runGame() {
        System.out.println("Do you want to play a new game? (Y/N):");
        if (input.nextLine().equals("Y")) {
            continuegame();
        }
    }

    //continueGame():
    //Void function that holds the main loop of the game, and will repeat with successive games.
    public void continuegame() {
        if (isTesting) {
            System.out.println("Secret Code: " + answer);
        }
        while (!b.isFull() && !won) {
            playerTurn();
        }
        if (won) {
            System.out.println("You win!");
            System.out.println("");
        } else {
            System.out.println("You lose! The pattern was " + answer);
            System.out.println("");
        }
        reset();
    }

    //playerTurn():
    //Void function that serves as an individual playerTurn, also calls isValid for validity of guesses. This function
    //will loop for successive player turns.
    public void playerTurn() {
        System.out.println("");
        System.out.println("You have " + (b.size - b.currInd) + " guess(es) left.");
        System.out.println("Enter guess:");
        String guess = input.nextLine();
        if (isValid(guess)) {
            b.addToBoard(new Code(guess));
            System.out.println(guess + " -> " + b.getHint(b.getCode(b.getCurrInd()-1)));
            if (guess.equals(b.history[0].toString())) {
                won = true;
            }
        } else if (guess.equals("HISTORY")) {
            b.getHistory();
        } else {
            System.out.println("INVALID_GUESS");
        }
    }

    //isValid(guess):
    //Pass a guess string into this function to process the validity of the guess and whether or not it will loop back
    //to a playerTurn or continue to process the guess and formulate a response.
    public boolean isValid(String guess) {
        if (guess.length() == gc.pegNumber) {
            for (int i = 0; i < guess.length(); i++) {
                boolean matched = false;
                for (int j = 0; j < gc.colors.length; j++) {
                    if (guess.charAt(i) == gc.colors[j].charAt(0)) {
                        matched = true;
                    }
                }
                if (!matched) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    //reset():
    //Void function that re-prompts the user to play again and then resets the important variables of the game.
    public void reset() {
        System.out.println("Do you want to play a new game? (Y/N):");
        if (input.nextLine().equals("Y")) {
            answer = new Code(scg.getNewSecretCode());
            b = new Board(gc.guessNumber, answer);
            won = false;
            continuegame();
        }
    }
}
