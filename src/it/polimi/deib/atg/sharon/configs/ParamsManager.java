package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.engine.LowLevelADL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamsManager {
	public static ParamsManager instance;
	private int[] sid;//sensorId
	private Map<Integer, List<Float>> activityRhythms; //not used
	private static final String CONFIG_ENV = ConfigurationManager.getInstance().getCONFIG_ENV();

	private ParamsManager() {
		super();
		activityRhythms = new HashMap<>();
		loadConfig();
	}

	public static ParamsManager getInsatnce() {
		if (instance == null) {
			instance = new ParamsManager();
		}
		return instance;
	}

	private void loadConfig() {
		try {
			/*
			 * Importing activityRythms -> no more used
			 * 
			 * 
			File folder = new File(CONFIG_ENV);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}

			File currentFile = new File(CONFIG_ENV + "/activityRhythm.conf");
			if (!currentFile.exists()) {
				throw new FileNotFoundException(null);
			}

			ArrayList<String> configLines = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(currentFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				configLines.add(line);
			}
			reader.close();

			Integer numActivityRhythms=0;
			for (String pattern : configLines) {
				// idAct, k, list of k values

				String[] chunks = pattern.split(",");
				if (chunks.length < 2) {
					throw new Exception("Unexpected format of row in configuration file");
				}else{
					List<Float> coeffAct=new ArrayList<Float>();
					Integer idAct=Integer.valueOf(chunks[0]);
					Integer k=Integer.valueOf(chunks[1]);
					for(Integer i=0;i<k;i++){
						Float fn=Float.valueOf(chunks[i+2]);
						coeffAct.add(fn);
					}
					this.activityRhythms.put(idAct, coeffAct);
					numActivityRhythms++;
				}
			}
			System.out.println("Imported "+numActivityRhythms.toString()+" activity rhythms line");
			 */
			//now sensors
			File currentFile = new File(CONFIG_ENV + "/sensorsId.conf");
			if (!currentFile.exists()) {
				throw new FileNotFoundException(null);
			}

			ArrayList<String> configLines = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(currentFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				configLines.add(line);
			}
			reader.close();

			Integer numSensors=0;
			for (String pattern : configLines) {
				// sensors id , separated

				String[] chunks = pattern.split(",");
				this.sid=new int[chunks.length];
				for(int i=0;i<chunks.length;i++){
					this.sid[i]=Integer.parseInt(chunks[i]);
				}
				numSensors++;
			}

			System.out.println("Imported "+numSensors.toString()+" sensors");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Float> getRhythmCoeffByIdAct(Integer id) {
		return activityRhythms.get(id);
	}
	public int[] getSid() {
		return sid;
	}

	public void setSid(int[] sid) {
		this.sid = sid;
	}
	
}
