package it.polimi.deib.atg.sharon.engine.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;




public class ActivityImportationThread implements Runnable {
	private BlockingQueue<ADLQueue> q;
	private String url;
	private Integer numd;
	private ArrayList<File> orderedList;

    public ActivityImportationThread(BlockingQueue<ADLQueue> q, String url){
		this.q=q;
        this.url = url;
        this.numd=0;
	}
    
    
    public int numberOfDays(){
    	try{
    	File folder = new File(url);
        if(!folder.exists()){
            throw new NotDirectoryException(null);
        }

        FilenameFilter ActFilter = (dir, name) -> {
            if(name.lastIndexOf('.')>0){
                if(name.contains("_")){
                        int lastIndexDot = name.lastIndexOf('.');
                        int lastIndexUnSl = name.lastIndexOf('_');
                        String filename = name.subSequence(0, lastIndexUnSl).toString();
                        String extension = name.substring(lastIndexDot);
                        if(filename.equals("DAY") && extension.equals(".txt")){
                            return true;
                        }
                }
            }
            return false;
        };
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(ActFilter)));
        if(fileList.isEmpty()){
            throw new FileNotFoundException(null);
        }
        //order the file list
        orderedList=new ArrayList<File>();
        for(Integer i=0;i<=fileList.size();i++){
        	for (File CurrentFile : fileList) {
        		if(CurrentFile.getName().equals("DAY_"+i.toString()+".txt")){
        			orderedList.add(CurrentFile);
        			this.numd++;
        		}
        	}
        }
        if(fileList.size()!=(orderedList.size())){
        	throw new Exception("Dimension of list of files wrong");
        }
        fileList=null;
        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return numd;
    }

	@Override
	public void run() {		
		try{BufferedReader reader = null;
			Integer nFile=0;
	        for (File CurrentFile : orderedList) {
	     	   nFile++;
	             ArrayList<String> configLines = new ArrayList<String>();
	             try {
	                 reader = new BufferedReader(new FileReader(CurrentFile));
	                 String line = null;
	                 line=reader.readLine();
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
	             String startSec=null;
	             String endSec=null;
	             String idAct=null;
	             for(String pattern:configLines){
	                String[] inf=pattern.split(",");
	                if(startSec!=null){
	                	long diff=Long.parseLong(endSec)-Long.parseLong(startSec);
	                	ADLQueue a=new ADLQueue(Integer.parseInt(idAct),diff);
	                	q.add(a);
	                }
	                endSec=inf[0];
	                idAct=inf[1];
	                startSec=endSec.toString();
	             }
	             System.out.println("Finished importing day: "+CurrentFile.getName());
	         } 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}