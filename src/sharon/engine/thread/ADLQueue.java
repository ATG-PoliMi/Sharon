package sharon.engine.thread;

public class ADLQueue {

	private int id;
	private long time;
    
    public ADLQueue(int id, long usedTime){
        this.id=id;
        this.time=usedTime;
    }
 
    public int getADLId() {
        return id;
    }    
    public long getTime() {
        return time;
    }
}
