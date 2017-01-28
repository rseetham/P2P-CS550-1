package hello;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.rmi.server.RemoteServer;
       
public class Server implements Hello, Index{
        
    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }
        
    private HashMap<Integer, Peer> peers =  new HashMap<Integer, Peer>();
    
    private HashMap<String, Integer> peerids =  new HashMap<String, Integer>();
    
    private HashMap<String, ArrayList<Integer>> filesIndex =  new HashMap<String, ArrayList<Integer>>();
    
    private static final AtomicInteger sequence = new AtomicInteger();
    
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
            
            Index index = (Index) UnicastRemoteObject.exportObject(obj, 0);
                        
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello", stub);
            
            registry.bind("Index", index);
            
            
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private static int next() {
        return sequence.incrementAndGet();
    }

	@Override
	public int getMyPeerID() throws RemoteException {
		try{
		    String clientIp = RemoteServer.getClientHost();		
		    if(peerids.containsKey(clientIp))
		    	return peerids.get(clientIp);
		    int peerId = next();
		    peerids.put(clientIp, peerId);
		    peers.put(peerId, new Peer(peerId, clientIp));
		    return peerId;
		}	
		catch(Exception e){System.out.println(e);}
		return 0;
	}

	@Override
	public Boolean registerFile(int peerId, String file_name) throws RemoteException {
		try{
			Peer peer = peers.get(peerId);
			peer.addFile(file_name);
			if (filesIndex.containsKey(file_name)){
				filesIndex.get(file_name).add(peerId);
				return true;
			}
			ArrayList<Integer> al = new ArrayList<Integer>();
			al.add(peerId);
			filesIndex.put(file_name, al);
			return true;			
		}
		catch(Exception e){System.out.println(e);}
		return false;
	}

	@Override
	public String lookUp(String file_name) throws RemoteException {
		if (filesIndex.containsKey(file_name)){
			ArrayList<Integer> peerlist = filesIndex.get(file_name);
			int randomPeer = (int) (Math.random() * peerlist.size());
			return peers.get(peerlist.get(randomPeer)).ip;
		}
		return null;
	}

	@Override
	public Boolean removeFile(int peerId, String file_name) throws RemoteException {
		try{
			Peer peer = peers.get(peerId);
			peer.removeFile(file_name);
			if (filesIndex.containsKey(file_name)){
				ArrayList<Integer> peerlist = filesIndex.get(file_name);
				Iterator<Integer> it = peerlist.iterator();
				while(it.hasNext()){
					if (it.next().equals(peerId)){
						peerlist.remove(peerId);
						return true;
					}
				} 
			}
			return true;			
		}
		catch(Exception e){System.out.println(e);}
		return false;
	}
}
