package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.data.PatternPlace;
import it.polimi.deib.atg.sharon.data.PatternSS;
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

    private static final String CONFIG_PATH =ConfigurationManager.getInstance().getCONFIG_PATH();
    private static final String PATTERN_PRE=ConfigurationManager.getInstance().getPATTERN_PRE();
    private static final String PATTERN_PRE_SS=ConfigurationManager.getInstance().getPATTERN_PRE_SS();

    private static LowLevelADLDB instance;

    /**
     * Internal constructor for singleton. Called by public getInstance().
     */
    private LowLevelADLDB() throws NotDirectoryException, FileNotFoundException {
        patternMap = new HashMap<>();
        loadConfigs(patternMap);
        loadSSConfigs(patternSSMap);
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
                // idAct, prob, Name, numberOfSSInPattern, listOfSSID (comma separated), matrix of probability in line
            
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
                PatternSS patternSS=new PatternSS(patternId,ssIds,iniProb,probMatrix);
                patternMap.put(patternId, new LowLevelSSADL(act_ID, chunks[2], patternSS));
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

    public ADLMatch getMatch(Integer key){
        return getInstance().matcher.get(key);
    }
}
