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


package it.polimi.deib.atg.sharon.configs;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import it.polimi.deib.atg.sharon.engine.ADLEffect;
import it.polimi.deib.atg.sharon.engine.ADL;
import it.polimi.deib.atg.sharon.engine.Needs;
import it.polimi.deib.atg.sharon.utils.Time;

/**
 * This class contains:
 * <ul>
 * <li>The maps of all the ADL that the simulator can use.
 * <li>The list of all the drifts that have to appear to the ADL
 * </ul>
 * <p>
 * The first is stored in member @see {@link #adlmap} as a Map<Integer, ADL> and is accessible only with methods @see {@link HighLevelADLDB#getAdlmap()}
 * and @see {@link HighLevelADLDB#setAdlmap(Map)()}.
 * <p>
 * The second is stored in member @see {@link #actdrift} as an ArrayList of instances of @see HighLevelADLDrift, is accessible with method
 * @see {@link HighLevelADLDB#setAdlmap(Map)()}.
 * <p>
 * Both of them are loaded from confing files in the builder @see {@link #HighLevelADLDB()}.
 * <p>
 * It's a singleton class, its instance is contained in its member @see {@link #instance}, 
 * accessible only with methods @see {@link #getInstance()} and @see {@link #setInstance(HighLevelADLDB)}.
 * <p>
 * @author alessandro 
 */
public class HighLevelADLDB {

    /* Removed to avoid reference error generated by refactoring
    * It also contains the methods @see {@link #defaultADL()}, that is used in
    * @see {@link HLThread#HLThread(BlockingQueue<ADLQueue> q, int simulatedDays, int printLog, int mode)}to choose the starting ADL of simulator,
    * and the methods @see {@link #CreateMap(ArrayList)} and @see {@link #CreateTd(float)} that are used to create correctly @see {@link #adlmap}.
     */
	
	private static HighLevelADLDB instance = null;
	
	private Map<Integer, ADL> adlmap;
	
	private ArrayList<HighLevelADLDrift> actdrift;

    private static final String CONFIG_PATH="config";
    private static final String ADL_PRE="act";
	
	/**
	 * This builder is called from the method @see {@link #getInstance()}.
	 * <p>
	 * It uses to instance the member @see {@link #adlmap} the method @see {@link #CreateMap(ArrayList)} with the ArrayList of ADL 
	 * that the method @see {@link #loadAct()} has loaded from the config files.
	 * <p>
	 * It also instances the member @see {@link #actdrift} with the method @see {@link HighLevelADLDrift#loadActDrift()} that loads the drifts from config files.
	 */
	private HighLevelADLDB(){
		super();
		try {
			adlmap = (CreateMap(loadAct()));
			actdrift = (HighLevelADLDrift.loadActDrift());
		} catch (NotDirectoryException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Creates an HashMap of an ArrayList of ADL
	 * @param ADLList	The ADL to map
	 * @return	the map created
	 */
	private Map<Integer, ADL> CreateMap(ArrayList<ADL> ADLList){
		Map<Integer, ADL> Map = new HashMap<>();
		Iterator<ADL> ItrADL = ADLList.iterator();
		while(ItrADL.hasNext()){
			ADL cADL = ItrADL.next();
			Map.put(cADL.getId(), cADL);
		}
		return Map;
	}
	
	/**
	 * Loads the acts of the simulator from the configuration files. The template files would be ignored
	 * @return	An ArrayList of the ADL loaded
	 * @throws NotDirectoryException	If folder "config" doesn't exist
	 */
	private static ArrayList<ADL> loadAct()throws NotDirectoryException{
			
		ArrayList<ADL>result = new ArrayList<ADL>();
		File folder = new File(CONFIG_PATH);
		if(!folder.exists()){
			throw new NotDirectoryException(null);
		}
		
		BufferedReader reader = null;
		
		FilenameFilter ActFilter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.')>0){
					if(name.contains("_")){
						if(!name.equals("act_template.conf")){
							int lastIndexDot = name.lastIndexOf('.');
							int lastIndexUnSl = name.lastIndexOf('_');
							String filename = name.subSequence(0, lastIndexUnSl).toString();
							String extension = name.substring(lastIndexDot);
							if(filename.equals(ADL_PRE) && extension.equals(".conf")){
								return true;
							}
						}
					}
				}
				return false;
			}
		};
		ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(ActFilter)));
		if(fileList.isEmpty()){
			throw new NullPointerException();
		}
        for (File CurrentFile : fileList) {
            ArrayList<String> configLines = new ArrayList<String>();
            try {
                reader = new BufferedReader(new FileReader(CurrentFile));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    configLines.add(line);
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            int IdADL = 0;
            String NameADL = null;
            ArrayList<String> needs = new ArrayList<String>();
            ArrayList<ADLEffect> effects = new ArrayList<ADLEffect>();
            int timeMin = 0;
            double[] week = new double[7];
            double[] weather = new double[3];
            Float[] timeDependency = new Float[1440];
            ArrayList<Double> Weights = new ArrayList<Double>();

            Iterator<String> itrLines = configLines.iterator();
            String singleLine;
            String[] StringSplitted;

            StringSplitted = itrLines.next().split(",");
            IdADL = Integer.parseInt(StringSplitted[0]);
            NameADL = StringSplitted[1].toLowerCase();

            singleLine = itrLines.next();
            if (!singleLine.contains("Effects")) {
//	TODO			throw new
            }

            StringSplitted = itrLines.next().split(",");
            Iterator<String> ItrWeights = Arrays.asList(StringSplitted).iterator();
            while (ItrWeights.hasNext()) {
                Weights.add(Double.parseDouble(ItrWeights.next()));
            }
            for (int j = 0; j < Weights.size(); j++) {
                if (Weights.get(j) != 0.0) {
                    effects.add(new ADLEffect(Needs.getInstance().searchNamewIn(j), Weights.get(j)));
                }
            }

            singleLine = itrLines.next();
            if (!singleLine.contains("Weights")) {
//	TODO			throw new
            }

            StringSplitted = itrLines.next().split(",");
            Iterator<String> ItrNeeds = Arrays.asList(StringSplitted).iterator();
            while (ItrNeeds.hasNext()) {
                needs.add(ItrNeeds.next().toLowerCase());
            }

            singleLine = itrLines.next();
            if (!singleLine.contains("Weekdays")) {
//	TODO			throw new
            }

            StringSplitted = itrLines.next().split(",");
            for (int j = 0; j < 7; j++) {
                week[j] = Double.parseDouble(StringSplitted[j]);
            }

            singleLine = itrLines.next();
            if (!singleLine.contains("Weather")) {
//	TODO			throw new
            }

            StringSplitted = itrLines.next().split(",");
            for (int j = 0; j < 3; j++) {
                weather[j] = Double.parseDouble(StringSplitted[j]);
            }

            singleLine = itrLines.next();
            if (!singleLine.contains("Theta")) {
//	TODO			throw new
            }

            StringSplitted = itrLines.next().split(",");
            if (StringSplitted.length == 1) {
                timeDependency = CreateTd(Float.parseFloat(StringSplitted[0]));
            } else if (StringSplitted.length == 1440) {
                for (int j = 0; j < 1440; j++) {
                    timeDependency[j] = Float.parseFloat(StringSplitted[j]);
                }
            } else {
                try {
                    timeDependency = CreateTd(StringSplitted);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//	TODO			throw new
            }

            singleLine = itrLines.next();
            if (!singleLine.contains("MinDuration")) {
//	TODO			throw new
            }

            singleLine = itrLines.next();
            timeMin = Integer.parseInt(singleLine);

            result.add(new ADL(IdADL, NameADL, week, weather, timeDependency, timeMin, needs, effects));
        }
		return result;
	}
	
	/**
	 * Checks and apply the drifts of the ADL.
	 * @param time	The time of the simulation
	 */
	public void update(long time){
		Iterator<HighLevelADLDrift> itrDrifts = actdrift.iterator();
		while(itrDrifts.hasNext()){
			HighLevelADLDrift currentDrift = itrDrifts.next();
			if(time == currentDrift.updateTime){
				HighLevelADLDrift.applyDrift(currentDrift, adlmap.get(currentDrift.idADL));
			}
		}
	}
	
	/**
	 * Create an array that represent the time description of an ADL made of a single value.
	 * @param value	The value that would be use to create the array.
	 * @return	An array of 1440 float that represents a time description
	 */
	private static Float[] CreateTd(float value){
		Float[] td = new Float[1440];
		for(int i = 0; i < 1440; i++){
			td[i] = value;
		}
		return td;
	}

    /**
     * Create an array that represent the time description of an ADL made of a sequence of flags:</br>
     * <b>R,HH:MM,F,HH:MM</b>: sharp rising and falling;</br>
     * <b>RS,HH:MM,RE,HH:MM</b>: linear rising (RS rising start, RE rising end);</br>
     * <b>FS,HH:MM,FE,HH:MM</b>: linear falling (FS falling start, FE falling end).</br>
     *
     * Asymmetric edges are accepted.
     *
     * @param Edges  The formatted string describing falling or rising edges.
     * @return	An array of 1440 float that represents a time description
     */
    private static Float[] CreateTd(String[] Edges) throws Exception {
        int tkn=0;
        Float[] td = new Float[1440];
        for(int k = 0 ; k < 1440 ; k++){
            td[k] = 0F;
        }
        while(tkn<Edges.length){
            Integer begin = 0;
            Integer end = 1439;
            if(Edges[tkn].equals("RS")){
                begin = new Time(Edges[tkn + 1]).getMinuteS();
                if(Edges[tkn+2].equals("RE")){
                    end = new Time(Edges[tkn + 3]).getMinuteS();
                }else {
                    throw new Exception("Wrong Timeprofile parameters");
                }
                float delta = 1F/((float)(end - begin));
                for (int i = begin; i < end; i++){
                    if(i>0){
                        td[i] = td[i-1] + delta;
                    }else{
                        td[i] = delta;
                    }
                }
            }
            if((Edges[tkn].equals("R") || Edges[tkn].equals("RE")) && tkn<(Edges.length-3)){
                begin = new Time(Edges[tkn + 1]).getMinuteS();
                if(Edges[tkn+2].equals("F") || Edges[tkn+2].equals("FS")) {
                    end = new Time(Edges[tkn + 3]).getMinuteS();
                }else{
                    // FIXME: this exception must not be thrown in warp around cases, not implemented!
                    //throw new Exception("Wrong Timeprofile parameters");
                }
                for (int i = begin; i < end; i++){
                    td[i] = 1F;
                }
            }
            if(Edges[tkn].equals("FS")){
                if(tkn==0) {
                    throw new Exception("Wrong Timeprofile parameters");
                }
                begin = new Time(Edges[tkn + 1]).getMinuteS();
                if(Edges[tkn+2].equals("FE")){
                    end = new Time(Edges[tkn + 3]).getMinuteS();
                }else {
                    throw new Exception("Wrong Timeprofile parameters");
                }
                float delta = 1F/((float)(end - begin));
                for (int i = begin; i < end; i++){
                    td[i] = td[i-1] - delta;
                }
            }
            tkn+=2;
        }
        return td;
    }


    /**
	 * Return the value of @see {@link #instance}, but if its value equals to null, it would first call the constructor @see {@link #HighLevelADLDB()}.
	 * @return	The value of the Parameters' instance
	 */
	public static HighLevelADLDB getInstance() {
		if(instance == null){
			instance = new HighLevelADLDB();
		}
		return instance;
	}
	/**
	 * Sets the @see {@link #instance} to a new value
	 * @param instance	The new value of instance
	 */
	public static void setInstance(HighLevelADLDB instance) {
		HighLevelADLDB.instance = instance;
	}
	
	/**
	 * Returns @see {@link #adlmap}
	 * @return the value of adlmap
	 */
	public Map<Integer, ADL> getAdlmap() {
		return adlmap;
	}
	
	/**
	 * Sets @see {@link #adlmap} to a new value
	 * @param adlmap	the new value of adlmap
	 */
	public void setAdlmap(Map<Integer, ADL> adlmap) {
		this.adlmap = adlmap;
	}
	
	/**
	 * Searches the id of an ADL.
	 * @param ADLName	The string that represent the name of the ADL
	 * @return	The id of the ADL with the specified name. If any ADL is matched null would be returned
	 */
	public int searchId(String ADLName){
		if(adlmap.containsValue(ADLName)){
			Integer[] keys = adlmap.keySet().toArray(new Integer[adlmap.keySet().size()]);
			Iterator<ADL> map = adlmap.values().iterator();
			int i = -1;
			ADL cADL;
			do{
				cADL = map.next();
				i++;
			}while(cADL.getName() != ADLName);
			return keys[i];
		}else{
			return (Integer)null;
		}
	}


	public ADL getADLById(int id) {
		return adlmap.get(new Integer(id));
	}

	/**
	 * Select the default ADL for start the simulation
	 * @return	The ADL that represent the act "sleeping". If it wouldn't be found, the first ADL would be returned.
	 */
	public ADL defaultADL(){
		Set<Integer> keys = adlmap.keySet();
		Iterator<Integer> ItrKey = keys.iterator();
		
		while(ItrKey.hasNext()){
			int cKey = ItrKey.next();
			ADL cADL = adlmap.get(cKey);
			if(cADL.getName().contains("Sleeping")){
				return cADL;
			}
		}
		
		return adlmap.get(keys.toArray()[0]);
	}
}
