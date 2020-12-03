/*
 * EE422C Final Project submission by
 * Lars Fyhr
 * lcf597
 * 16195
 * Fall 2020
 * Slip Days Used: 3/3
 */

package server;

import java.util.*;

public class Item {
    String iname;
    double currprice;
    double minbid;
    int numbids;
    double buynow;
    String timeleft;
    String description;
    Timer timer;
    TimerTask tasknew;
    String topbidder;


    public Item(String s) {
        String[] parts = s.split("/");
        iname = parts[0];
        currprice = Double.parseDouble(parts[1]);
        minbid = Double.parseDouble(parts[2]);
        numbids = Integer.parseInt(parts[3]);
        buynow = Double.parseDouble(parts[4]);
        timeleft = parts[5];
        description = parts[6];
        topbidder = "No bids";

        //Generate the countdown clock
        tasknew = new TimerTask() {
            @Override
            public void run() {
                incTimeLeft();
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(tasknew, 0 ,1000);
    }

    //Format the object to a string for transmitting purposes
    public String toString() {
        return iname + "/" + currprice + "/" + minbid + "/" + numbids + "/" + buynow + "/" + timeleft + "/"
                + description + "/" + topbidder;
    }

    //Set the current bid price of an item based on the bidder and their bid amount
    public synchronized void setCurrprice(String bidder, double newbid) {
        if (newbid >= minbid && newbid > currprice && newbid < buynow) {
            topbidder = bidder;
            numbids++;
            currprice = newbid;
        } else if (newbid >= buynow && !timeleft.equals("EXPIRED")) {
            numbids++;
            currprice = buynow;
            expirelisting(bidder);
        }
    }

    //Expire a listing and stop clock thread
    public synchronized void expirelisting(String client) {
        topbidder = client;
        timeleft = "EXPIRED";
        timer.cancel();
        timer.purge();
    }

    //Make the countdown clock translate to nice format
    public void incTimeLeft() {
        int mins = Integer.parseInt(timeleft.split(":")[0]);
        int sec = Integer.parseInt(timeleft.split(":")[1]);
        if (sec > 0) {
            sec--;
        } else if (sec == 0) {
            mins--;
            sec = 59;
        }
        String seconds = "";
        if (sec < 10) {
            seconds = "0" + sec;
        } else {
            seconds = "" + sec;
        }
        timeleft = mins + ":" + seconds;
        if (mins < 0) {
            expirelisting(topbidder);
        }
    }

}
