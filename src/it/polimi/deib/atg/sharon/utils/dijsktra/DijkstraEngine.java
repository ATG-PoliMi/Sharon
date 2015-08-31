/*
 *
 * SHARON - Human Activities Simulator
 * Author: ATG Group (http://atg.deib.polimi.it/)
 *
 * Copyright (C) 2015, Politecnico di Milano
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package it.polimi.deib.atg.sharon.utils.dijsktra;

import java.util.ArrayList;

public class DijkstraEngine {
	Graph graph;
	public String initial;

    private int[][] neig_offset = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}};
    //,{1,0},{-1,1},{0,1},{1,1}}; // it is ok to check the whole neighborhood,
    // but we can omit half of it and use symmetry

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
		graph.dijkstra(initial);
	}

	public DijkstraEngine() {
		graph = new Graph();
	}

	public void buildAdjacencyMatrix(int[][] map) {
		int row = map.length;
		int col = map[0].length;
        // On the border of the environment I always have walls, so I can skip these coordinates.
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (map[i][j] != 1) {
                    for (int[] aNeig_offset : neig_offset) {
                        int nx = i + aNeig_offset[0];
                        int ny = j + aNeig_offset[1];
                        if (nx < 0 || ny < 0 || nx >= row || ny >= col)
                            continue;
                        if (map[nx][ny] != 1) {
                            graph.addVertex(Integer.toString(i) + "," + Integer.toString(j),
                                    Integer.toString(nx) + "," + Integer.toString(ny), 1);
                            graph.addVertex(Integer.toString(nx) + "," + Integer.toString(ny),
                                    Integer.toString(i) + "," + Integer.toString(j), 1);
                        }
                    }
                }
			}
		}			

	}

	public ArrayList<String> computePath(String target) {
		Vertex tar = graph.getVertex(target);
		graph.printPath(tar); 
		return graph.getPath();		
	}
}