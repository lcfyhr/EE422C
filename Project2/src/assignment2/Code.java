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

//Code class: this class will serve as the structure for each code object, whether its a guess or secret key.

package assignment2;

public class Code {
    String code;

    Code(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code;
    }

}
