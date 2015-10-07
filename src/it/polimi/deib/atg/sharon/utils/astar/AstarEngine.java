package it.polimi.deib.atg.sharon.utils.astar;

import it.polimi.deib.atg.sharon.data.Coordinate;
import it.polimi.deib.atg.sharon.utils.PathEngine;

import java.util.ArrayList;
import java.util.Collections;

import static it.polimi.deib.atg.sharon.utils.Methods.*;
import static java.lang.Math.sqrt;

/**
 * Created by fabio on 10/6/15.
 */
public class AstarEngine extends PathEngine {

    public AstarEngine(int[][] map, int spacing) {
        super(map, spacing);
    }

    @Override
    protected ArrayList<Coordinate> innerComputePath(Coordinate start, Coordinate target) {
        ArrayList<Coordinate> path = new ArrayList<Coordinate>();
        ArrayList<int[]> fringe = new ArrayList<int[]>();
        ArrayList<Double> score = new ArrayList<Double>();

        int[] indxstart = new int[]{start.getX() / spacing, start.getY() / spacing};
        int[] indxend = new int[]{target.getX() / spacing, target.getY() / spacing};

        int sizex = map.length;
        int sizey = map[0].length;

        boolean[][] visited = new boolean[sizex][sizey];
        visited[indxstart[0]][indxstart[1]] = true;

        double[][] dist = new double[sizex][sizey];
        boolean found = false;

        if (indxstart[0] == indxend[0] && indxstart[1] == indxend[1]) {
            path.add(target);
            return path;
        }

        fringe.add(indxstart);
        score.add(geoDist(indxstart, indxend));
        while (!fringe.isEmpty()) {
            growfringe(fringe, score, dist, indxend, visited);
            if (fringe.size() > 0) {
                int[] last = fringe.get(0);
                if (last[0] == indxend[0] && last[1] == indxend[1]) {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            path = tracebackPath(dist, indxend, indxstart, visited);
            return path;
        }

        return null;
    }

    private ArrayList<Coordinate> tracebackPath(double[][] dist, int[] indxend, int[] indxstart, boolean[][] visited) {
        int[][] mask = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        ArrayList<int[]> indxPath = new ArrayList<int[]>();

        int[] curr = new int[2];

        indxPath.add(indxend);
        while (indxPath.get(0)[0] != indxstart[0] || indxPath.get(0)[1] != indxstart[1]) {
            curr = indxPath.get(0);
            boolean[] valid = truesArray(8);
            // Boundaries Check
            if (curr[0] <= 0) {
                for (int a : new int[]{0, 1, 2}) {
                    valid[a] = false;
                }
            }
            if (curr[0] >= (visited.length - 1)) {
                for (int a : new int[]{5, 6, 7}) {
                    valid[a] = false;
                }
            }
            if (curr[1] <= 0) {
                for (int a : new int[]{0, 3, 5}) {
                    valid[a] = false;
                }
            }
            if (curr[1] >= (visited[0].length - 1)) {
                for (int a : new int[]{2, 4, 7}) {
                    valid[a] = false;
                }
            }
            ArrayList<Double> tmp_score = new ArrayList<Double>();
            ArrayList<int[]> tmp_next = new ArrayList<int[]>();

            for (int k = 0; k < 8; k++) {
                if (valid[k]) {
                    int[] neib = new int[]{curr[0] + mask[k][0], curr[1] + mask[k][1]};
                    if (visited[neib[0]][neib[1]]) {
                        tmp_next.add(neib);
                        tmp_score.add(dist[neib[0]][neib[1]]);
                    }
                }
            }
            int minIndex = tmp_score.indexOf(Collections.min(tmp_score));
            indxPath.add(0, tmp_next.get(minIndex));
        }
        ArrayList<Coordinate> finalPath = indxToCoordinates(indxPath, spacing);
        return finalPath;
    }

    private ArrayList<Coordinate> indxToCoordinates(ArrayList<int[]> indxPath, int spacing) {
        ArrayList<Coordinate> finalPath = new ArrayList<>(indxPath.size());
        for (int k = 0; k < indxPath.size(); k++) {
            finalPath.add(new Coordinate(indxPath.get(k), spacing));
        }
        return finalPath;
    }

    private void growfringe(ArrayList<int[]> fringe, ArrayList<Double> scores, double[][] dist,
                            int[] indxend, boolean[][] visited) {
        int[][] mask = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        boolean[] valid = truesArray(8);

        int[] curr = fringe.get(0);
        fringe.remove(0);
        scores.remove(0);

        // Boundaries Check
        if (curr[0] <= 0) {
            for (int a : new int[]{0, 1, 2}) {
                valid[a] = false;
            }
        }
        if (curr[0] >= (visited.length - 1)) {
            for (int a : new int[]{5, 6, 7}) {
                valid[a] = false;
            }
        }
        if (curr[1] <= 0) {
            for (int a : new int[]{0, 3, 5}) {
                valid[a] = false;
            }
        }
        if (curr[1] >= (visited[0].length - 1)) {
            for (int a : new int[]{2, 4, 7}) {
                valid[a] = false;
            }
        }

        double neibdist, updist;


        for (int k = 0; k < 8; k++) {
            int[] neib = new int[]{curr[0] + mask[k][0], curr[1] + mask[k][1]};
            if (valid[k]) {
                if (visited[neib[0]][neib[1]]) {
                    if (mask[k][0] != 0 ^ mask[k][1] != 0) {
                        neibdist = 1;
                    } else {
                        neibdist = sqrt(2);
                    }
                    updist = dist[curr[0]][curr[1]] + neibdist;
                    if (dist[neib[0]][neib[1]] > updist) {
                        dist[neib[0]][neib[1]] = updist;
                    }
                }
            }
        }

        double dend;

        for (int k = 0; k < 8; k++) {
            int[] neib = new int[]{curr[0] + mask[k][0], curr[1] + mask[k][1]};
            if (valid[k]) {
                if (!(map[neib[0]][neib[1]] == 0 || visited[neib[0]][neib[1]])) {
                    if (mask[k][0] != 0 ^ mask[k][1] != 0) {
                        neibdist = 1;
                    } else {
                        neibdist = sqrt(2);
                    }
                    dist[neib[0]][neib[1]] = dist[curr[0]][curr[1]] + neibdist;
                    dend = geoDist(indxend, neib);
                    double neibscore = dend * dist[neib[0]][neib[1]];
                    int insertPoint = searchInPoint(scores, neibscore);
                    fringe.add(insertPoint, neib);
                    scores.add(insertPoint, neibscore);
                    visited[neib[0]][neib[1]] = true;
                }
            }
        }
    }

}