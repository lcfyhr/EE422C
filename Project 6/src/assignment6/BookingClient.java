/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Lars Fyhr
 * LCF597
 * 16195
 * Slip days used: <0>
 * Fall 2020
 */
package assignment6;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.lang.Thread;
import assignment6.Theater.BoxOffice;

public class BookingClient {

    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public Map<String, Integer> office;
    public Theater th;

    public BookingClient(Map<String, Integer> office, Theater theater) {
        this.office = office;
        this.th = theater;
    }

    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
        List<Thread> threads = new ArrayList<Thread>();
        for (Map.Entry<String, Integer> entry : office.entrySet()) {
            BoxOffice BO = th.new BoxOffice(entry.getKey(), entry.getValue());
            Thread BoxThread = new Thread(BO);          // iterate through each entry in the Box Office map, and create
            BoxThread.start();                          // a thread for each, then start that thread
            threads.add(BoxThread);
        }
        if (threads.size() != 0) {
            return threads;
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Integer> clientTest = new HashMap<String, Integer>();

        clientTest.put("BX13", 200);
        clientTest.put("BX2", 220);
        clientTest.put("BX14", 200);
        clientTest.put("BX8", 200);
        clientTest.put("BX5", 200);
        clientTest.put("BX10", 200);
        clientTest.put("BX12", 220);
        clientTest.put("BX4", 200);
        clientTest.put("BX11", 200);
        clientTest.put("BX15", 200);
        clientTest.put("BX3", 200);
        clientTest.put("BX7", 220);
        clientTest.put("BX9", 200);
        clientTest.put("BX1", 200);
        clientTest.put("BX6", 200);

        Theater theaterTest = new Theater(20, 5, "Django");
        BookingClient bc = new BookingClient(clientTest, theaterTest);


        List<Thread> BOs = bc.simulate();

        //wait for all threads to finish separate execution
        for(Thread t : BOs) {
            try {
                t.join();
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        for (Map.Entry<String, Integer> entry : theaterTest.BOs.entrySet()) {
            //System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
