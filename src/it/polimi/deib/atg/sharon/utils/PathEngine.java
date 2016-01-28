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
        internalCache = new HashMap<>();
    }

    protected String keyfy(Coordinate start, Coordinate end) {
        return start.toString() + ":" + end.toString();
    }


    public ArrayList<Coordinate> computePath(Coordinate start, Coordinate target) {
        String key = keyfy(start, target);
        if (internalCache.containsKey(key)) {
            return (ArrayList<Coordinate>) internalCache.get(key).clone();
        } else {
            if (internalCache.containsKey(keyfy(target, start))) {
                ArrayList<Coordinate> path = (ArrayList<Coordinate>) internalCache.get(keyfy(target, start)).clone();
                reverse(path);
                return path;
            }
            ArrayList<Coordinate> path = innerComputePath(start, target);
            internalCache.put(key, (ArrayList<Coordinate>) path.clone());
            return path;
        }
    }

    protected abstract ArrayList<Coordinate> innerComputePath(Coordinate start, Coordinate target);

}
