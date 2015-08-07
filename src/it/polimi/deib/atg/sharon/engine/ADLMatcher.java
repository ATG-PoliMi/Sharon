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

package it.polimi.deib.atg.sharon.engine;

import java.util.ArrayList;

public class ADLMatcher {
	private int HLadl;
	private ArrayList<Integer> LLadl = new ArrayList<Integer>();
	private ArrayList<Double> LLadlProbability = new ArrayList<Double>();
	
	
	public ADLMatcher(int hLadl, ArrayList<Integer> lLadl,
			ArrayList<Double> lLadlProbability) {
		super();
		HLadl = hLadl;
		LLadl = lLadl;
		LLadlProbability = lLadlProbability;
	}


	public int getHLadl() {
		return HLadl;
	}


	public void setHLadl(int hLadl) {
		HLadl = hLadl;
	}


	public ArrayList<Integer> getLLadl() {
		return LLadl;
	}


	public void setLLadl(ArrayList<Integer> lLadl) {
		LLadl = lLadl;
	}


	public ArrayList<Double> getLLadlProbability() {
		return LLadlProbability;
	}


	public void setLLadlProbability(ArrayList<Double> lLadlProbability) {
		LLadlProbability = lLadlProbability;
	}
}
