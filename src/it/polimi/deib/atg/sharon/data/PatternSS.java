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

package it.polimi.deib.atg.sharon.data;

import java.util.ArrayList;

public class PatternSS {

    private int id;
    private ArrayList<Integer> ssIds;
    private ArrayList<Float> initialProb;
    private Float[][] probMatrix;

    public PatternSS(Integer id, ArrayList<Integer> ssIds, ArrayList<Float> initialProb, Float[][] probMatrix) {
        super();
        this.id = id;
        this.initialProb = initialProb;
        this.probMatrix = probMatrix;
        this.ssIds = ssIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float[][] getProbMatrix() {
        return probMatrix;
    }

    public void setProbMatrix(Float[][] probMatrix) {
        this.probMatrix = probMatrix;
    }

	public ArrayList<Integer> getSsIds() {
		return ssIds;
	}

	public void setSsIds(ArrayList<Integer> ssIds) {
		this.ssIds = ssIds;
	}

	public ArrayList<Float> getInitialProb() {
		return initialProb;
	}

	public void setInitialProb(ArrayList<Float> initialProb) {
		this.initialProb = initialProb;
	}
    
	public Integer getInitialSSId(){
		float rnd=(float) Math.random();
		
		float cumulativeProb=0;
		int position=0;
		for(Float p:initialProb){
			cumulativeProb+=p.floatValue();
			if(rnd<cumulativeProb){
				return this.getSsIds().get(position);
			}
			position++;
		}
		return this.getSsIds().get(this.getSsIds().size());
	}
	
	public Integer getDifferentSS(Integer actualSS){
		//TODO choose a SS different from the previous SS using transition prob
		return null;
	}
	
	public Integer getNextSS(Integer actualSS){
		//TODO choose a SS using transition prob
		return null;
	}
}
