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

public class ADLMatch {
	private int HLadl;
	private ArrayList<Integer> LLadl = new ArrayList<Integer>();
	private ArrayList<Float> LLadlProbability = new ArrayList<Float>();
	
	
	public ADLMatch(int hLadl, ArrayList<Integer> lLadl,
                    ArrayList<Float> lLadlProbability) {
		super();
		HLadl = hLadl;
		LLadl = lLadl;
		LLadlProbability = lLadlProbability;
	}
	
	public Integer getPatternID(){
		float rnd=(float) Math.random();	
		float cumulativeProb=0;
		int position=0;
		for(Float p:LLadlProbability){
			cumulativeProb+=p.floatValue();
			if(rnd<cumulativeProb){
				return this.getLLadl().get(position);
			}
			position++;
		}
		return this.getLLadl().get(this.getLLadl().size()-1);
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


	public ArrayList<Float> getLLadlProbability() {
		return LLadlProbability;
	}


	public void setLLadlProbability(ArrayList<Float> lLadlProbability) {
		LLadlProbability = lLadlProbability;
	}

}
