package it.polimi.deib.atg.sharon.configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import it.polimi.deib.atg.sharon.engine.ADL;
import it.polimi.deib.atg.sharon.engine.ADLEffect;
import it.polimi.deib.atg.sharon.engine.Needs;

/**
 * This class represent a drift of an ADL during the simulation, it contains several members:
 * <ul>
 * <li>The id of the ADL to drift
 * <li>The name of the ADL to drift
 * <li>The mode in which the ADL has to be applied
 * <li>The time of the simulation when the drift has to appear
 * <li>The names of the parameters of the ADL that the drift would change
 * <li>The new values of the parameters of the ADL that the drift would change
 * </ul>
 * All this members are protected so the class has now getters and setters.
 * <p>
 * This class has 2 methods important methods:
 * <p>
 * The first is @see {@link #loadActDrift()}, that is called in the builder of the class @see ADLDB. The reason of this fact is 
 * that the class @see ADLDB is the one who has the member which is the ArrayList of all the drifts that has to appear in the ADL.
 * This methods is also the one how instance this class calling the builder @see #ADLDrift(int, String, ModeOfDrift, long, ArrayList, ArrayList).
 * <p>
 * The second is @see {@link #applyDrift(ADLDrift, ADL)}, that is called in the method @see {@link ADLDB#update(long)}, and it manages to change
 * the parameters of the ADL agreeing with the selected drift. (This method doesn't check the time of the drift, that is a task of the method @see {@link ADLDB#update(long)})
 * <p>
 * It also contains a method @see {@link #CreateTd(float)}, that is used in {@link #applyDrift(ADLDrift, ADL)}.
 * <p>
 * @author alessandro
 */
public class ADLDrift {
	
	public enum ModeOfDrift { STEP };
	
	int idADL = 0;
	String nameADL = null;
	ModeOfDrift mode;
	long updateTime = 0;
	ArrayList<String> Parameters;
	ArrayList<String> Constants;
	
	/**
	 * This builder is called in the method @see {@link #loadActDrift()}, the last two members are ArrayList of string because the parser doesn't at the start
	 * the way it has to parse the config files so they are stored as String to been next parse while the drift appears by the method @see {@link #applyDrift(ADLDrift, ADL)}.
	 * @param idADL			The id of the ADL that has to change, represent as an integer.
	 * @param nameADL		The name of the ADL that has to change, represent as a string.
	 * @param mode			The mode in which the drift has to appears, represent as a enumerative type. (Only the mode "STEP" is implemented).
	 * @param updateTime	The time of simulation when the drift has to happen, represent as a long.
	 * @param Parameters	The name of the parameters of the ADL that has to change, represent as an ArrayList of string.
	 * @param Constants		The new values of the parameters of the ADL that has to change, represent as an ArrayList of string.
	 */
	public ADLDrift(int idADL, String nameADL, ModeOfDrift mode, long updateTime, ArrayList<String> Parameters,ArrayList<String> Constants){
		super();
		this.idADL = idADL;
		this.nameADL= nameADL;
		this.mode= mode;
		this.updateTime = updateTime;
		this.Parameters= Parameters;
		this.Constants= Constants;
	}
	
	/**
	 * Loads the ADLDrifts from configuration files. Template files would be ignored
	 * @return	An ArrayList of instances of ADLDrift that contains all the drifts loaded
	 * @throws NotDirectoryException	If folder "config" doesn't exist
	 */
	public static ArrayList<ADLDrift> loadActDrift()throws NotDirectoryException{
		
		ArrayList<ADLDrift>result = new ArrayList<ADLDrift>();
		File folder = new File("config");
		if(folder.exists() == false){
			throw new NotDirectoryException(null);
		}
		
		int IdADL = 0;
		String NameADL = null;
		ModeOfDrift Mode;
		long UpdateTime = 0;
		ArrayList<String> paramName = new ArrayList<String> ();
		ArrayList<String> paramValue = new ArrayList<String> ();
		
		BufferedReader reader = null;
		
		ArrayList<String> distribuction = new ArrayList<String>();
		FilenameFilter ActDriftFilter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.')>0){
					if(name.contains("_")){
						if(!name.contains("act_template.drift")){
							int lastIndexDot = name.lastIndexOf('.');
							int lastIndexUnSl = name.lastIndexOf('_');
							String nameoffile = name.subSequence(0, lastIndexUnSl).toString();
							String extention = name.substring(lastIndexDot);
							if(nameoffile.equals("act") && extention.equals(".drift")){
								return true;
							}
						}
					}
				}
				return false;
			}
		};
		ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(ActDriftFilter)));
		Iterator<File> itrFile = fileList.iterator();
		while(itrFile.hasNext()){
			File CurrentFile = itrFile.next();
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
			
			Iterator<String> itrDist = distribuction.iterator();
			
			String ADLString;
			String[] StringSplitted;
			
			StringSplitted = itrDist.next().split(",");
			IdADL = Integer.parseInt(StringSplitted[0]);
			NameADL = StringSplitted[1];
			
			StringSplitted = itrDist.next().split(",");
			Mode = ModeOfDrift.valueOf(StringSplitted[0]);
			UpdateTime = Long.parseLong(StringSplitted[1]);
			
			int i = 0;
			while(itrDist.hasNext()){
				ADLString = itrDist.next();
				if((i%2) == 0){
					paramName.add(ADLString);
				}else{
					paramValue.add(ADLString);
				}
				i++;
			}
			
			result.add(new ADLDrift(IdADL, NameADL, Mode, UpdateTime, paramName, paramValue));
			}
		return result;
	}
	
	/**
	 * Apply a selected drift to the correspondent ADL.
	 * <p>
	 * This method also takes cares of the parsing of @see {@link #Parameters} and @see {@link #Constants}, choosing in base of the contents of the 
	 * item of @see {@link #Parameters}, the way in which parse the correspondent string of @see {@link #Constants}.
	 * @param drift			The drift to apply
	 * @param outdateADL	The ADL to update
	 */
	public static void applyDrift(ADLDrift drift, ADL outdateADL){
		Iterator<String> itrName = drift.Parameters.iterator();
		Iterator<String> itrEffects = drift.Constants.iterator();
		while(itrName.hasNext()){
			String name = itrName.next();
			String effect = itrEffects.next();
			if(name.contains("Effects")){
				Iterator<String> ItrNeeds = Arrays.asList(effect.split(",")).iterator();
				ArrayList<String> needs = new ArrayList<String>();
				while(ItrNeeds.hasNext()){
					needs.add(ItrNeeds.next());
				}
				outdateADL.setNeeds(needs);
			} else	if(name.contains("Weights")){
				Iterator<String> ItrWeights = Arrays.asList(effect.split(",")).iterator();
				ArrayList<Double> Weights = new ArrayList<Double>();
				ArrayList<ADLEffect> effects = new ArrayList<ADLEffect>();
				while(ItrWeights.hasNext()){
					Weights.add(Double.parseDouble(ItrWeights.next()));
				}
				for(int j = 0; j< Weights.size(); j++){
					effects.add(new ADLEffect(Needs.getInstance().searchNamewIn(j), Weights.get(j)));
				}
				outdateADL.setEffects(effects);
			}else if(name.contains("Weekdays")){
				Iterator<String> ItrDay = Arrays.asList(effect.split(",")).iterator();
				double[] day = new double[7];
				for(int i = 0; i < day.length; i++){
						day[i] = Double.parseDouble(ItrDay.next());
				}
				outdateADL.setDays(day);
			}else if(name.contains("Weather")){
				Iterator<String> ItrWeather = Arrays.asList(effect.split(",")).iterator();
				double[] weather = new double[3];
				for(int i = 0; i < weather.length; i++){
						weather[i] = Double.parseDouble(ItrWeather.next());
				}
				outdateADL.setWeather(weather);
			}else if(name.contains("Theta")){
				String[] StringSplitted = effect.split(",");
				Float[] timeDependency = new Float[1440];
				if(StringSplitted.length == 1){
					timeDependency = CreateTd(Float.parseFloat(StringSplitted[0]));
				} else if(StringSplitted.length == 1440){
					for(int j = 0; j < 1440; j++){
						timeDependency[j] = Float.parseFloat(StringSplitted[j]);
					}
				}
				outdateADL.setTimeDescription(timeDependency);
			}else if(name.contains("MinDuration")){
				outdateADL.setMinTime(Integer.parseInt(effect));
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
}
