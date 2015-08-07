package it.polimi.deib.atg.sharon.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import it.polimi.deib.atg.sharon.configs.Parameters;

/**
 * This class contains all the needs of the simulation in 3 parallel arrays, so a value placed at the same index in different arrays refers to the same need.
 * <p>
 * The first @see #id contains the ids of the needs and it's accessible with the methods @see {@link #getId()} and @see {@link #setId(Integer[])}
 * <p>
 * The second @see #name contains the names of the needs and it's accessible with the methods @see {@link #getName()}, @see {@link #setName(String[])}, 
 * @see {@link #setName(int, String)} and @see {@link #setName(String, String)}.
 * <p>
 * The third @see #status contains the actual status of the needs and it's accessible with the methods @see {@link #getStatus()}, 
 * @see {@link #getStatus(int)}, @see {@link #setStatus(Double[])}, @see {@link #setStatus(int, Double)}, @see {@link #setStatus(String, Double)}.
 * <p>
 * This class has also methods to search values in those 3 arrays, such as @see {@link #searchId(String)}, @see {@link #searchIndex(int)}, 
 * @see {@link #searchIndex(String)}, @see {@link #searchNamewId(int)} and  @see {@link #searchNamewIn(int)}, that are called in several other classes.
 * <p>
 * It's a singleton class, so its instance is contained in its member @see {@link #instance}, 
 * accessible only with the methods @see {@link #getInstance()} and @see {@link #setInstance(Needs)}.
 * <p>
 * It contains a method @see {@link #LoadValues(ArrayList, ArrayList, ArrayList, ArrayList)}, that allows it to load the needs from the config file.
 * <p>
 * It also contains an override method @see {@link #toString()} that returns a string with all the labels and the currents status of needs properly formatted.
 * <p>
 * @author alessandro
 */

public class Needs {
	
	private static Needs instance = null;
	
	private Integer[] id;
	private String[] name;
	private Double[] status;
	
	/**
	 * This constructor instances the members of the class, it is called in the methods @see {@link #getInstance()} that provides the values to build up the class. 
	 * If in the configuration files there aren't any init values, the method supplies 
	 * to initialize all the status values of the arrays to "0.0" except for the one of the "Sleeping" that is setted to "1.0".
	 * @param Id	Integer that represents the ids of the needs
	 * @param Name	String that represents the name of the needs
	 * @param init	Double that represents the init value of the status of a need. If a value in this array equals null the methods provide an arbitrary initialization.
	 */
	private Needs(ArrayList<Integer> Id, ArrayList<String> Name, ArrayList<Double> init){
		/*
		this.hunger			=	0.2;
		this.stress			=	1.0;
		this.sweat			=	0.0;
		this.toileting		=	0.0;
		this.tirediness		=	1.0;
		this.boredom		=	0.0;
		this.outOfStock		= 	0.0;
		this.dirtiness		= 	0.0;
		this.asociality		= 	0.0;
		*/
		super();
		this.id = new Integer[Id.size()];
		this.name = new String[Id.size()];
		this.status= new Double[Id.size()];
		
		Id.toArray(this.id);
		Name.toArray(this.name);
		
		Iterator<Double> itrInit = init.iterator();
		int i = 0;
		while(itrInit.hasNext()){
			Double cInit = itrInit.next();
			if(cInit == null){
				if(name[i].contains("tiredness")){
					this.status[i] = 1.0;
				} else{
					this.status[i] = 0.0;
				}
			} else{
				this.status[i] = cInit;
			}
			i++;
		}
	}
	
	/**
	 * Change the value of @see {@link #instance}
	 * @param instance	The new value of instance
	 */
	public static void setInstance(Needs instance) {
		Needs.instance = instance;
	}
	
	/**
	 * Return the value of @see {@link #instance}, but if its value equals to null, first it will load the values of need from the config files 
	 * using the method @see {@link #LoadValues(ArrayList, ArrayList, ArrayList, ArrayList)} and then call the constructor
	 * @see {@link #Needs(ArrayList, ArrayList, ArrayList)} and it will also set the needs growth rate with the methods @see {@link Parameters#setNeedsParameters(Double[])}.
	 * @return	The value of the Needs' instance
	 */
	public static synchronized Needs getInstance() {
		if(instance==null) {
			ArrayList<Integer> id = new ArrayList<Integer>(); 
			ArrayList<String> name = new ArrayList<String>();
			ArrayList<Double> init = new ArrayList<Double>();
			ArrayList<Double> constant = new ArrayList<Double>();
			try {
				LoadValues(id, name, init, constant);
			} catch (NotDirectoryException e) {
				e.printStackTrace();
			}
			instance = new Needs(id, name, init);
			Parameters.getInstance().setNeedsParameters(constant.toArray(new Double[constant.size()]));
		}
		return instance;
	}
	
	/**
	 * Loads the values of the instances of the class "Needs" in the ArrayList specified. It doesn't load the template file.
	 * @param id		The id of the needs.
	 * @param name		The name of the needs.
	 * @param init		The init value of the needs. If the file doesn't contains its, the method will put null.
	 * @param constant	The growing constant of the needs.
	 * @throws NotDirectoryException	If the folder "Config" doesn't exist
	 */
	public static void LoadValues(ArrayList<Integer> id, ArrayList<String> name, ArrayList<Double> init, ArrayList<Double> constant) throws NotDirectoryException{
		
		File folder = new File("config");
		if(!folder.exists()){
			throw new NotDirectoryException(null);
		}
		
		BufferedReader reader 	= null;
		
		ArrayList<String[]> distribution = new ArrayList <String[]>();
		
		String[] need = null;

		FilenameFilter NeedsFilter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
					if(name.equals("need.conf")){
						return true;
					}
				return false;
			}
		};
		try{
			ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(NeedsFilter)));
			reader = new BufferedReader(new FileReader(fileList.get(0)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				distribution.add(line.split(","));
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch (Exception e) {e.printStackTrace();} finally {
			try {reader.close();} catch (IOException e) {e.printStackTrace();}
		}
		
		Iterator<String[]> itr = distribution.iterator();
		while(itr.hasNext()){
			need = itr.next();
			if(Arrays.asList(need).size() == 3 || Arrays.asList(need).size() == 4){
				id.add(Integer.parseInt(need[0]));
				name.add(need[1].toLowerCase());
				constant.add(Double.parseDouble(need[2]));
				if(Arrays.asList(need).size() == 4){
					init.add(Double.parseDouble(need[3]));
				} else {
					init.add(null);
				}
			} else{
	//			throw new
			}
		}
	}
	
	/**
	 * Searches the name of a need having its index. If the index is out of bound it will return null
	 * @param index	The index of name in the array.
	 * @return	The name contained in the specified index or null
	 */
	public String searchNamewIn(int index){
		try{
			return name[index];
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Searches the name of a need having its id. If the id isn't contained in the array, it will return null
	 * @param id	The id of a need.
	 * @return		The name of the need that has the specified id or null.
	 */
	public String searchNamewId(int id){
		try{
			return name[Arrays.asList(this.id).indexOf(id)];
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Searches the id of a need having its name. If the name isn't contained in the array, it will return null
	 * @param name	The string that represents the name of a need
	 * @return		The id of the need with the specified need
	 */
	public int searchId(String name){
		try{
			return id[Arrays.asList(getName()).indexOf(name)];
		} catch(Exception e){
			e.printStackTrace();
		}
		return (Integer) null;
	}
	
	/**
	 * @Overload
	 * Searches the index of a need having its name. If the name isn't contained in the array, it will return null
	 * @param name	The string that represents the name of a need
	 * @return		The index of the need with the specified need. If the name isn't matched with no needs in the vector null would be returned
	 */
	public int searchIndex(String name){
		Integer index = null;
		for(int i = 0; i < this.name.length; i++){
			if(this.name[i].contains(name)){
				index = i;
			}
		}
		if(index != null){
			return index;
		}
		return (Integer) null;
	}
	
	/**
	 * @Overload
	 * Searches the index of a need having its id. If the id isn't contained, it will return null
	 * @param id	The id of the need in the configs
	 * @return		The index of the need with the specified need. If the name isn't matched with no needs in the vector null would be returned
	 */
	public int searchIndex(int id){
		try{
			return Arrays.asList(this.id).indexOf(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		return (Integer) null;
	}
	
	/**
	 * Returns @see {@link #id};
	 * @return	The array of the ids of the needs
	 */
	public Integer[] getId() {
		return id;
	}
	
	/**
	 * Sets a new array as @see {@link #id}
	 * @param id	New array
	 */
	public void setId(Integer[] id) {
		this.id = id;
	}
	
	/**
	 * Returns @see {@link #name}
	 * @return	The array of the names of the needs
	 */
	public String[] getName() {
		return name;
	}

	/**
	 * Sets a new array as @see {@link #name}
	 * @param name	New array
	 */
	public void setName(String[] name) {
		this.name = name;
	}
	
	/**
	 * Sets a value placed at the index in @see {@link #name} with a new value.
	 * @param index		The index of the value
	 * @param name		The new value
	 */
	public void setName(int index, String name){
		try{
			this.name[index] = name;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a value placed in @see {@link #name} that have the specified name with a new value.
	 * @param oldName	The old name of the value.
	 * @param newName	The new name of the value
	 */
	public void setName(String oldName, String newName){
		try{
			this.name[searchIndex(oldName)] = newName;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns @see {@link #status} converted into primitive type
	 * @return	The array of the status
	 */
	//TODO: Define if Status[] is a primitive or a Wrapped Class instance
	public double[] getStatus() {
		double [] temp = new double[status.length];
		for(int i = 0; i < status.length; i++){
			temp[i] = status[i];
		}
		return temp;
	}
	
	/**
	 * Returns @see {@link #status}
	 * @return	The array of the status
	 */
	public Double[] getStatusWrapped() {
		return status;
	}
	
	/**
	 * Returns the value of the element of @see {@link #status} placed at the specified index
	 * @param index	Index of the desired value
	 * @return		The value placed at the position of index
	 */
	public Double getStatus(int index) {
		return status[index];
	}

	/**
	 * Sets the status of the need specified with its own name to a new value
	 * @param Name	Name of the need
	 * @param newStatus	New status of the need
	 */
	public void setStatus(String Name, Double newStatus) {
		try{
			status[searchIndex(Name)] = newStatus;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Sets the value of @see {@link #status} placed at index to a new value
	 * @param index	 	The index of the desired value
	 * @param newStatus	The new value
	 */
	public void setStatus(int index, Double newStatus) {
		try{
			status[index] = newStatus;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a new array to @see {@link #status}
	 * @param status	New array
	 */
	public void setStatus(Double[] status) {
		this.status = status;
	}
	
	/**
	 * @Override
	 * @return the string that represents the status of the class. 
	 * It's formatted to have a label made of 3 letter for every need and the status with 3 decimal digits.
	 */
	public String toString(){
		String OutputStr = "NewNeeds:";
		Iterator<String> ItrNeeds = Arrays.asList(getName()).iterator();
		Iterator<Double> ItrStatus = Arrays.asList((getStatusWrapped())).iterator();
		int i = 0;
		while(ItrNeeds.hasNext()){
			String name = ItrNeeds.next();
			i++;
			String ADLLabel = new String(" " + name.substring(0, 1).toUpperCase()+ name.substring(1, 3) + ":" + String.format("%.3f",ItrStatus.next()));
			OutputStr = OutputStr + ADLLabel;
			if(i != getName().length-1){
				OutputStr = OutputStr + ",";
			}
		}
		return OutputStr;
	}
	
}