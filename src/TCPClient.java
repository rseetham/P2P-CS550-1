import java.io.*;
import java.net.*;

class TCPClient {

    public static void main(String args[]) {
        byte[] aByte = new byte[1];
        int bytesRead;
        
        int serverPort = (args.length < 1) ? 0 : Integer.parseInt(args[0]);
        int myPeerID = (args.length < 2) ? 0 : Integer.parseInt(args[1]);
    	String serverIP = (args.length < 3) ? null : args[2];
    	String fileOutput = (args.length < 4) ? null : args[3];

        Socket socClient = null;
        InputStream inS = null;

        try {
            socClient = new Socket( serverIP , serverPort );
            inS = socClient.getInputStream();
        } catch (IOException ex) {
        	System.out.println("Server IO exception.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (inS != null) {
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream( fileOutput );
                bos = new BufferedOutputStream(fos);
                bytesRead = inS.read(aByte, 0, aByte.length);

                do {
                        baos.write(aByte);
                        bytesRead = inS.read(aByte);
                } while (bytesRead != -1);

                bos.write(baos.toByteArray());
                if (fos != null) fos.close();
                if (bos != null) {
                	bos.flush();
                	bos.close();
                }
                if (socClient != null) socClient.close();
            } catch (IOException ex) {
            	System.out.println("Client IO exception for file receive");
            }
        }
    }
}