package it.polimi.deib.atg.sharon.utils;

import it.polimi.deib.atg.sharon.data.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Collections.reverse;

/**
 * Created by fabio on 10/6/15.
 */
public abstract class PathEngine {

    protected int[][] map;
    protected int spacing;
    protected HashMap<String, ArrayList<Coordinate>> internalCache;

    public PathEngine(int[][] map, int spacing) {
        this.map = map.clone();
        this.spacing = spacing;
    }

    ;

    protected String keyfy(Coordinate start, Coordinate end) {
        return start.toString() + end.toString();
    }

    ;

    public ArrayList<Coordinate> computePath(Coordinate start, Coordinate target) {
        String key = keyfy(start, target);
        if (internalCache.containsKey(key)) {
            return internalCache.get(key);
        } else {
            if (internalCache.containsKey(keyfy(target, start))) {
                ArrayList<Coordinate> path = internalCache.get(keyfy(target, start));
                reverse(path);
                return path;
            }
            ArrayList<Coordinate> path = innerComputePath(start, target);
            internalCache.put(key, path);
            return path;
        }
    }

    protected abstract ArrayList<Coordinate> innerComputePath(Coordinate start, Coordinate target);

}
