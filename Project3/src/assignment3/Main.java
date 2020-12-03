/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2020
 */


package assignment3;
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Main {

    // static variables and constants only here.

    public static void main(String[] args) throws Exception {

        Scanner kb;	// input Scanner for commands
        PrintStream ps;	// output file, for student testing and grading only
        // If arguments are specified, read/write from/to files instead of Std IO.
        if (args.length != 0) {
            kb = new Scanner(new File(args[0]));
            ps = new PrintStream(new File(args[1]));
            System.setOut(ps);			// redirect output to ps
        } else {
            kb = new Scanner(System.in);// default input from Stdin
            ps = System.out;			// default output to Stdout
        }
        initialize();
        while (true) {
            ArrayList<String> words = parse(kb);
            System.out.println("The entered words are: " + words);
            ArrayList<String> wl = getWordLadderDFS(words.get(0), words.get(1));
            System.out.println(wl);
            wl = getWordLadderBFS(words.get(0), words.get(1));
            System.out.println(wl);
        }

        // TODO methods to read in words, output ladder
    }

    public static void initialize() {
        // initialize your static variables or constants here.
        // We will call this method before running our JUNIT tests.  So call it
        // only once at the start of main.
    }

    /**
     * @param keyboard Scanner connected to System.in
     * @return ArrayList of Strings containing start word and end word.
     * If command is /quit, return empty ArrayList.
     */
    public static ArrayList<String> parse(Scanner keyboard) {
        // TO DO
        ArrayList<String> words = new ArrayList<String>();
        String word = keyboard.next();
        if(word.equals("/quit")) { //if quit return an empty ArrayList
            return words;
        }
        else {
            words.add(word); //adding first word
            word = keyboard.next();
            words.add(word); //adding second word
        }
        return words;
    }

    public static ArrayList<String> getWordLadderDFS(String start, String end) {
        Stack<Node> stack = new Stack<Node>();
        ArrayList<String> wl = new ArrayList<String>();
        Set<String> seen = new HashSet<String>();           // create a new Queue, a Word Ladder array,
        Set<String> dict = makeDictionary();                // a set of seen words, and set the dictionary
        int rank = 0;

        Node head = new Node(start);
        stack.push(head);                                        // begin with the first node (the start string)
        seen.add(head.word);

        DFS(head, end, seen, dict, stack, wl);
        //System.out.println("Path not Found");             // simple alert to how the function finished
        if (wl.isEmpty()) {
            wl.add(start);                              // if no path was found, just return the start and end
            wl.add(end);
        }
        return wl;
    }

    public static ArrayList<String> DFS(Node head, String end, Set<String> seen, Set<String> dict, Stack<Node> stack, ArrayList<String> wl) {
        head = stack.pop();                              // process the first item in the Q
        dict.remove(head.word.toUpperCase());
        if (head.word.equals(end)) {
            //System.out.println("Path Found");         // simple alert to how the function finished
            while (head != null) {
                wl.add(head.word);
                head = head.parent;                     // if found the end word, reverse through your traversal
            }                                           // to generate the Word Ladder then reverse it
            Collections.reverse(wl);
            return wl;
        }
        head.adjacent = getAdjacent(head.word, dict);   // set all of the adjacent nodes for your given node
        for (int i = end.length(); i >= 0; i--) {
            for (Node n : head.adjacent) {                  // for every Node that is adjacent, process it
                if (!seen.contains(n.word) && diff(n.word, end) == i) {
                    seen.add(n.word);                       // if it has not already been seen, add it to the queue
                    n.parent = head;                        // and set its parent node
                    stack.push(n);
                }
            }
        }
        DFS(head, end, seen, dict, stack, wl);
        return wl;
    }


    public static ArrayList<String> getWordLadderBFS(String start, String end) {
        Queue<Node> Q = new LinkedList<Node>();
        ArrayList<String> wl = new ArrayList<String>();
        Set<String> seen = new HashSet<String>();           // create a new Queue, a Word Ladder array,
        Set<String> dict = makeDictionary();                // a set of seen words, and set the dictionary

        Node head = new Node(start);
        Q.add(head);                                        // begin with the first node (the start string)
        seen.add(head.word);

        while (!Q.isEmpty()) {                              // loop through while Q is not empty
            head = Q.remove();                              // process the first item in the Q
            dict.remove(head.word.toUpperCase());
            if (head.word.equals(end)) {
                //System.out.println("Path Found");         // simple alert to how the function finished
                while (head != null) {
                    wl.add(head.word);
                    head = head.parent;                     // if found the end word, reverse through your traversal
                }                                           // to generate the Word Ladder then reverse it
                Collections.reverse(wl);
                return wl;
            }
            head.adjacent = getAdjacent(head.word, dict);   // set all of the adjacent nodes for your given node
            for (Node n : head.adjacent) {                  // for every Node that is adjacent, process it
                if (!seen.contains(n.word)) {
                    seen.add(n.word);                       // if it has not already been seen, add it to the queue
                    n.parent = head;                        // and set its parent node
                    Q.add(n);
                }
            }
        }
        //System.out.println("Path not Found");             // simple alert to how the function finished
        wl.add(start);
        wl.add(end);                                        // if no path was found, just return the start and end
        return wl;
    }


    public static void printLadder(ArrayList<String> ladder) {

    }
    // TODO
    // Other private static methods here

    public static boolean diffbyone(String str1, String str2) {
        int diff = 0;
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diff++;
            }
        }
        return diff==1;
    }

    public static int diff(String str1, String str2) {
        int diff = 0;
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

    public static ArrayList<Node> getAdjacent(String s, Set<String> dict){
        ArrayList<Node> n = new ArrayList<Node>();
        StringBuilder wl = new StringBuilder(s);

        for(int x = 0; x < s.length(); x++){
            for(char y = 'a'; y <= 'z'; y++){
                wl.setCharAt(x, y);
                String temp = wl.toString();
                //Only adds words within the dictionary

                if(dict.contains(temp.toUpperCase()) && diffbyone(s, temp)){
                    Node neigh = new Node(temp);
                    n.add(neigh);
                    dict.remove(temp.toUpperCase());
                }
                wl = new StringBuilder(s);
            }
        }

        return n;
    }

    /* Do not modify makeDictionary */
    public static Set<String>  makeDictionary () {
        Set<String> words = new HashSet<String>();
        Scanner infile = null;
        try {
            infile = new Scanner (new File("five_letter_words.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary File not Found!");
            e.printStackTrace();
            System.exit(1);
        }
        while (infile.hasNext()) {
            words.add(infile.next().toUpperCase());
        }
        return words;
    }
}
