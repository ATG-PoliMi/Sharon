package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.data.PatternSS;
import it.polimi.deib.atg.sharon.data.Sensorset;
import it.polimi.deib.atg.sharon.engine.ADLMatch;
import it.polimi.deib.atg.sharon.engine.LowLevelSSADL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SensorsetManager {
	private static SensorsetManager instance;
	private ArrayList<Sensorset> sensorsets;
	private BufferedReader reader;
	private static final String CONFIG_ENV = ConfigurationManager.getInstance()
			.getCONFIG_ENV();

	private SensorsetManager() throws IOException {
		super();
		sensorsets = new ArrayList<Sensorset>();
		this.loadConfigs();
	}

	public static SensorsetManager getInstance() throws IOException {
		if (instance == null) {
			instance = new SensorsetManager();
		}
		return instance;
	}

	public Sensorset getSensorsetByID(Integer idSensorset) {
		for (Sensorset ss : sensorsets) {
			if (ss.getIdSensorset() == idSensorset) {
				return ss;
			}
		}
		return new Sensorset(idSensorset);
	}

	public void addSensorset(Integer idSensorset, Integer minTime,
			Integer maxTime, ArrayList<Integer> activatedSensorsId) {
		Sensorset ss = this.getSensorsetByID(idSensorset);
		ss.setMinTime(minTime);
		ss.setMaxTime(maxTime);
		ss.setActivatedSensorsId(activatedSensorsId);
		sensorsets.add(ss);
	}

	public void loadConfigs() throws IOException {
		File folder = new File(CONFIG_ENV);
		if (!folder.exists()) {
			throw new NotDirectoryException(null);
		}

		File currentFile = new File(CONFIG_ENV + "/sensorset.conf");
		if (!currentFile.exists()) {
			throw new FileNotFoundException(null);
		}

		ArrayList<String> configLines = new ArrayList<String>();
			reader = new BufferedReader(new FileReader(currentFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				configLines.add(line);
			}
		reader.close();
				
		Integer numLine = 0;
		for (String pattern : configLines) {
			numLine++;
			// idSensorset, mintime, maxtime, list of the ids of the activated
			// sensors

			String[] chunks = pattern.split(",");

			if (chunks.length < 3) {
				// TODO throw proper exception
			}

			Integer ss_ID = Integer.parseInt(chunks[0]);
			Integer minTime = Integer.parseInt(chunks[1]);
			Integer maxTime = Integer.parseInt(chunks[2]);
			ArrayList<Integer> actSensorId=new ArrayList<Integer>();
			if(chunks.length>3){
				for(int pos=4; pos<chunks.length;pos++){
					actSensorId.add(Integer.parseInt(chunks[pos]));
				}
			}
			this.addSensorset(ss_ID, minTime, maxTime, actSensorId);
		}

	}

}
