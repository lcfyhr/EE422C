/*
 * EE422C Final Project submission by
 * Lars Fyhr
 * lcf597
 * 16195
 * Fall 2020
 * Slip Days Used: 3/3
 */

package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private String cname;
    private Socket clientsocket;
    DataInputStream in;
    DataOutputStream out;
    ArrayList<Item> id;

    public ClientHandler (String name, Socket s, ArrayList<Item> itemDirectory) {
        String cpass = "";
        cname = name;
        clientsocket = s;
        id = itemDirectory;
        //Generate a communication method between the Client and Server
        try {
            in = new DataInputStream(new BufferedInputStream(clientsocket.getInputStream()));
            cname = in.readUTF();
            cpass = in.readUTF();
            out = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {}
        System.out.println("Created client: " + cname + " : " + cpass + " @ " + clientsocket);
    }
    @Override
    public void run() {
        //Set up constant dual communication via connection method
        try {
            out.writeUTF(id.toString());
            while (true) {
                String input = in.readUTF();
                if (input.contains("=")) {
                    String [] parts = input.split("=");
                    for (Item i : id) {
                        if (i.iname.equals(parts[0])) {
                            i.setCurrprice(parts[2], Double.parseDouble(parts[1]));
                            out.writeUTF(id.toString());
                        }
                    }
                } else if (input.contains("update")) {
                    out.writeUTF(id.toString());
                }
            }
        } catch (IOException e) {}
    }
}
