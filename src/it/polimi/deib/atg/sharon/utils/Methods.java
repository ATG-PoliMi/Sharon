package it.polimi.deib.atg.sharon.utils;

import static java.lang.Math.sqrt;

/**
 * Created by fabio on 9/23/15.
 */
public class Methods {

    public static double geoDist(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return sqrt(dx * dx + dy * dy);
    }
}
