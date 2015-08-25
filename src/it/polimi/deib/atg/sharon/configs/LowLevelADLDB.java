package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.engine.ADLMatch;
import it.polimi.deib.atg.sharon.engine.LowLevelADL;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.*;

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
    private Map<Integer, ADLMatch> matcher = new HashMap<>();

    // TODO migrate these to a single ref in the main file?
    private static final String CONFIG_PATH="config/patterns";
    private static final String PATTERN_PRE="patt";

    private static LowLevelADLDB instance;

    /**
     * Internal constructor for singleton. Called by public getInstance().
     */
    private LowLevelADLDB() throws NotDirectoryException {
        patternMap = new HashMap<>();
        loadConfigs(patternMap);
    }

    private void loadConfigs(Map<Integer, LowLevelADL> patternMap) throws NotDirectoryException {
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
            throw new NullPointerException();
        }
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

            Iterator<String> itrLines = configLines.iterator();

            //TODO parse config file looking for pattern



        }
    }

    public static synchronized LowLevelADLDB getInstance() {
        if (instance == null){
            try {
                instance = new LowLevelADLDB();
            } catch (NotDirectoryException e) {
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
