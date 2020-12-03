// SortTools.java
/*
 * EE422C Project 1 submission by
 * Replace <...> with your actual data.
 * <Student Name>
 * <Student EID>
 * <5-digit Unique No.>
 * Spring 2020
 * Slip days used:
 */
package assignment1;

import java.util.Arrays;

public class SortTools {
    /**
      * Return whether the first n elements of x are sorted in non-decreasing
      * order.
      * @param x is the array
      * @param n is the size of the input to be checked
      * @return true if array is sorted
      */
    public static boolean isSorted(int[] x, int n) {
        for (int i = 0; i < n - 1; i++) {
            if (x[i] > x[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return an index of value v within the first n elements of x.
     * @param x is the array
     * @param n is the size of the input to be checked
     * @param v is the value to be searched for
     * @return any index k such that k < n and x[k] == v, or -1 if no such k exists
     */
    public static int find(int[] x, int n, int v) {
        int right = n-1;
        int left = 0;
        while (right > left + 1) {
            if (x[(right + left)/2] > v) {
                right = (right + left)/2;
            } else if (x[(right + left)/2] <= v) {
                left = (right + left)/2;
            }
            if (x[right] == v) {
                return right;
            }
            if (x[left] == v) {
                return left;
            }
        }
        return -1;
    }

    /**
     * Return a sorted, newly created array containing the first n elements of x
     * and ensuring that v is in the new array.
     * @param x is the array
     * @param n is the number of elements to be copied from x
     * @param v is the value to be added to the new array if necessary
     * @return a new array containing the first n elements of x as well as v
     */
    public static int[] copyAndInsert(int[] x, int n, int v) {
        int[] y = new int[n + 1];
        boolean found = false;
        int offset = 0;
        for (int i = 0; i < n; i++) {
            if (!found && x[i] > v) {
                y[i] = v;
                offset = 1;
                found = true;
            }
            if (x[i] == v) {
                found = true;
            }
            y[i + offset] = x[i];
        }
        if (y[n] == 0) {
               y = Arrays.copyOf(y, n);
        }
        return y;
    }

    /**
     * Insert the value v in the first n elements of x if it is not already
     * there, ensuring those elements are still sorted.
     * @param x is the array
     * @param n is the number of elements in the array
     * @param v is the value to be added
     * @return n if v is already in x, otherwise returns n+1
     */
    public static int insertInPlace(int[] x, int n, int v) {
        boolean inserted = false;
        int swapval = 0;
        for (int i = 0; i < n; i++) {
            int currval = x[i];
            if (x[i] == v) {
                return n;
            }
            if (inserted) {
                x[i] = swapval;
                swapval = currval;
            }
            if (x[i] > v && !inserted) {
                swapval = x[i];
                x[i] = v;
                inserted = true;
            }
        }
        return n + 1;
    }

    /**
     * Sort the first n elements of x in-place in non-decreasing order using
     * insertion sort.
     * @param x is the array to be sorted
     * @param n is the number of elements of the array to be sorted
     */
    public static void insertSort(int[] x, int n) {
        int min = 0;
        int loc = 0;
        int swapval = 0;
        for (int i = 0; i < n; i++) {
            min = x[i];
            loc = i;
            for (int j = i; j < n; j++) {
                if (x[j] < min) {
                    min = x[j];
                    loc = j;
                }
            }
            swapval = x[i];
            x[i] = min;
            x[loc] = swapval;
        }
    }
}
