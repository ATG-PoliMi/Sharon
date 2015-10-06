package it.polimi.deib.atg.sharon.utils;

import it.polimi.deib.atg.sharon.data.Coordinate;

/**
 * Created by fabio on 10/6/15.
 */
public interface PathEngine {

    abstract void setMap(int[][] map);

    abstract void setStart(Coordinate start);

    abstract void computePath(Coordinate target);


}
