/* MULTITHREADING <Theater.java>
 * EE422C Project 6 submission by
 * Lars Fyhr
 * LCF597
 * 16195
 * Slip days used: <0>
 * Fall 2020
 */
package assignment6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Theater {

    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log = new SalesLogs(); // Field in Theater class.

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    public Seat bestAvailableSeat;
    public String show;
    public int numRows;
    public int seatsPerRow;
    public Integer clientID = 0;
    public boolean soldout = false;
    private static Semaphore mutex = new Semaphore(1);
    public Map<String, Integer> BOs = new HashMap<String, Integer>();

    public SalesLogs sl;

    /**
     * Represents a seat in the theater A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
    }

    // end of class Seat

    /**
     * Represents a paper ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;

        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol + showLine + eol + boxLine + eol + seatLine + eol + clientLine + eol + dashLine;

            return result;
        }
    }

    /**
     * SalesLogs are security wrappers around an ArrayList of Seats and one of
     * Tickets that cannot be altered, except for adding to them. getSeatLog returns
     * a copy of the internal ArrayList of Seats. getTicketLog returns a copy of the
     * internal ArrayList of Tickets.
     */
    static class SalesLogs {
        private ArrayList<Seat> seatLog;
        private ArrayList<Ticket> ticketLog;

        private SalesLogs() {
            seatLog = new ArrayList<Seat>();
            ticketLog = new ArrayList<Ticket>();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Seat> getSeatLog() {
            return (ArrayList<Seat>) seatLog.clone();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Ticket> getTicketLog() {
            return (ArrayList<Ticket>) ticketLog.clone();
        }

        public void addSeat(Seat s) { // call when seat is allocated
            seatLog.add(s);
        }

        public void addTicket(Ticket t) { // call when ticket is printed
            ticketLog.add(t);
        }

    } // end of class SeatLog


    /**
     * Represents a Box Office that will handle a certain amount of given waiting individuals
     */
    public class BoxOffice implements Runnable {
        private String BOID;
        private Integer waiting;
        private int sold;

        public BoxOffice (String BOID, Integer clientsinline) {
            this.BOID = BOID;
            this.waiting = clientsinline;
            this.sold = 0;
        }

        @Override
        public void run(){
            try {
                mutex.acquire();
                while (waiting > 0 && !soldout) {
                    BOs.put(BOID, sold);
                    synchronized (Theater.this) {
                        clientID++;
                        Seat s = bestAvailableSeat();       // find the best available seat
                        String tempBOID = BOID;
                        if (s == null) {
                            tempBOID = null;                // if the best seat is unavailable, set BOID to null
                        }
                        this.sold++;
                        mutex.release();
                        if (!soldout && printTicket(tempBOID, s, clientID) == null) {   // try to print a ticket unless
                            System.out.println("Sorry, we are sold out!");              // the seats are all full
                            soldout = true;
                            return;
                        }
                    }
                    waiting--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Theater(int numRows, int seatsPerRow, String show) {
        this.numRows = numRows;
        this.seatsPerRow = seatsPerRow;
        this.show = show;
        this.bestAvailableSeat = new Seat(0,1);
        this.sl = new SalesLogs();
    }

    /**
     * Calculates the best seat not yet reserved
     *
     * @return the best seat or null if theater is full
     */
    public synchronized Seat bestAvailableSeat() {
        if (bestAvailableSeat != null && bestAvailableSeat.getRowNum() < numRows) {
            Seat bestSeat = bestAvailableSeat;
            int newRow;
            int newSeat;
            if (bestSeat.getSeatNum() < seatsPerRow) {
                newSeat = bestSeat.getSeatNum() + 1;
                newRow = bestSeat.getRowNum();          // find best available seat, check if it needs to overflow to
            } else {                                    // a new row, or if it is through all the seats
                newSeat = 1;
                newRow = bestSeat.getRowNum() + 1;
            }
            bestAvailableSeat = new Seat(newRow, newSeat);
            sl.addSeat(bestSeat);
            return bestSeat;
        }
        return null;
    }

    /**
     * Prints a ticket to the console for the client after they reserve a seat.
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        if (boxOfficeId != null) {
            Ticket newTicket = new Ticket(show, boxOfficeId, seat, client);
            System.out.println(newTicket.toString());
            sl.addTicket(newTicket);
            try {
                Thread.sleep(getPrintDelay());              // sleep the ticket printing
            } catch (InterruptedException e) {
                return newTicket;
            }
            return newTicket;
        }
        return null;
    }

    /**
     * Lists all seats sold for this theater in order of purchase.
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        return sl.seatLog;
    }

    /**
     * Lists all tickets sold for this theater in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return sl.ticketLog;
    }
}
