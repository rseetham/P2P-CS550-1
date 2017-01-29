import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Index extends Remote {
	

	/** Register peer with the port of the server
	 * @param serverPort : port that server is hosted on
	 * @return peer ID
	 * @throws RemoteException
	 */
	int registerPeer(int serverPort) throws RemoteException;
	
	/** Sets the server port 
	 * @param peerI̋d 
	 * @param serverPort : : port that server is hosted on
	 * @return true if success
	 * @throws RemoteException
	 */
	boolean setServerPort(int peerI̋d, int serverPort) throws RemoteException;
	
	/** Resets ip address of the peer represented by peerId to the current IP
	 * @param peerId
	 * @return true if success
	 * @throws RemoteException
	 */
	boolean resetIp(int peerId) throws RemoteException;
	
	/** Registers file with file_name 
	 * @param peerId
	 * @param file_name : name of the file at the peerId
	 * @return true if success
	 * @throws RemoteException
	 */
	boolean registerFile(int peerId, String file_name) throws RemoteException;
	
	/** Finds a peer with the file needed
	 * @param file_name file name of the file needed
	 * @return Server ip of the peer that has the file
	 * @throws RemoteException
	 */
	String lookUp (String file_name) throws RemoteException;
	
	/** Removed the file from lookUp. Should be called if file is no longer available
	 * @param peerId 
	 * @param file_name file name of the file to remove
	 * @return true if success
	 * @throws RemoteException
	 */
	boolean removeFile(int peerId, String file_name) throws RemoteException;
	
	/** Deletes peer from the index
	 * @param peerId
	 * @return true if success
	 * @throws RemoteException
	 */
	boolean deletePeer(int peerId) throws RemoteException;

}
