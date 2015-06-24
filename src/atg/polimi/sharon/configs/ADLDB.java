package atg.polimi.sharon.configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import atg.polimi.sharon.engine.ADLEffect;
import atg.polimi.sharon.engine.ADL;
import atg.polimi.sharon.engine.Needs;

/**
 * This class contains:
 * <ul>
 * <li>The maps of all the ADL that the simulator can use.
 * <li>The list of all the drifts that have to appear to the ADL
 * </ul>
 * <p>
 * The first is stored in member @see {@link #adlmap} as a Map<Integer, ADL> and is accessible only with methods @see {@link ADLDB#getAdlmap()} 
 * and @see {@link ADLDB#setAdlmap(Map)()}.
 * <p>
 * The second is stored in member @see {@link #actdrift} as an ArrayList of instances of @see ADLDrift, is accessible with method
 * @see {@link ADLDB#setAdlmap(Map)()}.
 * <p>
 * Both of them are loaded from confing files in the builder @see {@link #ADLDB()}.
 * <p>
 * It's a singleton class, its instance is contained in its member @see {@link #instance}, 
 * accessible only with methods @see {@link #getInstance()} and @see {@link #setInstance(ADLDB)}.
 * <p>
 * It also contains the methods @see {@link #defaultADL()}, that is used in 
 * @see {@link HLThread#HLThread(BlockingQueue<ADLQueue> q, int simulatedDays, int printLog, int mode)}to choose the starting ADL of simulator, 
 * and the methods @see {@link #CreateMap(ArrayList)} and @see {@link #CreateTd(float)} that are used to create correctly @see {@link #adlmap}.
 * <p>
 * @author alessandro 
 */
public class ADLDB {
	
	private static ADLDB instance = null;
	
	private Map<Integer, ADL> adlmap;
	
	private ArrayList<ADLDrift> actdrift;
	
	/**
	 * This builder is called from the method @see {@link #getInstance()}.
	 * <p>
	 * It uses to instance the member @see {@link #adlmap} the method @see {@link #CreateMap(ArrayList)} with the ArrayList of ADL 
	 * that the method @see {@link #loadAct()} has loaded from the config files.
	 * <p>
	 * It also instances the member @see {@link #actdrift} with the method @see {@link ADLDrift#loadActDrift()} that loads the drifts from config files.
	 */
	private ADLDB(){
		super();
		try {
			adlmap = (CreateMap(loadAct()));
			actdrift = (ADLDrift.loadActDrift());
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
		File folder = new File("config");
		if(folder.exists() == false){
			throw new NotDirectoryException(null);
		}
		
		BufferedReader reader = null;
		
		FilenameFilter ActFilter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.')>0){
					if(name.contains("_")){
						if(!name.contains("act_template.conf")){
							int lastIndexDot = name.lastIndexOf('.');
							int lastIndexUnSl = name.lastIndexOf('_');
							String nameoffile = name.subSequence(0, lastIndexUnSl).toString();
							String extention = name.substring(lastIndexDot);
							if(nameoffile.equals("act") && extention.equals(".conf")){
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
		Iterator<File> itrFile = fileList.iterator();
		while(itrFile.hasNext()){
			File CurrentFile = itrFile.next();
			ArrayList<String> distribuction = new ArrayList<String>();
			try{
				reader = new BufferedReader(new FileReader(CurrentFile));
				String line = null;
				while ((line = reader.readLine()) != null) {
					distribuction.add(line);	
				}
			}catch(NullPointerException e){
				e.printStackTrace();
			}catch (Exception e) {e.printStackTrace();} finally {
				try {reader.close();} catch (IOException e) {e.printStackTrace();}
			}
			
			int IdADL = 0;
			String NameADL = null;
			ArrayList<String> needs = new ArrayList<String> ();
			ArrayList<ADLEffect> effects = new ArrayList<ADLEffect> ();
			int timeMin = 0;
			double [] week = new double[7];
			double [] weather = new double[3];
			Float[] timeDependency = new Float[1440];
			Iterator<String> itrDist = distribuction.iterator();
			ArrayList<Double> Weights = new ArrayList<Double> ();
			
			String ADLString;
			String[] StringSplitted;
			
			StringSplitted = itrDist.next().split("\t");
			IdADL = Integer.parseInt(StringSplitted[0]);
			NameADL = StringSplitted[1].toLowerCase();
			
			ADLString = itrDist.next();
			if(!ADLString.contains("Effects")){
//				throw new
			}
			
			StringSplitted = itrDist.next().split(",");
			Iterator<String> ItrWeights = Arrays.asList(StringSplitted).iterator();
			while(ItrWeights.hasNext()){
				Weights.add(Double.parseDouble(ItrWeights.next()));
			}
			for(int j = 0; j< Weights.size(); j++){
				if(Weights.get(j) != 0.0){
					effects.add(new ADLEffect(Needs.getInstance().searchNamewIn(j), Weights.get(j)));
				}
			}
			
			ADLString = itrDist.next();
			if(!ADLString.contains("Weights")){
//				throw new
			}
			
			StringSplitted = itrDist.next().split(",");
			Iterator<String> ItrNeeds = Arrays.asList(StringSplitted).iterator();
			while(ItrNeeds.hasNext()){
				needs.add(ItrNeeds.next().toLowerCase());
			}
			
			ADLString = itrDist.next();
			if(!ADLString.contains("Weekdays")){
//				throw new
			}
			
			StringSplitted = itrDist.next().split(",");
			for(int j = 0; j < 7; j++){
				week[j]= Double.parseDouble(StringSplitted[j]);
			}
			
			ADLString = itrDist.next();
			if(!ADLString.contains("Weather")){
//				throw new
			}
			
			StringSplitted = itrDist.next().split(",");
			for(int j = 0; j < 3; j++){
				weather[j]= Double.parseDouble(StringSplitted[j]);
			}
			
			ADLString = itrDist.next();
			if(!ADLString.contains("Theta")){
//				throw new
			}
			
			StringSplitted = itrDist.next().split(",");
			if(StringSplitted.length == 1){
				timeDependency = CreateTd(Float.parseFloat(StringSplitted[0]));
			} else if(StringSplitted.length == 1440){
				for(int j = 0; j < 1440; j++){
					timeDependency[j] = Float.parseFloat(StringSplitted[j]);
				}
			} else{
//				throw new
			}
			
			ADLString = itrDist.next();
			if(!ADLString.contains("MinDuration")){
//				throw new
			}
			
			ADLString = itrDist.next();
			timeMin = Integer.parseInt(ADLString);
			
			result.add(new ADL(IdADL, NameADL, week, weather, timeDependency, timeMin, needs, effects));
			}
		return result;
	}
	
	/**
	 * Checks and apply the drifts of the ADL.
	 * @param time	The time of the simulation
	 */
	public void update(long time){
		Iterator<ADLDrift> itrDrifts = actdrift.iterator();
		while(itrDrifts.hasNext()){
			ADLDrift currentDrift = itrDrifts.next();
			if(time == currentDrift.updateTime){
				ADLDrift.applyDrift(currentDrift, adlmap.get(currentDrift.idADL));
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
	 * Return the value of @see {@link #instance}, but if its value equals to null, it would first call the constructor @see {@link #ADLDB()}.
	 * @return	The value of the Parameters' instance
	 */
	public static ADLDB getInstance() {
		if(instance == null){
			instance = new ADLDB();
		}
		return instance;
	}
	/**
	 * Sets the @see {@link #instance} to a new value
	 * @param instance	The new value of instance
	 */
	public static void setInstance(ADLDB instance) {
		ADLDB.instance = instance;
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
			if(cADL.getName().contains("sleeping")){
				return cADL;
			}
		}
		
		return adlmap.get(keys.toArray()[0]);
	}
}
