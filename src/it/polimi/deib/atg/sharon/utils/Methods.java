package it.polimi.deib.atg.sharon.utils;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

/**
 * Created by fabio on 9/23/15.
 */
public class Methods {

    public static int searchInPoint(ArrayList<Double> list, double a) {
        int c = 0;
        while (list.get(0) < a) {
            c++;
        }
        return c;
    }

    public static int[] onesArray(int len) {
        int[] array = new int[len];
        for (int k = 0; k < len; k++) array[k] = 1;
        return array;
    }

    public static boolean[] truesArray(int len) {
        boolean[] array = new boolean[len];
        for (int k = 0; k < len; k++) array[k] = true;
        return array;
    }

    public static double geoDist(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return sqrt(dx * dx + dy * dy);
    }

    public static double geoDist(int[] p1, int[] p2) {
        return geoDist(p1[0], p1[1], p2[0], p2[1]);
    }
}
