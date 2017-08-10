package xf.jmockit;


public class PssClient {

	private static Object LOCK = new Object();
	
	private static PssClient instance = null;
	
	private MyObject myObject = new MyObject();
	
	private PssClient(){
		
	}
	
	private PssClient(long time){
		
	}
	
    private static PssClient newInstance()
    {

        synchronized (LOCK)
        {
            if (instance == null)
            {
                instance = new PssClient ();
            }
        }
        return instance;
    }
    
    public static PssClient getInstance(long time){
    	return newInstance();
    }

	public MyObject getMyObject() {
		return myObject;
	}

	public void setMyObject(MyObject myObject) {
		this.myObject = myObject;
	}
}
