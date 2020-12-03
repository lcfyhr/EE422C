/*
 * EE422C Final Project submission by
 * Lars Fyhr
 * lcf597
 * 16195
 * Fall 2020
 * Slip Days Used: 3/3
 */

package client;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ClientItem extends GridPane {
    String iname;
    double currprice;
    double minbid;
    int numbids;
    double buynow;
    String endtime;
    String description;
    String topbidder;
    GridPane ip = new GridPane();
    Label lcurrPrice;
    Label lnumbids;
    Label lendtime;
    Label ltopbid;
    Boolean notsold;


    //Initialization constructor for a client-item
    public ClientItem (String s) {
        String[] parts = s.split("/");
        iname = parts[0];
        currprice = Double.parseDouble(parts[1]);
        minbid = Double.parseDouble(parts[2]);
        numbids = Integer.parseInt(parts[3]);
        buynow = Double.parseDouble(parts[4]);
        endtime = parts[5];
        description = parts[6];
        topbidder = parts[7];
        if (currprice == buynow) {
            notsold = false;
        } else {
            notsold = true;
        }

    }

    //Update info for the item
    public void updateinfo(double newprice, int newbids, String newendtime, String newtopbid) {
        this.currprice = newprice;
        this.numbids = newbids;
        this.endtime = newendtime;
        this.topbidder = newtopbid;
    }

    //Update the item's pane
    public void updateGP(MediaPlayer mp) {
        lcurrPrice.setText("Current Bid: $" + String.format("%.2f", this.currprice));
        lnumbids.setText("Bids: " + this.numbids);
        lendtime.setText("Time Left: " + this.endtime);
        ltopbid.setText("Top Bid: " + this.topbidder);

        //Updates the items pane to an expired item pane (either bought or not)
        if (this.endtime.equals("EXPIRED")) {
            ip.getChildren().clear();
            Label name = new Label(this.iname);
            name.setFont(Font.font("Andale Mono", FontWeight.BOLD, 20));
            Label desc = new Label(this.description);
            Label sold = new Label();
            if (!topbidder.equals("No bids")) {
                sold.setText("SOLD to " + topbidder + " for $" + String.format("%.2f", this.currprice));
                sold.setTextFill(Color.DARKGREEN);
                if (notsold) {
                    mp.seek(Duration.ZERO);
                    mp.setStartTime(Duration.ZERO);
                    mp.play();
                    notsold = false;
                }
            } else {
                sold.setText("Listing has expired!");
                sold.setTextFill(Color.RED);
            }
            sold.setFont(Font.font("Arial", FontWeight.BOLD, 30));

            //re-add children and center them
            ip.add(name,2,0);
            ip.add(desc,2,1);
            ip.add(sold,2,3);
            for (Node n : ip.getChildren()) {
                if (GridPane.getColumnIndex(n) == 2) {
                    GridPane.setHalignment(n, HPos.CENTER);
                }
            }
        }
    }

    public void attachGP(Client c) {
        ip.setPadding(new Insets(10, 10, 10, 10));

        //Create the different objects to lay out on each item's section
        Label name = new Label(this.iname);
        Label desc = new Label(this.description);
        name.setFont(Font.font("Andale Mono", FontWeight.BOLD, 20));
        lcurrPrice = new Label("Current Bid: $" + String.format("%.2f", this.currprice));
        lnumbids = new Label("Bids: " + this.numbids);
        lendtime = new Label("Time Left: " + this.endtime);
        Label minbid = new Label("Starting Bid: $" + String.format("%.2f", this.minbid));
        ltopbid = new Label("Top Bid: " + this.topbidder);
        TextField bidamt = new TextField();
        Text biderr = new Text("");

        //Buy Now Button: will send the server the max bid to buy now
        Button buyNow = new Button("Buy Now: $" + String.format("%.2f", this.buynow));
        buyNow.setOnAction(e -> {
            if (!this.endtime.equals("EXPIRED")) {
                c.writeCommand(this.iname + "=" + this.buynow + "=" + c.name);
            }
        });

        //Bid Button: will either send in a correct bid or return an error message
        Button bid = new Button("Bid");
        bid.setOnAction(e -> {
            double bidded = 0;
            boolean isInt = true;
            if (!bidamt.getText().equals("") && !this.endtime.equals("EXPIRED")) {
                try {
                    bidded = Double.parseDouble(bidamt.getText());
                } catch (NumberFormatException nfe) {
                    isInt = false;
                    biderr.setText("Your last bid was not valid!");
                    biderr.setFill(Color.RED);
                }
                if (isInt && bidded > this.currprice) {
                    c.writeCommand(this.iname + "=" + bidded + "=" + c.name);
                    biderr.setText("Your last bid was $" + String.format("%.2f", bidded));
                    biderr.setFill(Color.DARKGREEN);
                } else if (isInt) {
                    biderr.setText("Your last bid was not high enough!");
                    biderr.setFill(Color.RED);
                }
            }
        });

        //Set Column and Row Constraints for formatting
        ip.getColumnConstraints().add(new ColumnConstraints(25));
        ip.getColumnConstraints().add(new ColumnConstraints(175));
        ip.getColumnConstraints().add(new ColumnConstraints(400));
        ip.getColumnConstraints().add(new ColumnConstraints(175));
        ip.getColumnConstraints().add(new ColumnConstraints(25));
        ip.getRowConstraints().add(new RowConstraints(30));
        ip.getRowConstraints().add(new RowConstraints(30));
        ip.getRowConstraints().add(new RowConstraints(30));
        ip.getRowConstraints().add(new RowConstraints(30));
        ip.getRowConstraints().add(new RowConstraints(30));
        ip.getRowConstraints().add(new RowConstraints(30));

        //Add all the children to the pane in the right format
        ip.add(name,2,0);
        ip.add(desc,2,1);
        ip.add(lcurrPrice,1,2);
        ip.add(lnumbids, 1,3);
        ip.add(ltopbid, 1,4);
        ip.add(minbid,2,2);
        ip.add(buyNow,3,2);
        ip.add(lendtime,2,4);
        ip.add(bid,3,3);
        ip.add(bidamt, 2, 3);
        ip.add(biderr,2, 5);

        //For each node in the pane, make sure its centered
        for (Node n : ip.getChildren()) {
            if (GridPane.getColumnIndex(n) == 2) {
                GridPane.setHalignment(n, HPos.CENTER);
            }
        }

        //Create a border outline for the items
        ip.setStyle("-fx-border-color: black; -fx-border-width: 0 1 1 1");
    }

}
