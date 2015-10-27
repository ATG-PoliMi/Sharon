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

import it.polimi.deib.atg.sharon.configs.ParamsManager;
import it.polimi.deib.atg.sharon.configs.SensorsetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternSS {

    private int id;
    private Integer idAct;
    private String name;
    private String nameAct;
    private Float prob;
    private ArrayList<Integer> ssIds;
    private ArrayList<Float> initialProb;
    private Float[][] probMatrix;

    public PatternSS(Integer id,Integer idAct,String name,String nameAct, Float prob, ArrayList<Integer> ssIds, ArrayList<Float> initialProb, Float[][] probMatrix) {
        super();
        this.id = id;
        this.idAct=idAct;
        this.name=name;
        this.nameAct=nameAct;
        this.prob=prob;
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
	
	private Integer pseudoRandomchoice(Random rndmn,ArrayList<Integer> ids,ArrayList<Float> probs) {
		try {
			if (ids.size() > 0) {
				Float maxProb = (float) 0;
				for (Float p : probs) {
					maxProb += p;
				}
				float rnd = (float) rndmn.nextDouble() * maxProb;
				float cumulativeProb = 0;
				int position = 0;
				for (Float p : probs) {
					cumulativeProb += p.floatValue();
					if (rnd < cumulativeProb) {
						return ids.get(position);
					}
					position++;
				}
				return ids.get(ids.size() - 1);
			} else {

				//throw new Exception("The choice cannot be performed on empty list");
				//we are forcing a sensorset which is the only one in the pattern to stay more than 
				//its maximum time to complete the pattern time
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer getInitialSSIdAPriori(){
		return this.pseudoRandomchoice(new Random(),this.getSsIds(),this.initialProb);
	}
	
	public Integer getInitialSSIdAPrioriAndTransitionMatrix(Integer previousSSId){	
		Float maxValue=(float) 0;
		ArrayList<Integer> maxSSid=new ArrayList<Integer>();
		ArrayList<Float> maxSSprob=new ArrayList<Float>();
		
		for(int posSS=0;posSS<this.ssIds.size();posSS++){
			//within each SS of the pattern I should find the one with highest prob
			Float probCurrent=(float) 0.0;
			try {
				probCurrent=SensorsetManager.getInstance().getTransitionProb()[previousSSId-1][this.getSsIds().get(posSS)-1];
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(probCurrent.equals(maxValue)){
				maxSSid.add(this.getSsIds().get(posSS));
				maxSSprob.add(this.getInitialProb().get(posSS));
			}
			if(probCurrent>maxValue){	
				maxValue=probCurrent;
				maxSSid=new ArrayList<Integer>();
				maxSSprob=new ArrayList<Float>();
				maxSSid.add(this.getSsIds().get(posSS));
				maxSSprob.add(this.getInitialProb().get(posSS));
			}
		}
		//pseudo Random choice of the SS with highest transition probability according to their apriori initial prob
		return this.pseudoRandomchoice(new Random(),maxSSid, maxSSprob);
	}
	
	public Integer getPosOfIdInList(ArrayList<Integer> l,Integer id){
		int pos=0;
		for(Integer num:l){
			if (num.equals(id)){
				return pos;
			}
			pos++;
		}
		return null;
	}
	
	
	
	public Integer getNextSS(Random rndm,Integer actualSS){
		Float[][] pm=this.getProbMatrix();
		ArrayList<Integer> sSid=new ArrayList<Integer>();
		ArrayList<Float> sSprob=new ArrayList<Float>();
		for(Integer idss:this.getSsIds()){
			sSid.add(idss);
			try{
				sSprob.add(pm[getPosOfIdInList(this.getSsIds(),actualSS)][getPosOfIdInList(this.getSsIds(),idss)]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return this.pseudoRandomchoice(rndm,sSid, sSprob);
	}
	
	public Integer getNextSSRhythm(Random rndm,Integer ti,String pattName,Integer actualSS, Integer activityId, Integer actualSecond, Integer activityTotalduration){
		Float[][] pm=this.getProbMatrix();
		

		List<Float> listCoeff=ParamsManager.getInsatnce().getRhythmCoeffByIdAct(activityId);
		Integer N=listCoeff.size();
		Integer n=(int) ((Math.floor(((actualSecond*N)/activityTotalduration))+1) % N);
		
		Double xn= ((1/Math.sqrt(N))*(listCoeff.get(0))*(Math.cos(0)));
		for(Integer i=1;i<N;i++){
			xn+=(Math.sqrt((double) 2/N))*(listCoeff.get(i))*(Math.cos(((Math.PI)*((2*n)-1)*(i))/(2*N)));
		}
		
		Float pself=pm[getPosOfIdInList(this.getSsIds(),actualSS)][getPosOfIdInList(this.getSsIds(),actualSS)];
		Double kn=(1-(pself*xn))/(1-pself);
		
		ArrayList<Integer> sSid=new ArrayList<Integer>();
		ArrayList<Float> sSprob=new ArrayList<Float>();
		
		for(Integer idSS: this.getSsIds()){
			//System.out.print();
			//prob transition from actualSS to idSS
			Float pr=pm[getPosOfIdInList(this.getSsIds(),actualSS)][getPosOfIdInList(this.getSsIds(),idSS)];
			if(idSS.equals(actualSS)){
				//transition from actual to actual
				pr=(float) (pr*xn);			
			}else{
				//transition from actual to new SS
				pr=(float) (pr*kn);
			}
			
			sSid.add(idSS);
			sSprob.add(pr);
		}
		
		return this.pseudoRandomchoice(rndm,sSid, sSprob);
	}
	
	public Integer getNextSS(Random rndm,Integer ti,String pattName,Integer actualSS, Integer activityId, Integer actualSecond, Integer activityTotalduration){
		Float[][] pm=this.getProbMatrix();
		ArrayList<Integer> sSid=new ArrayList<Integer>();
		ArrayList<Float> sSprob=new ArrayList<Float>();
		
		for(Integer idSS: this.getSsIds()){
			//System.out.print();
			//prob transition from actualSS to idSS
			Float pr=pm[getPosOfIdInList(this.getSsIds(),actualSS)][getPosOfIdInList(this.getSsIds(),idSS)];
			if(idSS.equals(actualSS)){
				//transition from actual to actual
				pr=(float) 0;			
			}else{
				//transition from actual to new SS
				sSid.add(idSS);
				sSprob.add(pr);
			}	
		}
		
		return this.pseudoRandomchoice(rndm,sSid, sSprob);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getProb() {
		return prob;
	}

	public void setProb(Float prob) {
		this.prob = prob;
	}

	public Integer getIdAct() {
		return idAct;
	}

	public void setIdAct(Integer idAct) {
		this.idAct = idAct;
	}

	public String getNameAct() {
		return nameAct;
	}

	public void setNameAct(String nameAct) {
		this.nameAct = nameAct;
	}
	
}
