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

package it.polimi.deib.atg.sharon;

import it.polimi.deib.atg.sharon.configs.HighLevelADLDB;
import it.polimi.deib.atg.sharon.configs.NeedsDrift;
import it.polimi.deib.atg.sharon.configs.Parameters;
import it.polimi.deib.atg.sharon.engine.Needs;
import it.polimi.deib.atg.sharon.engine.thread.ADLQueue;
import it.polimi.deib.atg.sharon.engine.thread.ActivityImportationThread;
import it.polimi.deib.atg.sharon.engine.thread.ActivitySimulationThread;
import it.polimi.deib.atg.sharon.engine.thread.HMMSensorSimulationThread;
import it.polimi.deib.atg.sharon.engine.thread.SensorSimulationThread;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

	//*****SIMULATION PARAMETERS:*****
	private static int def_simulatedDays 	= 3; 	//Days to simulate
	//***** END PARAMETERS *****
	
	Parameters param = Parameters.getInstance();

	// Extra parameters - not to touch
	public static final boolean ENABLE_SENSORS_ACTIVITY = true;	//0: only High Level, 1: High Level + Low Level (Experimental!)
	public static final boolean PRINT_LOG 				= false;	//0: no log print, 1: print (histograms...)
	public static final boolean DISABLE_DIJKSTRA        = true;	//0: ENABLE_DIJKSTRA (slow), 1: DISABLE_DIJKSTRA
	public static final boolean USE_DRIFTS				= false;		// activates drifts
    public static final boolean USE_HMM_LL = false;// activates LowLevel Based on HMM
	public static final boolean GENERATE_HL_SCHEDULUNG	= false; //true to generate, false to import from file

	private static String sensorOutputPrefix = "data/SensorOutput/DAY";	//this file is heavy. Open it from explorer.
	private static String activityOutputPrefix = "data/ActivityOutput/DAY";	//this file is heavy. Open it from explorer.
	//

	//Thread
	private static ActivitySimulationThread activitySimulationThread;
	private static ActivityImportationThread activityImportationThread;
	//private static SensorSimulationThread sensorSimulationThread;
	private static SensorsetSimulationThread sensorsetSimulationThread;
	private static BlockingQueue<ADLQueue> queue = new ArrayBlockingQueue<>(400); //ADL QUEUE

	public static void main(String[] args) {

		int simulatedDays;
		try{
			Needs.getInstance();
			if (USE_DRIFTS)
				Parameters.getInstance().setDrifts(NeedsDrift.loadNeedDrift());
			HighLevelADLDB.getInstance();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//HIGH LEVEL SIMULATION
		if (args.length > 0){
			try {
				simulatedDays = Integer.parseInt(args[0]);
			}catch (Exception e){
				System.out.println("Incoherent input, refer to README. Quitting.");
				return;
			}
		}else{
			simulatedDays = def_simulatedDays;
		}
//<<<<<<< HEAD
//		activitySimulationThread = new ActivitySimulationThread(queue, simulatedDays, activityOutputPrefix);
//		new Thread(activitySimulationThread).start();
//		System.out.println("Simulator correctly instantiated... Beginning the simulation");
//        Runnable sensorSimulationThread;
//        //LOW LEVEL SIMULATION
//        if (ENABLE_SENSORS_ACTIVITY) {
//            if (USE_HMM_LL)
//                try {
//                    sensorSimulationThread = new HMMSensorSimulationThread(queue, simulatedDays, sensorOutputPrefix);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            else
//                sensorSimulationThread = new SensorSimulationThread(queue, simulatedDays, sensorOutputPrefix);
//            new Thread(sensorSimulationThread).start();
//			System.out.println("LowLevel Starts");
//=======
		
		if(GENERATE_HL_SCHEDULUNG){
			//GENERATE ACTIVITY SCHEDULING
			activitySimulationThread = new ActivitySimulationThread(queue, simulatedDays, activityOutputPrefix);
			new Thread(activitySimulationThread).start();
			System.out.println("Simulator correctly instantiated... Beginning the simulation");
		}else{
			//IMPORT ACTIVITY SCHEDULING
			activityImportationThread = new ActivityImportationThread(queue,"data/ActivityInput/");
			simulatedDays=activityImportationThread.numberOfDays();
			new Thread(activityImportationThread).start();
			System.out.println("Simulator correctly instantiated... Beginning to import activities");
		}

		//LOW LEVEL SIMULATION
		if (ENABLE_SENSORS_ACTIVITY) {
            //Simulation by sensorset
            try{
                if(USE_HMM_LL) {
                    sensorsetSimulationThread = new SensorsetSimulationThread(queue, simulatedDays, sensorOutputPrefix);
                    new Thread(sensorsetSimulationThread).start();
                }else{
                    //Simulation by sensor
                    sensorSimulationThread = new SensorSimulationThread(queue, simulatedDays, sensorOutputPrefix);
                    new Thread(sensorSimulationThread).start();
                    System.out.println("Consumer Starts");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
		}			
	}
}