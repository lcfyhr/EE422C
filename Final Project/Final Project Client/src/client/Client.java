/*
 * EE422C Final Project submission by
 * Lars Fyhr
 * lcf597
 * 16195
 * Fall 2020
 * Slip Days Used: 3/3
 */

package client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Client {

    String name = "";
    String pass = "";
    Socket sock;
    DataOutputStream out;
    DataInputStream in;
    Boolean connected;

    public Client (String address, int port, String inname, String inpass) {
        //Try and connect the client to the server, if failed, return error and connected is false
        try {
            sock = new Socket(address, port);
            System.out.println("Connected");
            out = new DataOutputStream(sock.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
            //Send across the username and password
            name = inname;
            pass = inpass;
            out.writeUTF(name);
            out.writeUTF(pass);
            System.out.println("Signed in as " + name + " : " + pass);
            connected = true;
        } catch (UnknownHostException u) {
            System.out.println("Unknown Host");
            connected = false;
        } catch (IOException e) {
            System.out.println("Bad Host");
            connected = false;
        }
    }

    //Get the block text of items from the server
    public String getItems() {
        String s = "";
        try {
            s = in.readUTF();
        } catch (IOException e) {}
        return s;
    }

    //Write a command to the server
    public void writeCommand(String command) {
        try {
            out.writeUTF(command);
        } catch (IOException e) {}
    }

    //disconnect
    public void dc() {
        try {
            sock.close();
            System.out.println("Disconnected");
        } catch(IOException i) {
            System.out.println("Client Failed Closing");
        }
    }

}
