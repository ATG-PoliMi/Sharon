package it.polimi.deib.atg.sharon;

import it.polimi.deib.atg.sharon.configs.ParamsManager;
import it.polimi.deib.atg.sharon.configs.LowLevelADLDB;
import it.polimi.deib.atg.sharon.configs.SensorsetManager;
import it.polimi.deib.atg.sharon.data.PatternSS;
import it.polimi.deib.atg.sharon.data.Sensorset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.DoubleStream;

public class ProbActivityCheck {

	public static void main(String[] args) {
		/*try{
		LowLevelADLDB lLADL = LowLevelADLDB.getInstance();
		Integer activityId=1;
		Integer activityTotalduration=1200;
		PatternSS currentPattern = lLADL.getPatternSS(lLADL.getMatch(activityId).getPatternID());
		Integer actualSS = currentPattern.getInitialSSIdAPriori();
		Sensorset currentSS = SensorsetManager.getInstance().getSensorsetByID(actualSS);
		
		for (Integer actualSecond=1;actualSecond<=activityTotalduration;actualSecond++){
				Float[][] pm=currentPattern.getProbMatrix();
				
				Integer n=(int) Math.floor(((actualSecond*10)/activityTotalduration))+1;
				List<Float> listCoeff=ActivityManager.getInsatnce().getRhythmCoeffByIdAct(activityId);
				Integer N=listCoeff.size();
				
				Double xn= (1/Math.sqrt(N))*(listCoeff.get(0))*Math.cos(0);
				//System.out.println((1/Math.sqrt(N))+" "+listCoeff.get(0)+" "+Math.cos(0));
				for(Integer i=2;i<=N;i++){
					//System.out.println(Math.sqrt(2/N)+" "+listCoeff.get(i-1)+" "+Math.cos((Math.PI*((2*n)-1)*(i))/(2*N)));
					xn+=(Math.sqrt((double) 2/N)*(listCoeff.get(i-1))*(Math.cos((Math.PI*((2*n)-1)*(i))/(2*N))));
				}
				System.out.print("second: "+actualSecond+" - n:"+n+" - xn: "+xn+" ");
				Float pself=pm[currentPattern.getPosOfIdInList(currentPattern.getSsIds(),actualSS)][currentPattern.getPosOfIdInList(currentPattern.getSsIds(),actualSS)];
				Double kn=(1-(pself*xn))/(1-pself);
				
				ArrayList<Integer> sSid=new ArrayList<Integer>();
				ArrayList<Float> sSprob=new ArrayList<Float>();
				
				for(Integer idSS: currentPattern.getSsIds()){
					//prob transition from actualSS to idSS
					Float pr=pm[currentPattern.getPosOfIdInList(currentPattern.getSsIds(),actualSS)][currentPattern.getPosOfIdInList(currentPattern.getSsIds(),idSS)];
					if(idSS.equals(actualSS)){
						//transition from actual to actual
						pr=(float) (pr*xn);
						System.out.println("prob: "+pr);
					}else{
						//transition from actual to new SS
						pr=(float) (pr*kn);
					}
					sSid.add(idSS);
					sSprob.add(pr);		
				}
		}
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
	}

}
