package it.polimi.deib.atg.sharon.configs;

import it.polimi.deib.atg.sharon.data.Sensorset;

public class ConfigurationManager {
	private static ConfigurationManager instance;
	private String CONFIG_PATH;
	private String CONFIG_ENV;
    private String PATTERN_PRE;
    private String PATTERN_PRE_SS;
	
    
    private ConfigurationManager(){
    	super();
    	this.CONFIG_PATH="config/patterns";
    	this.CONFIG_ENV="config/env";
        this.PATTERN_PRE="patt";
        this.PATTERN_PRE_SS="pattSS";
    }
    
    public static ConfigurationManager getInstance(){
		if(instance==null){
			instance=new ConfigurationManager();
		}
		return instance;
	}

	public String getCONFIG_PATH() {
		return CONFIG_PATH;
	}

	public void setCONFIG_PATH(String cONFIG_PATH) {
		CONFIG_PATH = cONFIG_PATH;
	}

	public String getPATTERN_PRE() {
		return PATTERN_PRE;
	}

	public void setPATTERN_PRE(String pATTERN_PRE) {
		PATTERN_PRE = pATTERN_PRE;
	}

	public String getPATTERN_PRE_SS() {
		return PATTERN_PRE_SS;
	}

	public void setPATTERN_PRE_SS(String pATTERN_PRE_SS) {
		PATTERN_PRE_SS = pATTERN_PRE_SS;
	}

	public String getCONFIG_ENV() {
		return CONFIG_ENV;
	}

	public void setCONFIG_ENV(String cONFIG_ENV) {
		CONFIG_ENV = cONFIG_ENV;
	}
    
    
}
