package atg.polimi.sharon.configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class represents a drift in the value of a needs growth rate, so every instance of this class represent a specific drift.
 * <p>
 * It has 4 members:
 * <ul>
 * <li> The id of the need.
 * <li> The mode of the drift.
 * <li> The time when the drift appears.
 * <li> The new value of the need growth rate.
 * </ul>
 * All those members can be reached and modified with those methods: @see {@link NeedsDrift#getId()}, @see {@link NeedsDrift#setId()},
 * @see {@link NeedsDrift#getMode()}, @see {@link NeedsDrift#setMode(ModeOfDrift)}, @see {@link NeedsDrift#getNewValue()}, 
 * @see {@link NeedsDrift#setNewValue(double)}, @see {@link NeedsDrift#getTime()} and @see {@link NeedsDrift#setTime(long)}.
 * <p>
 * The drifts of the needs are loaded for config files with the method @see {@link NeedsDrift#loadNeedDrift()} in the class @see MainHLLL and stored in the singolet 
 * class @see Parameters, for the next elaboration.
 * <p>
 * @author alessandro
 */
public class NeedsDrift {
	
	public enum ModeOfDrift { STEP };
	
	private int id = 0;
	private ModeOfDrift mode = ModeOfDrift.valueOf("STEP");
	private long time;
	private double newValue;
	
	/**
	 * This constructor is called in method @see {@link NeedsDrift#loadNeedDrift()}.
	 * @param id		The id of the need to drift, is represent as an integer.
	 * @param mode		The mode in which the drift has to be applied, is represent as an enumerative type(that has the implementation only for the mode "STEP").
	 * @param time		The time of the simulation when the drift has to appear, is represent as a long. 
	 * @param newValue	The new value of the needs growth rate, is represent as a double.
	 */
	public NeedsDrift(int id, ModeOfDrift mode,long time, double newValue){
		super();
		this.id= id;
		this.mode= mode;
		this.time= time;
		this.newValue= newValue;
	}
	
	/**
	 * Loads the drifts of needs from the configuration files. It would ignore template files.
	 * @return	An ArrayList of instances of NeedsDrift
	 * @throws NotDirectoryException	If the folder "config" doesn't exist
	 */
	public static synchronized ArrayList<NeedsDrift> loadNeedDrift() throws NotDirectoryException{

		File folder = new File("config");
		if(folder.exists() == false){
			throw new NotDirectoryException(null);
		}
		ArrayList<NeedsDrift> DriftInstances = new ArrayList<NeedsDrift>();
		
		ArrayList<Integer> IdNeed = new ArrayList<Integer>();
		ArrayList<ModeOfDrift> Mode = new ArrayList<ModeOfDrift>();
		ArrayList<Long> Time = new ArrayList<Long>();
		ArrayList<Double> NewValue = new ArrayList<Double>();
		
		BufferedReader reader 	= null;
		
		ArrayList<String> distribution = new ArrayList <String>();

		FilenameFilter NeedDriftFilter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
					if(name.equals("need.drift")){
						return true;
					}
				return false;
			}
		};
		try{
			ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(NeedDriftFilter)));
			reader = new BufferedReader(new FileReader(fileList.get(0)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				distribution.add(line);
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch (Exception e) {e.printStackTrace();} finally {
			try {reader.close();} catch (IOException e) {e.printStackTrace();}
		}
		
		Iterator<String> itr = distribution.iterator();
		while(itr.hasNext()){
			String[] need = itr.next().split("\t");
			if(Arrays.asList(need).size() == 4){
				IdNeed.add(Integer.parseInt(need[0]));
				Mode.add(ModeOfDrift.valueOf(need[1]));
				Time.add(Long.parseLong(need[2]));
				NewValue.add(Double.parseDouble(need[3]));
			} else{
				//throw new
			}
		}
		Integer[] id = IdNeed.toArray(new Integer[IdNeed.size()]);
		ModeOfDrift[] mode = Mode.toArray(new ModeOfDrift[Mode.size()]);
		Long[] time = Time.toArray(new Long[Time.size()]);
		Double[] newValue = NewValue.toArray(new Double[NewValue.size()]);
		
		for(int i = 0; i < id.length; i++){
			int tempId = id[i];
			long tempTime = time[i];
			double tempValue = newValue[i];
			DriftInstances.add(new NeedsDrift(tempId, mode[i], tempTime, tempValue));
		}
		
		return DriftInstances;
	}
	/**
	 * Returns @see {@link #id}
	 * @return	the id of the need of the specified drift.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set @see {@link #id} to a new value.
	 * @param id	the new value of id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns @see {@link #mode}.
	 * @return the mode of the specified drift
	 */
	public ModeOfDrift getMode() {
		return mode;
	}
	
	/**
	 * Sets the @see {@link #mode} to a new value.
	 * @param mode	the new value of mode.
	 */
	public void setMode(ModeOfDrift mode) {
		this.mode = mode;
	}
	
	/**
	 * Returns @see {@link #time}
	 * @return	the time of the specified drift
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Sets @see {@link #time} to a new value
	 * @param time	the new value of time
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * Returns @see {@link #newValue}
	 * @return the new value of the grown rate of the specified drift
	 */
	public double getNewValue() {
		return newValue;
	}
	
	/**
	 * Sets @see {@link #newValue} to a new value.
	 * @param newValue	the new value of the grown rate.
	 */
	public void setNewValue(double newValue) {
		this.newValue = newValue;
	}	
}
