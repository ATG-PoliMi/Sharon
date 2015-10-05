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

import it.polimi.deib.atg.sharon.configs.LowLevelADLDB;
import it.polimi.deib.atg.sharon.configs.SensorsetManager;
import it.polimi.deib.atg.sharon.data.PatternSS;
import it.polimi.deib.atg.sharon.data.Sensorset;

import java.io.IOException;
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

//	public Integer getPatternIDSS(Sensorset currentSS) {
//		
//		//choose the next pattern according to the last SS
//		
//		//for every Pattern retrieve the max probability
//		Float absoluteMax=(float) 0;
//		Float[] maxProbForPattern=new Float[LowLevelADLDB.getInstance().getPatternSSs().size()];
//		for(int posPattern=0; posPattern<LowLevelADLDB.getInstance().getPatternSSs().size();posPattern++){
//			PatternSS patternSS=LowLevelADLDB.getInstance().getPatternSSs().get(posPattern);
//			Float maxValue=(float) 0;
//			for(int posSS=0;posSS<patternSS.getSsIds().size();posSS++){
//				//within each SS of the pattern I should find the one with highest prob
//				Float probCurrent=(float) 0.0;
//				try {
//					probCurrent=SensorsetManager.getInstance().getTransitionProb()[currentSS.getIdSensorset()-1][patternSS.getSsIds().get(posSS)-1]; 
//					//-1 because the id of the sensorsets are ordered starting from 1 but the position in the matrix starts from zero
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				if(probCurrent>=maxValue){
//					maxValue=probCurrent;
//				}
//			}
//			maxProbForPattern[posPattern]=maxValue;
//			if(maxValue>=absoluteMax){
//				absoluteMax=maxValue;
//			}
//		}
//		
//		//retrieve the list of the pattern with highest probability (can be more than one with same prob)
//		ArrayList<Integer> idToConsider=new ArrayList<Integer>();
//		ArrayList<Float> initialProb=new ArrayList<Float>();
//		Float maxPrioriProb=(float) 0;
//		for(int pos=0;pos<maxProbForPattern.length;pos++){
//			if(maxProbForPattern[pos]>=absoluteMax){
//				PatternSS pss=LowLevelADLDB.getInstance().getPatternSSs().get(pos);
//				idToConsider.add(pss.getId());
//				initialProb.add(pss.getProb());
//				maxPrioriProb+=pss.getProb();
//			}
//		}
//		
//	
//		//choose pseudoRandomly among the selected one according to the a priori probability
//		Integer selectedPatternId=0;
//		float rnd=(float) Math.random()*maxPrioriProb;	
//		float cumulativeProb=0;
//		int position=0;
//		for(Float p:initialProb){
//			cumulativeProb+=p.floatValue();
//			if(rnd<=cumulativeProb){
//				selectedPatternId= idToConsider.get(position);
//			}
//			position++;
//		}
//		
//		return selectedPatternId;
//	}
}
