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

import java.util.*;
public class Vertex implements Comparator<Vertex>{
	public String name;
	public LinkedList<Edge> adjacentD;
	public LinkedList<Vertex> adjacentT;
	public int distance;
	public Vertex previous;    //previous vertex on shortest path
	public boolean known;
	public int defaultDis = Integer.MAX_VALUE;
	public Vertex() {
		name = null;
		distance = defaultDis;
		previous = null;
		known = false;
	}
	public Vertex(String argName) {
		this.name = argName;
		adjacentD = new LinkedList<Edge>();
		adjacentT = new LinkedList<Vertex>();
		distance = defaultDis;
		previous = null;
		known = false;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int w) {
		distance = w;
	}
	public int compare(Vertex v1, Vertex v2) {
		int w1 = v1.getDistance();
		int w2 = v2.getDistance();
		if(w1 > w2)
			return 1;
		else if (w1 < w2)
			return -1;
		else
			return 0;
	}
}