import java.io.*;
import java.net.*;

class TCPServer {

    private final static String fileToSend = "C:\\test1.txt";

    public static void main(String args[]) {

        while (true) {
		    int serverPort = 1100;
            ServerSocket welcomeSocket = null;
            Socket connectionSocket = null;
            BufferedOutputStream outToClient = null;
            System.out.println("Server running at port "+serverPort);
            try {
                welcomeSocket = new ServerSocket(serverPort);
                connectionSocket = welcomeSocket.accept();
                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            } catch (IOException ex) {
            	System.out.println("Server IO exception.");
            }

            if (outToClient != null) {
                File myFile = new File( fileToSend );
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(myFile);
                } catch (FileNotFoundException ex) {
                	System.out.println("Server File Not Found exception.");
                }
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    bis.read(mybytearray, 0, mybytearray.length);
                    outToClient.write(mybytearray, 0, mybytearray.length);
                    outToClient.flush();
                    outToClient.close();
                    connectionSocket.close();

                    // File sent, exit the main method
                    return;
                } catch (IOException ex) {
                	System.out.println("Server IO exception for file send");
                }
            }
        }
    }
}