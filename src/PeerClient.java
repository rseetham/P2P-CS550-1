import java.io.*;
import java.net.*;
import java.io.File;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PeerClient {
	
	   private PeerClient() {}
	    
	    public static int myPeerID;
	    
	    public static Index index;
	    
	    static Random rand;
	    
	    private final static String serverIP = "127.0.0.1";
	    private final static int serverPort = 3248;
	    private final static String fileOutput = "C:\\testout.pdf";
	    
	    /**
	     * @param args serverportno peerid hostport
	     * serverportno - port no the peer's port is set up on
	     * peerno - for tests to determine which files to ask for
	     * peerid - peer id if this peer has already been registered
	     * hostport - 
	     */
	    public static void main(String[] args) {

	        int serverPort = (args.length < 1) ? 0 : Integer.parseInt(args[0]);
	        int peerno = (args.length < 2) ? null : Integer.parseInt(args[1]);
	        myPeerID = (args.length < 3) ? 0 : Integer.parseInt(args[2]);
	    	String host = (args.length < 4) ? null : args[3];
	    	System.out.println(host);
	        try {
	            Registry registry = LocateRegistry.getRegistry(host);
	            
	            System.out.println("abc");
	            
	            index = (Index) registry.lookup("Index");
	            System.out.println("abc");
	            if (myPeerID != 0) {
	            	index.resetIp(myPeerID);
	            	index.setServerPort(myPeerID, serverPort);
	            }
	            else myPeerID = index.registerPeer(serverPort);
	            
	            rand = new Random();
	            
	            ArrayList<String> fLookUp = filesToLookUp(peerno);
	            
	            setUpFiles();
	            
	            sameLookUpTest(fLookUp);
	            //runningTest();
	            //index.deletePeer(myPeerID);
	            
			    
	            
	            
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	        
	    }
	    
	    /**determine the files that this peer can look up
	     * @param peerno used to determine the files it can look up
	     * @return list of 
	     */
	    private static ArrayList<String> filesToLookUp(int peerno) {
	    	ArrayList<String> files = new ArrayList<String>(23);
	    	files.addAll(Arrays.asList("text_3.txt", "text_4.txt", "text_5.txt", "text_6.txt"));
	    	ArrayList<Integer> peers = (ArrayList<Integer>) Arrays.asList(1,2,3);
	    	peers.remove(new Integer(peerno));
	    	peers.forEach(no -> {
	    		for (int i = 0; i <= 9; i ++)
	    			files.add("text_"+no+""+i+".txt");
	    	});
			return files;
		}

		/**
		 * Registers all the files in the TestFiles dir of the client
		 */
		static void setUpFiles(){
	    	File[] files = new File(Paths.get("../TestFiles/").toString()).listFiles();
			for (File file : files) {
			    if (file.isFile()) {
			        System.out.println(file.getName());
			        try {
						index.registerFile(myPeerID, file.getName());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
			    }
			}
	    }
	    
	    
	    static void sameLookUpTest(ArrayList<String> files) throws RemoteException{
	    	
	    	int l = files.size();
	    	String file = files.get(rand.nextInt(l));
	    	long startTime = System.nanoTime();
	    	for (int i  = 0; i < 1000; i++){
	    		index.lookUp(file);
	    	}
	    	long estimatedTime = System.nanoTime() - startTime;	    	
	    	System.out.println("Same Look up "+estimatedTime);
	    }
	    
	    static void diffLookUpTest(ArrayList<String> files) throws RemoteException{
	    	
	    	int l = files.size();
	    	long startTime = System.nanoTime();
	    	for (int i  = 0; i < 1000; i++){
	    		index.lookUp(files.get(rand.nextInt(l)));
	    	}
	    	long estimatedTime = System.nanoTime() - startTime;	    	
	    	System.out.println(" Different Look up "+estimatedTime);
	    }
	    
	    static void runningTest () throws RemoteException{
	    	System.out.println(myPeerID);
            
            index.registerFile(myPeerID, "foo1.txt");
            index.registerFile(myPeerID, "foo6.txt");
            index.registerFile(myPeerID, "foo7.txt");
            System.out.println(index.lookUp("foo6.txt"));
            index.removeFile(myPeerID, "foo6.txt");
            System.out.println(index.lookUp("foo6.txt"));
            
            index.deletePeer(myPeerID);
	    }
	

}
