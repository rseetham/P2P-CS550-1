import java.io.File;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;


public class Client {

    /**
     * 
     */
    private Client() {}
    
    public static int myPeerID;
    
    public static Index index;
    
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