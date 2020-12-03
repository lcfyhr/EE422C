/*
 * EE422C Final Project submission by
 * Lars Fyhr
 * lcf597
 * 16195
 * Fall 2020
 * Slip Days Used: 3/3
 */

package server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Observable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class Server extends Observable {

    public static void main(String[] args) {
        //Start the server
        Server s = new Server();
        s.runServer(5000);
    }

    public void runServer(int port) {
        try {
            //try and read in the item list to generate the items
            ArrayList<Item> id = new ArrayList<>();
            File file = new File("items.txt");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    id.add(new Item(line));
                }
            }
            //try and connect the server socket and set up to accept new clients
            ServerSocket ss = new ServerSocket(port);
            System.out.println(ss.getInetAddress());
            while (true) {
                Socket sock = ss.accept();
                Thread t = new Thread(new ClientHandler("", sock, id));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Server Failure");
        }
    }

}
