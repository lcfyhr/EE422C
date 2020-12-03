package assignment3;

import java.util.ArrayList;

public class Node {
    String word;
    Node parent = null;
    ArrayList<Node> adjacent = new ArrayList<Node>();

    Node(String s){
        this.word = s;
    }
}
