package it.polimi.deib.atg.sharon.configs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ActivityViewer {
	private static int numAct=14;
	private static ActivityViewer instance;
	private Map<Integer,List<Integer>> activityGraph;
	private Map<Integer,List<Integer>> averageDurationPerDay;
	private Map<Integer,List<Integer>> numOccurrencesPerDay;
	private Map<Integer,List<Integer>> overallDurationPerDay;
	
	public static ActivityViewer getInstance(){
		if(instance==null){
			instance=new ActivityViewer();
		}
		return instance;
	}
	
	private ActivityViewer() {
		super();
		this.activityGraph=new HashMap<Integer,List<Integer>>();
		this.averageDurationPerDay=new HashMap<Integer,List<Integer>>();
		this.numOccurrencesPerDay=new HashMap<Integer,List<Integer>>();
		this.overallDurationPerDay=new HashMap<Integer,List<Integer>>();
		for(int act=0;act<numAct;act++){
			this.activityGraph.put(act+1, new ArrayList<Integer>());
		}
	}

	public void initDay(int d){
		if(this.activityGraph.size()>0){
			//add a new day
			List<Integer> avgDur=new ArrayList<Integer>();
			List<Integer> numOcc=new ArrayList<Integer>();
			List<Integer> totDur=new ArrayList<Integer>();
			for(int act=1;act<=numAct;act++){
				int td=0;
				int no=0;
				for(Integer occ:this.activityGraph.get(act)){
					td+=occ;
					no++;
				}
				List<Integer> l=this.activityGraph.get(act);
				Collections.sort(l);
				int ad=0;
				if((int) l.size()/2>0){
					ad=(int) Math.round(l.get((int) (l.size()/2)));
				}
				avgDur.add(ad);
				numOcc.add(no);
				totDur.add(td);
			}
			this.averageDurationPerDay.put(d, avgDur);
			this.numOccurrencesPerDay.put(d, numOcc);
			this.overallDurationPerDay.put(d, totDur);
		}
		// empty activityGraph
		this.activityGraph.clear();
		this.activityGraph= new HashMap<Integer,List<Integer>>();
		for(int act=0;act<numAct;act++){
			this.activityGraph.put(act+1, new ArrayList<Integer>());
		}
	}
	
	
	public void addActivity(Integer actId,Integer value){
		this.activityGraph.get(actId).add(value);
	}
	
	public void printFile(){
		printAll();
	}
	
	public void printAll(){
		try {
			File folder = new File("data");
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			
			//print total duration
			File currentFile = new File("data/activityTDView.txt");
			FileOutputStream fos = new FileOutputStream(currentFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(Entry<Integer, List<Integer>> td:this.overallDurationPerDay.entrySet()){
				String line="";
				for(Integer f:td.getValue()){
					line+=f.toString()+",";
					
				}	
				line=line.substring(0,line.length()-1);
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			
			//print number occ
			currentFile = new File("data/activityNOView.txt");
			fos = new FileOutputStream(currentFile);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(Entry<Integer, List<Integer>> td:this.numOccurrencesPerDay.entrySet()){
				String line="";
				for(Integer f:td.getValue()){
					line+=f.toString()+",";
					
				}	
				line=line.substring(0,line.length()-1);
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			
			//print average duration
			currentFile = new File("data/activityADView.txt");
			fos = new FileOutputStream(currentFile);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(Entry<Integer, List<Integer>> td:this.averageDurationPerDay.entrySet()){
				String line="";
				for(Integer f:td.getValue()){
					line+=f.toString()+",";
					
				}	
				line=line.substring(0,line.length()-1);
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	}

