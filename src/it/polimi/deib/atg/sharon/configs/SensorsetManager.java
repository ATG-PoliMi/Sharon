package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.data.Sensorset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

public class SensorsetManager {
	private static SensorsetManager instance;
	private ArrayList<Sensorset> sensorsets;
	private Float[][] transitionProb;
	private BufferedReader reader;
	private static final String CONFIG_ENV = ConfigurationManager.getInstance()
			.getCONFIG_ENV();

	private SensorsetManager() throws IOException {
		super();
		sensorsets = new ArrayList<Sensorset>();
		this.loadConfigsSS();
		int numberSS=sensorsets.size();
		transitionProb=new Float[numberSS][numberSS];
		this.loadConfigsTransition();
	}

	public static SensorsetManager getInstance() throws IOException {
		if (instance == null) {
			instance = new SensorsetManager();
		}
		return instance;
	}

	public Sensorset getSensorsetByID(Integer idSensorset) {
		for (Sensorset ss : sensorsets) {
			if (ss.getIdSensorset().equals(idSensorset)) {
				return ss;
			}
		}
		return null;
	}

	public void addSensorset(Integer idSensorset, ArrayList<Integer> dur, ArrayList<Integer> activatedSensorsId) {
		Sensorset ss=new Sensorset(idSensorset, dur , activatedSensorsId);
		sensorsets.add(ss);
	}

	public void loadConfigsSS() throws IOException {
		File folder = new File(CONFIG_ENV);
		if (!folder.exists()) {
			throw new NotDirectoryException(null);
		}

		File currentFile = new File(CONFIG_ENV + "/sensorsets.conf");
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
			// idSensorset, 100 element of durationDistribution, list of the ids of the activated sensors
			String[] chunks = pattern.split(",");

			if (chunks.length < 101) {
				// TODO throw proper exception
			}
			Integer ss_ID = Integer.parseInt(chunks[0]);
			ArrayList<Integer> durationDistr=new ArrayList<Integer>();
			for(int pos=1; pos<=100;pos++){
				durationDistr.add(Integer.parseInt(chunks[pos]));
			}
			ArrayList<Integer> actSensorId=new ArrayList<Integer>();
				for(int pos=101; pos<chunks.length;pos++){
					actSensorId.add(Integer.parseInt(chunks[pos]));
				}
			this.addSensorset(ss_ID, durationDistr, actSensorId);
		}

	}
	
	public void loadConfigsTransition() throws IOException {
		File folder = new File(CONFIG_ENV);
		if (!folder.exists()) {
			throw new NotDirectoryException(null);
		}

		File currentFile = new File(CONFIG_ENV + "/SStransitionProbability.conf");
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
				
		Integer row = 0;
		for (String pattern : configLines) {
			String[] chunks = pattern.split(",");
			if(chunks.length!=this.getSensorsets().size()){
				System.out.println("Unexpected number of parameters in the row");
				System.out.println("row: "+row.toString());
				System.out.println("Number of elements in the row: "+chunks.length);
				System.out.println("Expected number = number of ss = "+this.getSensorsets().size());
				//Throw the Exception
			}
			for(int col=0; col<chunks.length;col++){
				this.transitionProb[row][col]=Float.parseFloat(chunks[col]);
			}
			row++;
		}
	}

	public Float[][] getTransitionProb() {
		return transitionProb;
	}

	public void setTransitionProb(Float[][] transitionProb) {
		this.transitionProb = transitionProb;
	}

	public ArrayList<Sensorset> getSensorsets() {
		return sensorsets;
	}

	public void setSensorsets(ArrayList<Sensorset> sensorsets) {
		this.sensorsets = sensorsets;
	}
	
	

}
