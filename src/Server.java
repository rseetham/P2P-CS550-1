import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.rmi.server.RemoteServer;
       
public class Server implements Index{
        

    public Server() {}
        
    /**
     * Map of the <peer id -> Peer object>
     */
    private static Map<Integer, Peer> peers =  Collections.synchronizedMap( new HashMap<Integer, Peer>());
       
    /**
     * Map of the <file_name -> list of peer ids containing the file>
     */
    private static Map<String, ArrayList<Integer>> filesIndex =  Collections.synchronizedMap( new HashMap<String, ArrayList<Integer>>());
    
    
    /**
     * Used to generate peer ids
     */
    private static final AtomicInteger sequence = new AtomicInteger();
    
    /** Hosts the registry at port 1099 (default)
     * 
     */
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            
            Index index = (Index) UnicastRemoteObject.exportObject(obj, 0);
                        
            Registry registry = LocateRegistry.getRegistry();
            
            registry.bind("Index", index);            
            
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    /** Used to generate peer ids
     * @return next peer id
     */
    private static int next() {
        return sequence.incrementAndGet();
    }


	/* (non-Javadoc)
	 * @see Index#registerPeer(int)
	 */
	@Override
	public int registerPeer(int serverPort) throws RemoteException {
		try{
		    String clientIp = RemoteServer.getClientHost();		
		    int peerId = next();
		    // adds peer to peers
		    peers.put(peerId, new Peer(peerId, clientIp, serverPort));
		    System.out.println("registered peer : "+ peerId);
		    return peerId;
		}	
		catch(Exception e){System.out.println(e);}
		return 0;
	}

	/* (non-Javadoc)
	 * @see Index#registerFile(int, java.lang.String)
	 */
	@Override
	public boolean registerFile(int peerId, String file_name) throws RemoteException {
		try{
			// Add file to the peer objects index
			Peer peer = peers.get(peerId);
			peer.addFile(file_name);
			// Add file to filesIndex
			// if file is present in filesIndex add peer to list
			if (filesIndex.containsKey(file_name)){
				ArrayList<Integer> list = filesIndex.get(file_name);
				// Add file if peer is not already present as having the file
				if (!list.contains(peerId))
					filesIndex.get(file_name).add(peerId);
				System.out.println("Registered File - file : "+ file_name + " on peer : "+ peerId);
				return true;
			}
			// if file is not present in filesIndex create list and add peer to list
			ArrayList<Integer> al = new ArrayList<Integer>();
			al.add(peerId);
			filesIndex.put(file_name, al);
			System.out.println("Registered File - file : "+ file_name + " on peer : "+ peerId);
			return true;			
		}
		catch(Exception e){System.out.println(e);}
		return false;
	}

	/* (non-Javadoc)
	 * @see Index#lookUp(java.lang.String)
	 */
	@Override
	public String lookUp(String file_name) throws RemoteException {
		if (filesIndex.containsKey(file_name)){
			// Returns a random peer server ip that has the file by checking fileIndex
			ArrayList<Integer> peerlist = filesIndex.get(file_name);
			int randomPeer = ThreadLocalRandom.current().nextInt(peerlist.size());			
			return peers.get(peerlist.get(randomPeer)).getServerIp();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see Index#removeFile(int, java.lang.String)
	 */
	@Override
	public boolean removeFile(int peerId, String file_name) throws RemoteException {
		try{
			// Removes file from peer object
			Peer peer = peers.get(peerId);
			peer.removeFile(file_name);
			// Records that the file isn't in the peer anymore
			if (filesIndex.containsKey(file_name)){
				ArrayList<Integer> peerlist = filesIndex.get(file_name);
				peerlist.remove(new Integer(peerId));				
				if (peerlist.isEmpty()) filesIndex.remove(file_name);
				System.out.println("Removed File "+ file_name + " from "+ peerId);
				return true;
			    //System.out.println(filesIndex);
			    //System.out.println(peerlist);
			}			
		}
		catch(Exception e){System.out.println(e);}
		return false;
	}


	/* (non-Javadoc)
	 * @see Index#setServerPort(int, int)
	 */
	@Override
	public boolean setServerPort(int peerid, int serverPort) throws RemoteException {
		peers.get(peerid).setServerPort(serverPort);
		return true;
	}

	/* (non-Javadoc)
	 * @see Index#resetIp(int)
	 */
	@Override
	public boolean resetIp(int peerId) throws RemoteException {
		try {
			String clientIp = RemoteServer.getClientHost();
			peers.get(peerId).setIp(clientIp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see Index#deletePeer(int)
	 */
	@Override
	public boolean deletePeer(int peerId) throws RemoteException {
		peers.remove(peerId);
		filesIndex.values().forEach((list) -> list.remove(new Integer(peerId)));	
		filesIndex.values().removeIf(Objects::isNull);
		System.out.println("Deleted peer "+peerId);
		return true;
	}

	@Override
	public ArrayList<String> getFilesList(int peerId) throws RemoteException {
		return peers.get(peerId).getFilesList();
	}
}
