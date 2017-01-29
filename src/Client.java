import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author rekha
 *
 */
public class Client {

    /**
     * 
     */
    private Client() {}

    /**
     * @param args serverportno peerid hostport
     * serverportno - port no the peer's port is set up on
     * peerid - peer id if this peer has already been registered
     * hostport - 
     */
    public static void main(String[] args) {

        int serverPort = (args.length < 1) ? 0 : Integer.parseInt(args[0]);
        int myPeerID = (args.length < 2) ? 0 : Integer.parseInt(args[1]);
    	String host = (args.length < 3) ? null : args[2];
    	System.out.println(host);
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            //Hello stub = (Hello) registry.lookup("Hello");
            //String response = stub.sayHello();
            //System.out.println("response: " + response);
            
            System.out.println("abc");
            
            Index index = (Index) registry.lookup("Index");
            System.out.println("abc");
            if (myPeerID != 0) {
            	index.resetIp(myPeerID);
            	index.setServerPort(myPeerID, serverPort);
            }
            else myPeerID = index.registerPeer(serverPort);
            
           
            //int myPeerID = index.registerPeer(serverPort);
            System.out.println(myPeerID);
            
            index.registerFile(myPeerID, "foo1.txt");
            index.registerFile(myPeerID, "foo6.txt");
            index.registerFile(myPeerID, "foo7.txt");
            System.out.println(index.lookUp("foo6.txt"));
            index.removeFile(myPeerID, "foo6.txt");
            System.out.println(index.lookUp("foo6.txt"));
            
            index.deletePeer(myPeerID);
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}