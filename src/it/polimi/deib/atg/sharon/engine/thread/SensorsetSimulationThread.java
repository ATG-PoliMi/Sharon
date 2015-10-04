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

package it.polimi.deib.atg.sharon.engine.thread;

import it.polimi.deib.atg.sharon.Main;
import it.polimi.deib.atg.sharon.configs.HouseMap;
import it.polimi.deib.atg.sharon.configs.LowLevelADLDB;
import it.polimi.deib.atg.sharon.configs.SensorsetManager;
import it.polimi.deib.atg.sharon.data.Coordinate;
import it.polimi.deib.atg.sharon.data.Place;
import it.polimi.deib.atg.sharon.data.Sensor;
import it.polimi.deib.atg.sharon.data.Sensorset;
import it.polimi.deib.atg.sharon.engine.ADL;
import it.polimi.deib.atg.sharon.engine.LowLevelSSADL;
import it.polimi.deib.atg.sharon.utils.CumulateHistogram;
import it.polimi.deib.atg.sharon.utils.dijsktra.DijkstraEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class SensorsetSimulationThread implements Runnable {

	private Coordinate actor = new Coordinate(10, 10);
	private Coordinate Target = new Coordinate(15, 25);
	private DijkstraEngine DE;
	int[][] worldMapMatrix;
	private ArrayList<String> path = new ArrayList<String>();
	private String delims = ",";

	// ADL Handling
	Map<Integer, ADL> hLADL;
	LowLevelADLDB lLADL;
	// Map<Integer, ADLMatch> matchADL;
	HouseMap houseMap;

	// Sensorset Handling
	SensorsetManager ssManager;
	LowLevelSSADL currentPattern;
	Integer initialSS;
	Sensorset currentSS;
	Integer newSSId;
	Integer currentTimePattern;
	Integer currentTimeSS;
	Integer totalTimePattern;

	// User actions
	static int agentStatus = 1; // 1: extracting; 2: acting;
	static int idling = 0;

	// Utils
	static long timeInstant = 0;
	static long usedTime = 0;

	// TARGETS
	private Integer llADLIndex;
	private static int placesCounter = 0; // Place Counter

	// Support ADL
	static CumulateHistogram hist = new CumulateHistogram();

	private BlockingQueue<ADLQueue> queue;
	private int simulatedDays;
	private int action;
	private String simulationOutputPrefix;

	public SensorsetSimulationThread(BlockingQueue<ADLQueue> q,
			int simulatedDays, String sOutput) throws IOException {
		this.queue = q;
		this.simulatedDays = simulatedDays;
		this.simulationOutputPrefix = sOutput;

		houseMap = HouseMap.getInstance();
		lLADL = LowLevelADLDB.getInstance();
		this.ssManager = SensorsetManager.getInstance();
	}

	@Override
	public void run() {

		int emptyN = 0;
		PrintWriter out;
		try {
			Thread.sleep(1000);
			out = new PrintWriter(new FileWriter(simulationOutputPrefix
					+ "0.txt"));
			
			//initialization phase
			action=1;//sleep
			currentPattern=lLADL.getPatternSS(lLADL.getMatch(action).getPatternID());
			initialSS = currentPattern.getInitialSSIdAPriori();
			currentSS=SensorsetManager.getInstance().getSensorsetByID(initialSS);
			
			//loop for every second
			for (timeInstant = 0; timeInstant < (86400 * simulatedDays) - 5000; timeInstant++) {

				idling++;
				switch (agentStatus) {
				case 1: // Extracting + computing
					ADLQueue CADL;
					try {
						if (queue.isEmpty()) {
							System.out.println("***** A: EMPTY queue *****");
							timeInstant--;
							emptyN++;

						} else {
							CADL = queue.take();
							action = CADL.getADLId();
							totalTimePattern = (int) (long) CADL.getTime(); // TODO check the cast here... should be ok
							
							//choose the pattern according to the last sensorset and the probability
							llADLIndex = lLADL.getMatch(action).getPatternIDSS(currentSS); 
							
							// chosen pattern for the activity
							currentPattern = lLADL.getPatternSS(llADLIndex);

							// chosen ss to start the pattern
							initialSS = currentPattern.getInitialSSIdAPrioriAndTransitionMatrix(currentSS.getIdSensorset());
							currentSS = SensorsetManager.getInstance().getSensorsetByID(initialSS);

							// time counters set to zero
							currentTimeSS = 0;
							currentTimePattern = 0;

							agentStatus = 2;

						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("QUEUE ERROR!");
					}
					break;

				case 2: // Walking+Acting
					
					//TODO add here the algorithm to update the position
					
					currentTimeSS++;
					currentTimePattern++;
					if(currentTimePattern.equals(totalTimePattern)){
						//force to change the activity
						agentStatus=1;
					}else{
						if(currentTimeSS>currentSS.getMinTime()){
							//if the actual duration of this ss is > then its minimum is possible to change
							if(currentTimeSS>=currentSS.getMaxTime()){
								//force to change SS
								newSSId=currentPattern.getPatternSS().getDifferentSS(currentSS.getIdSensorset());
							}else{
								//compute using probability the next SS (can be the same)
								newSSId=currentPattern.getPatternSS().getNextSS(currentSS.getIdSensorset());
							}
							if(!newSSId.equals(currentSS.getIdSensorset())){
								currentTimeSS=0;
								currentSS=SensorsetManager.getInstance().getSensorsetByID(newSSId);
							}
						}
					}
					break;
				}

				if ((timeInstant % 86400 == 0) && (timeInstant > 0)) {
					out.close();
					out = new PrintWriter(new FileWriter(simulationOutputPrefix
							+ (int) timeInstant / 86400 + ".txt"));
				}
				out.println(printActiveSensors(action,currentSS));
				out.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void newTarget(int indexSensor) {
		DE = new DijkstraEngine();
		worldMapMatrix = HouseMap.getMap();
		DE.buildAdjacencyMatrix(worldMapMatrix);

		Sensor[] s = HouseMap.getS();

		Target = new Coordinate(s[indexSensor].getX(), s[indexSensor].getY());

		// Start point:
		DE.setInitial(actor.getX() + "," + actor.getY());

		// End point:
		path = DE.computePath(Target.getX() + "," + Target.getY());
		// System.out.println("PATH:"+path);

	}
	
	/**
	 * printActiveSensors computes the values for each sensor of the house and
	 * returns a String in the following format:
	 * "timeInstant, home area, ADL id, UserX, UserY, sensors 0-k"
	 * 
	 * @return
	 */
	public String printActiveSensors(int action,Sensorset currentSS) {

		String activeSensors = "";

		Sensor[] sensorsArray = HouseMap.getS();

		activeSensors += timeInstant;
		activeSensors += ", ";
		
		int sId=0;
		for (Sensor aSensorsArray : sensorsArray) {
			sId++;
			if(currentSS.getActivatedSensorsId().contains(sId)){
				activeSensors +="1, ";
			}else{
				activeSensors +="0, ";
			}	
		}

		// and ground truth
		activeSensors += action;
		activeSensors += ", ";
		activeSensors += (int) actor.getX() * (HouseMap.scale);
		activeSensors += ", ";
		activeSensors += (int) actor.getY() * (HouseMap.scale);

		return activeSensors;
	}
}