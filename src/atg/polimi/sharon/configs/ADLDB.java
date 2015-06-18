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
import atg.polimi.sharon.engine.ADLEffect;
import atg.polimi.sharon.engine.ADL;
import atg.polimi.sharon.engine.Needs;

public class ADLDB {
	
	private static ADLDB instance = null;
	
	private Map<Integer, ADL> adlmap;
	
	private ArrayList<ADLDrift> actdrift;
	
	private ADLDB(){
		super();
		try {
			adlmap = (CreateMap(loadAct()));
			actdrift = (ADLDrift.loadActDrift());
		} catch (NotDirectoryException e) {
			e.printStackTrace();
		}
	}
	
	private Map<Integer, ADL> CreateMap(ArrayList<ADL> ADLList){
		Map<Integer, ADL> Map = new HashMap<>();
		Iterator<ADL> ItrADL = ADLList.iterator();
		while(ItrADL.hasNext()){
			ADL cADL = ItrADL.next();
			Map.put(cADL.getId(), cADL);
		}
		return Map;
	}

	private static ArrayList<ADL> loadAct()throws NotDirectoryException{
			
		ArrayList<ADL>result = new ArrayList<ADL>();
		File folder = new File("config");
		if(folder.exists() == false){
			throw new NotDirectoryException(null);
		}
		
		int IdADL = 0;
		String NameADL = null;
		ArrayList<String> needs = new ArrayList<String> ();
		ArrayList<ADLEffect> effects = new ArrayList<ADLEffect> ();
		int timeMin = 0;
		double [] week = new double[7];
		double [] weather = new double[3];
		Float[] timeDependency = new Float[1440];
		
		BufferedReader reader = null;
		
		ArrayList<String> distribuction = new ArrayList<String>();
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
			ArrayList<Double> Weights = new ArrayList<Double> ();
			
			String ADLString;
			String[] StringSplitted;
			
			StringSplitted = itrDist.next().split("\t");
			IdADL = Integer.parseInt(StringSplitted[0]);
			NameADL = StringSplitted[1];
			
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
				effects.add(new ADLEffect(Needs.getInstance().searchNamewIn(j), Weights.get(j)));
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
	
	public void update(long time){
		Iterator<ADLDrift> itrDrifts = actdrift.iterator();
		while(itrDrifts.hasNext()){
			ADLDrift currentDrift = itrDrifts.next();
			if(time == currentDrift.updateTime){
				ADLDrift.applyDrift(currentDrift, adlmap.get(currentDrift.idADL));
			}
		}
	}
	
	private static Float[] CreateTd(float value){
		Float[] td = new Float[1440];
		for(int i = 0; i < 1440; i++){
			td[i] = value;
		}
		return td;
	}

	public static ADLDB getInstance() {
		if(instance == null){
			instance = new ADLDB();
		}
		return instance;
	}

	public static void setInstance(ADLDB instance) {
		ADLDB.instance = instance;
	}

	public Map<Integer, ADL> getAdlmap() {
		return adlmap;
	}

	public void setAdlmap(Map<Integer, ADL> adlmap) {
		this.adlmap = adlmap;
	}
	
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
	
	public ADL defaultADL(){
		if(adlmap.containsValue("Sleep")){
			return adlmap.get(searchId("Sleep"));
		} else{
			Integer[] keys = adlmap.keySet().toArray(new Integer[adlmap.keySet().size()]);
			return adlmap.get(keys[0]);
		}
	}
}
