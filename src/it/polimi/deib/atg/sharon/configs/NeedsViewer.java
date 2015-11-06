package it.polimi.deib.atg.sharon.configs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NeedsViewer {
	private static NeedsViewer instance;
	private Map<Integer,List<Float>> needsGraph;
	
	private NeedsViewer() {
		super();
		this.needsGraph=new HashMap<Integer,List<Float>>();
	}
	
	public static NeedsViewer getInstance(){
		if(instance==null){
			instance=new NeedsViewer();
		}
		return instance;
	}
	
	public void addNeed(Integer needId){
		this.needsGraph.put(needId, new ArrayList<Float>());
	}
	
	public void addNeed(Integer needId,Float value){
		this.needsGraph.get(needId).add(value);
	}
	
	public void printFile(){
		printComplete();
		//printNeed(1);
	}
	
	public void printNeed(int need){
		try {
			File folder = new File("data");
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			File currentFile = new File("data/needs"+need+"View.txt");
			FileOutputStream fos = new FileOutputStream(currentFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			List<Float> lf=needsGraph.get(need);
				for(Float f:lf){
					bw.write(f.toString());
					bw.newLine();
				}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printComplete(){
		try {
			File folder = new File("data");
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			
			for(Entry<Integer, List<Float>> n:needsGraph.entrySet()){
				File currentFile = new File("data/needs"+n.getKey().toString()+"View.txt");
				FileOutputStream fos = new FileOutputStream(currentFile);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				for(Float f:n.getValue()){
					bw.write(f.toString());
					bw.newLine();
				}
				bw.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
