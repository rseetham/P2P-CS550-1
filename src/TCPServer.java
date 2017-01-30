import java.io.*;
import java.net.*;

class TCPServer {

    public static void main(String args[]) {

        while (true) {
        	System.out.println("Waiting...\n");
            ServerSocket socServer = null;
            Socket socConn = null;
            BufferedOutputStream bos = null;
            
            int serverPort = (args.length < 1) ? 0 : Integer.parseInt(args[0]);
            int myPeerID = (args.length < 2) ? 0 : Integer.parseInt(args[1]);
        	String fileToSend = (args.length < 3) ? null : args[2];
            
            System.out.println("Server running at port "+serverPort);
            try {
                socServer = new ServerSocket(serverPort);
                socConn = socServer.accept();
                bos = new BufferedOutputStream(socConn.getOutputStream());
            } catch (IOException ex) {
            	System.out.println("Server IO exception.");
            }

            if (bos != null) {
            	FileInputStream fis = null;
            	File myFile = new File( fileToSend );
                byte[] fileArr = new byte[(int) myFile.length()];

                try {
                    fis = new FileInputStream(myFile);
                } catch (FileNotFoundException ex) {
                	System.out.println("Server File Not Found exception.");
                }
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    bis.read(fileArr, 0, fileArr.length);
                    bos.write(fileArr, 0, fileArr.length);
                    bos.flush();
                    bos.close();
                    socConn.close();

                    return;
                } catch (IOException ex) {
                	System.out.println("Server IO exception for file send");
                }
            }
        }
    }
}