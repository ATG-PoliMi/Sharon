package atg.polimi.sharon.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import atg.polimi.sharon.configs.Parameters;


public class Needs {
	private static Needs instance = null;
	
	private Integer[] id;
	private String[] name;
	private Double[] status;
		
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

	public static void setInstance(Needs instance) {
		Needs.instance = instance;
	}
	
	public static synchronized Needs getInstance() {
		if(instance==null) {
			ArrayList<Integer> id = new ArrayList<Integer>(); 
			ArrayList<String> name = new ArrayList<String>();
			ArrayList<Double> init = new ArrayList<Double>();
			ArrayList<Double> constant = new ArrayList<Double>();
			try {
				LoadValues(id, name, init, constant);
			} catch (NotDirectoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			instance = new Needs(id, name, init);
			Parameters.getInstance().setNeedsParameters(constant.toArray(new Double[constant.size()]));
		}
		return instance;
	}

	public static void LoadValues(ArrayList<Integer> id, ArrayList<String> name, ArrayList<Double> init, ArrayList<Double> constant) throws NotDirectoryException{
		
		File folder = new File("config");
		if(!folder.exists()){
			throw new NotDirectoryException(null);
		}
		
		BufferedReader reader 	= null;
		
		ArrayList<String[]> distribuction = new ArrayList <String[]>();
		
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
				distribuction.add(line.split("\t"));	
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch (Exception e) {e.printStackTrace();} finally {
			try {reader.close();} catch (IOException e) {e.printStackTrace();}
		}
		
		Iterator<String[]> itr = distribuction.iterator();
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
	
	public String searchNamewIn(int index){
		try{
			return name[index];
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String searchNamewId(int id){
		try{
			return name[Arrays.asList(this.id).indexOf(id)];
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public int searchId(String name){
		try{
			return id[Arrays.asList(getName()).indexOf(name)];
		} catch(Exception e){
			e.printStackTrace();
		}
		return (Integer) null;
	}
	
	public int searchIndex(String name){
		try{
			return name.indexOf(name);
		} catch(Exception e){
			e.printStackTrace();
		}
		return (Integer) null;
	}
	
	public int searchIndex(int id){
		try{
			return Arrays.asList(this.id).indexOf(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		return (Integer) null;
	}
	

	public Integer[] getId() {
		return id;
	}

	public void setId(Integer[] id) {
		this.id = id;
	}
	
	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}
	
	public void setName(int index, String name){
		try{
			this.name[index] = name;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setName(String oldName, String newName){
		try{
			this.name[searchIndex(oldName)] = newName;
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	//TODO: Define if Status[] is a primitive or a Wrapped Class instance
	public double[] getStatus() {
		double [] temp = new double[status.length];
		for(int i = 0; i < status.length; i++){
			temp[i] = status[i];
		}
		return temp;
	}
	
	public Double[] getStatusWrapped() {
		return status;
	}
	
	public Double getStatus(int index) {
		return status[index];
	}

	public void setStatus(String oldName, Double newStatus) {
		try{
			status[searchIndex(oldName)] = newStatus;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setStatus(int index, Double newStatus) {
		try{
			status[index] = newStatus;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setStatus(Double[] status) {
		this.status = status;
	}
	
	@Override
	public String toString(){
		String OutputStr = "NewNeeds:";
		Iterator<String> ItrNeeds = Arrays.asList(getName()).iterator();
		Iterator<Double> ItrStatus = Arrays.asList((getStatusWrapped())).iterator();
		int i = 0;
		while(ItrNeeds.hasNext()){
			i++;
			String ADLLabel = new String(" " + ItrNeeds.next().substring(i, 4) + ":" + Double.parseDouble(ItrStatus.next().toString().substring(1, 4)));
			OutputStr = OutputStr + ADLLabel;
			if(i != getName().length-1){
				OutputStr = OutputStr + ",";
			}
		}
		return OutputStr;
	}
	
}