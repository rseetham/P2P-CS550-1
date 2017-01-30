import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class peerDS {
	int peerID;
	int clientPort;
	int serverPort;
	String peerIP;
	String directoryName = "C:\\"+peerID+"\\";
	ArrayList<String> oldFileList;

	public void retrive(String filename){
		String fileOutput = directoryName+filename;

		byte[] aByte = new byte[1];
		int bytesRead;

		Socket clientSocket = null;
		InputStream is = null;

		try {
			clientSocket = new Socket( peerIP , serverPort );
			is = clientSocket.getInputStream();
		} catch (IOException ex) {
			System.out.println("Client Retrieve IO exception.");
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

	}

	//default constructor
	public peerDS(){
		this.peerID = 0;
		clientPort = 1111;
		serverPort = 2222;
		this.peerIP = "";
	}

	//constructor
	public peerDS(int pID, int cPort, int sPort, String pIP){
		this.peerID = pID;
		clientPort = cPort;
		serverPort = sPort;
		this.peerIP = pIP;
	}
	
	public void updateFilelist(String directoryName){//on internal deletes, let server know to update its registry
		Server myServer = new Server();
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        boolean fileExists = false;
        for (File file : fList){
            if (file.isFile()){
            	if(oldFileList.contains(file.getName())==false){
            		try {
						myServer.removeFile(peerID, file.getName());
					} catch (RemoteException e) {
						System.out.println("File Removal Error");
					}
            	}
            }
        }
        
        //update the file list to current one
        for (File file : fList){
            if (file.isFile()){
            	//output list of file names in arraylist format that can be sent to server
                System.out.println(file.getName());
                oldFileList.add(file.getName());
            }
        }
	}//end of updateFilelist

}
