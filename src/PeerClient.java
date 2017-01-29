import java.io.*;
import java.net.*;
import java.io.File;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class PeerClient {
	
	   private PeerClient() {}
	    
	    public static int myPeerID;
	    
	    public static Index index;
	    
	    private final static String serverIP = "127.0.0.1";
	    private final static int serverPort = 3248;
	    private final static String fileOutput = "C:\\testout.pdf";
	    
	    /**
	     * @param args serverportno peerid hostport
	     * serverportno - port no the peer's port is set up on
	     * peerid - peer id if this peer has already been registered
	     * hostport - 
	     */
	    public static void main(String[] args) {

	        int serverPort = (args.length < 1) ? 0 : Integer.parseInt(args[0]);
	        myPeerID = (args.length < 2) ? 0 : Integer.parseInt(args[1]);
	    	String host = (args.length < 3) ? null : args[2];
	    	System.out.println(host);
	        try {
	            Registry registry = LocateRegistry.getRegistry(host);
	            //Hello stub = (Hello) registry.lookup("Hello");
	            //String response = stub.sayHello();
	            //System.out.println("response: " + response);
	            
	            System.out.println("abc");
	            
	            index = (Index) registry.lookup("Index");
	            System.out.println("abc");
	            if (myPeerID != 0) {
	            	index.resetIp(myPeerID);
	            	index.setServerPort(myPeerID, serverPort);
	            }
	            else myPeerID = index.registerPeer(serverPort);
	            
	            setUpFiles();
	            
	            index.deletePeer(myPeerID);
	           
	            /*
	            //int myPeerID = index.registerPeer(serverPort);
	            System.out.println(myPeerID);
	            
	            index.registerFile(myPeerID, "foo1.txt");
	            index.registerFile(myPeerID, "foo6.txt");
	            index.registerFile(myPeerID, "foo7.txt");
	            System.out.println(index.lookUp("foo6.txt"));
	            index.removeFile(myPeerID, "foo6.txt");
	            System.out.println(index.lookUp("foo6.txt"));
	            
	            index.deletePeer(myPeerID);
	            
	            */
	            
			    
			    byte[] aByte = new byte[1];
		        int bytesRead;

		        Socket clientSocket = null;
		        InputStream is = null;

		        try {
		            clientSocket = new Socket( serverIP , serverPort );
		            is = clientSocket.getInputStream();
		        } catch (IOException ex) {
		        	System.out.println("Server IO exception.");
		        }

		        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		        if (is != null) {

		            FileOutputStream fos = null;
		            BufferedOutputStream bos = null;
		            try {
		                fos = new FileOutputStream( fileOutput );
		                bos = new BufferedOutputStream(fos);
		                bytesRead = is.read(aByte, 0, aByte.length);

		                do {
		                        baos.write(aByte);
		                        bytesRead = is.read(aByte);
		                } while (bytesRead != -1);

		                bos.write(baos.toByteArray());
		                bos.flush();
		                bos.close();
		                clientSocket.close();
		            } catch (IOException ex) {
		            	System.out.println("Client IO exception for file receive");
		            }
		        }
	            
	            
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	        
	    }
	    
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
	

}
