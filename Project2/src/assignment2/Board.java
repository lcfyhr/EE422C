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

//Board class: this class will serve as the imaginary board that holds all the guesses, hints, and secret code.

package assignment2;

public class Board {
    Code history[];
    int size;
    int currInd;

    Board (int size, Code answer) {
        history = new Code[size + 1];
        this.size = size + 1;
        currInd = 0;
        addToBoard(answer);
    }

    //addToBoard(code):
    //Pass a code into this method to add it to the board history, where the secrete code is always at history[0].

    public void addToBoard(Code code) {
        history[currInd] = code;
        currInd++;
    }

    //getHint(code):
    //Pass a code into this method to get the hint response that tells you how many black or white pegs exist. After
    //getting a response peg for a peg in the code, remove that from being checked next time.

    public String getHint(Code code) {
        String match = history[0].toString();
        String toMatch = code.toString();
        int b = 0;
        int w = 0;
            for (int j = 0; j < code.toString().length(); j++) {
                if (history[0].toString().charAt(j) == code.toString().charAt(j)) {
                    b++;
                    match = match.substring(0, j) + "_" + match.substring(j + 1);           //black pegs
                    toMatch = toMatch.substring(0, j) + "*" + toMatch.substring(j + 1);
                }
            }
            for (int i = 0; i < toMatch.length(); i++) {
                for (int j = 0; j < match.length(); j++) {
                    if (toMatch.charAt(i) == match.charAt(j)) {
                        w++;
                        match = match.substring(0, j) + "_" + match.substring(j+1);         //white pegs
                        toMatch = toMatch.substring(0, i) + "*" + toMatch.substring(i+1);
                    }
                }
            }
        return b + "b_" + w + "w";
    }

    public int getCurrInd() {
        return currInd;
    }

    public Code getCode(int ind) {
        return history[ind];
    }

    public boolean isFull() {
        return currInd >= size;
    }

    //getHistory():
    //Void method that will print out the strings of each code to console along with their hint responses.

    public void getHistory() {
        for (int i = 1; i < currInd; i++) {
            System.out.println(getCode(i).toString() + " -> " + getHint(getCode(i)));
        }
    }

}
