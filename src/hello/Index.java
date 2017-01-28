package hello;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Index extends Remote {
	
	int getMyPeerID() throws RemoteException;
	
	Boolean registerFile(int peerId, String file_name) throws RemoteException;
	
	String lookUp (String file_name) throws RemoteException;
	
	Boolean removeFile(int peerId, String file_name) throws RemoteException;
	
}
