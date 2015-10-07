package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.Main;
import it.polimi.deib.atg.sharon.data.PatternPlace;
import it.polimi.deib.atg.sharon.data.PatternSS;
import it.polimi.deib.atg.sharon.data.Sensorset;
import it.polimi.deib.atg.sharon.engine.ADLMatch;
import it.polimi.deib.atg.sharon.engine.LowLevelADL;
import it.polimi.deib.atg.sharon.engine.LowLevelSSADL;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <h2>EXPERIMENTAL</h2>
 * Container to retrieve the configuration of ADLs execution patterns in terms of activated
 * sensors, sequence and time spent in every location. Config files should be in <b>configs</b>
 * folder and with prefix <b>"patt"</b>
 * </br>
 * <i>Created by fabio on 8/25/15.</i>
 */
public class LowLevelADLDB {

    private Map<Integer,LowLevelADL> patternMap;
    private Map<Integer,LowLevelSSADL> patternSSMap;
    private Map<Integer, ADLMatch> matcher = new HashMap<>();
    private ArrayList<PatternSS> patternSSs;

    private static final String CONFIG_PATH =ConfigurationManager.getInstance().getCONFIG_PATH();
    private static final String PATTERN_PRE=ConfigurationManager.getInstance().getPATTERN_PRE();
    private static final String PATTERN_PRE_SS=ConfigurationManager.getInstance().getPATTERN_PRE_SS();

    private static LowLevelADLDB instance;

    /**
     * Internal constructor for singleton. Called by public getInstance().
     */
    private LowLevelADLDB() throws NotDirectoryException, FileNotFoundException {
        patternMap = new HashMap<>();
        patternSSMap = new HashMap<>();
        patternSSs=new ArrayList<PatternSS>();

        if (Main.USE_HMM_LL) {
            loadSSConfigs(patternSSMap);
        } else {
            loadConfigs(patternMap);
        }

    }

    private void loadConfigs(Map<Integer, LowLevelADL> patternMap) throws NotDirectoryException, FileNotFoundException {
        File folder = new File(CONFIG_PATH);
        if(!folder.exists()){
            throw new NotDirectoryException(null);
        }

        BufferedReader reader = null;

        FilenameFilter ActFilter = (dir, name) -> {
            if(name.lastIndexOf('.')>0){
                if(name.contains("_")){
                    if(!name.equals("patt_template.conf")){
                        int lastIndexDot = name.lastIndexOf('.');
                        int lastIndexUnSl = name.lastIndexOf('_');
                        String filename = name.subSequence(0, lastIndexUnSl).toString();
                        String extension = name.substring(lastIndexDot);
                        if(filename.equals(PATTERN_PRE) && extension.equals(".conf")){
                            return true;
                        }
                    }
                }
            }
            return false;
        };
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(ActFilter)));
        if(fileList.isEmpty()){
            throw new FileNotFoundException(null);
        }

        HashMap<Integer,ArrayList<Float>> probList = new HashMap<Integer,ArrayList<Float>>();
        HashMap<Integer,ArrayList<Integer>> patternIdList = new HashMap<Integer,ArrayList<Integer>>();

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
            for(String pattern:configLines){
                // act_ID, prob, pattern_ID, Patter_Name, Place_ID, perc_time, [...]
                // [...]
                String[] chunks = pattern.split(",");

                if (chunks.length < 6){
                    // TODO throw proper exception
                }
                Integer act_ID = Integer.parseInt(chunks[0]);
                if(!probList.containsKey(act_ID)){
                    probList.put(act_ID,new ArrayList<Float>());
                }
                probList.get(act_ID).add(Float.parseFloat(chunks[1]));
                Integer patternId = Integer.parseInt(chunks[2]);
                if(!patternIdList.containsKey(act_ID)){
                    patternIdList.put(act_ID,new ArrayList<Integer>());
                }
                patternIdList.get(act_ID).add(patternId);

                ArrayList<PatternPlace> places = new ArrayList<>();
                for(int m = 4; m < chunks.length; m=m+2 ){
                    places.add(new PatternPlace(Integer.parseInt(chunks[m]), Float.parseFloat(chunks[m + 1])));
                }
                patternMap.put(patternId, new LowLevelADL(act_ID, chunks[3], places));
            }
        }

        for(Integer act_Id:probList.keySet()){
            matcher.put(act_Id,new ADLMatch(act_Id, patternIdList.get(act_Id), probList.get(act_Id)));
        }
    }
    
    private void loadSSConfigs(Map<Integer, LowLevelSSADL> patternMap) throws NotDirectoryException, FileNotFoundException {
        File folder = new File(CONFIG_PATH);
        if(!folder.exists()){
            throw new NotDirectoryException(null);
        }

        BufferedReader reader = null;

        FilenameFilter ActFilter = (dir, name) -> {
            if(name.lastIndexOf('.')>0){
                if(name.contains("_")){
                    if(!name.equals("patt_template.conf")){
                        int lastIndexDot = name.lastIndexOf('.');
                        int lastIndexUnSl = name.lastIndexOf('_');
                        String filename = name.subSequence(0, lastIndexUnSl).toString();
                        String extension = name.substring(lastIndexDot);
                        if(filename.equals(PATTERN_PRE_SS) && extension.equals(".conf")){
                            return true;
                        }
                    }
                }
            }
            return false;
        };
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(ActFilter)));
        if(fileList.isEmpty()){
            throw new FileNotFoundException(null);
        }

        HashMap<Integer,ArrayList<Float>> probList = new HashMap<Integer,ArrayList<Float>>();
        HashMap<Integer,ArrayList<Integer>> patternIdList = new HashMap<Integer,ArrayList<Integer>>();

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
            //this is the parser to import sensorset patterns
            Integer numLine=0;
            for(String pattern:configLines){
            	numLine++;
                // idAct, prob, Name, numberOfSSInPattern, listOfSSID (comma separated), listOfInitialProb, matrix of probability in line
                String[] chunks = pattern.split(",");

                if (chunks.length < 6){
                    // TODO throw proper exception
                }
                Integer act_ID = Integer.parseInt(chunks[0]);
                if(!probList.containsKey(act_ID)){
                    probList.put(act_ID,new ArrayList<Float>());
                }
                probList.get(act_ID).add(Float.parseFloat(chunks[1]));
                Integer patternId = Integer.valueOf((new Integer(act_ID*100)).toString()+numLine.toString());
                if(!patternIdList.containsKey(act_ID)){
                    patternIdList.put(act_ID,new ArrayList<Integer>());
                }
                patternIdList.get(act_ID).add(patternId);
                
                Integer nSS=Integer.parseInt(chunks[3]);
               ArrayList<Integer> ssIds=new ArrayList<Integer>();
                for(int m = 4; m < 4+nSS; m++ ){
                	//id of the sensorset of the patter
                	ssIds.add(Integer.parseInt(chunks[m]));
                }
                ArrayList<Float> iniProb=new ArrayList<Float>();
                for(int m = 4+nSS; m < 4+nSS+nSS; m++ ){
                	//id of the sensorset of the patter
                	iniProb.add(Float.parseFloat(chunks[m]));
                }
                Float[][] probMatrix=new Float[nSS][nSS];
                for(int row = 0; row < nSS; row++ ){
                	for(int col = 0; col < nSS; col++ ){
                		int m=4+(2*nSS)+(row*nSS)+col;
                        probMatrix[row][col]= Float.parseFloat(chunks[m]);
                    }
                }
                String nameAct=HighLevelADLDB.getInstance().getADLById(act_ID).getName();
                PatternSS patternSS=new PatternSS(patternId,act_ID,chunks[2],nameAct,Float.parseFloat(chunks[1]),ssIds,iniProb,probMatrix);
                patternMap.put(patternId, new LowLevelSSADL(act_ID, chunks[2], patternSS));
                patternSSs.add(patternSS);
            }
        }

        for(Integer act_Id:probList.keySet()){
            matcher.put(act_Id,new ADLMatch(act_Id, patternIdList.get(act_Id), probList.get(act_Id)));
        }
    }

    public static synchronized LowLevelADLDB getInstance() {
        if (instance == null){
            try {
                instance = new LowLevelADLDB();
            } catch (NotDirectoryException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public LowLevelADL get(Integer key){
        return getInstance().patternMap.get(key);
    }
    
    public PatternSS getPatternSS(Integer key){
        for(PatternSS pss:patternSSs){
        	if(pss.getId()==key){
        		return pss;
        	}
        }
        return null;
    }

    
    public ADLMatch getMatch(Integer key){
        return getInstance().matcher.get(key);
    }

	public ArrayList<PatternSS> getPatternSSs() {
		return patternSSs;
	}

	public void setPatternSSs(ArrayList<PatternSS> patternSSs) {
		this.patternSSs = patternSSs;
	}
    
	private ArrayList<PatternSS> getPatternsSSbyAct(Integer idAct){
		ArrayList<PatternSS> pss=new ArrayList<PatternSS>();
		for(PatternSS ps:patternSSs){
			if (ps.getIdAct().equals(idAct)){
				pss.add(ps);
			}
		}
		return pss;
	}
	
	public Integer getPatternIDSS(Sensorset currentSS,Integer idAct) {
		
		ArrayList<PatternSS> allPatterns=getPatternsSSbyAct(idAct);
		
		//choose the next pattern according to the last SS
		
		//for every Pattern retrieve the max probability
		Float absoluteMax=(float) 0;
		Float[] maxProbForPattern=new Float[allPatterns.size()];
		for(int posPattern=0; posPattern<allPatterns.size();posPattern++){
			PatternSS patternSS=allPatterns.get(posPattern);
			Float maxValue=(float) 0;
			for(int posSS=0;posSS<patternSS.getSsIds().size();posSS++){
				//within each SS of the pattern I should find the one with highest prob
				Float probCurrent=(float) 0.0;
				try {
					probCurrent=SensorsetManager.getInstance().getTransitionProb()[currentSS.getIdSensorset()-1][patternSS.getSsIds().get(posSS)-1]; 
					//-1 because the id of the sensorsets are ordered starting from 1 but the position in the matrix starts from zero
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(probCurrent>=maxValue){
					maxValue=probCurrent;
				}
			}
			maxProbForPattern[posPattern]=maxValue;
			if(maxValue>=absoluteMax){
				absoluteMax=maxValue;
			}
		}
		
		//retrieve the list of the pattern with highest probability (can be more than one with same prob)
		ArrayList<Integer> idToConsider=new ArrayList<Integer>();
		ArrayList<Float> initialProb=new ArrayList<Float>();
		Float maxPrioriProb=(float) 0;
		for(int pos=0;pos<maxProbForPattern.length;pos++){
			if(maxProbForPattern[pos]>=absoluteMax){
				PatternSS pss=allPatterns.get(pos);
				idToConsider.add(pss.getId());
				initialProb.add(pss.getProb());
				maxPrioriProb+=pss.getProb();
			}
		}
		
	
		//choose pseudoRandomly among the selected one according to the a priori probability
		Integer selectedPatternId=0;
		float rnd=(float) Math.random()*maxPrioriProb;	
		float cumulativeProb=0;
		int position=0;
		for(Float p:initialProb){
			cumulativeProb+=p.floatValue();
			if(rnd<=cumulativeProb){
				selectedPatternId= idToConsider.get(position);
			}
			position++;
		}
		
		return selectedPatternId;
	} 
}
