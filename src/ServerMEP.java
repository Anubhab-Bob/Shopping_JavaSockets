// Importing necessary classes
import java.net.ServerSocket;

/**
 * Takes care of Presentation Logic for the server
 * @author Anubhab
 *
 */
public class ServerMEP {
	
	public static void main(String[] args) {
		
		int serverPort = 7; 			// default port
		if (args.length == 1)
			serverPort = Integer.parseInt(args[0]);
		try {
			// instantiates a stream socket for accepting connections
			ServerSocket myConnectionSocket = new ServerSocket(serverPort);
			// Shopping server times out after 1 hour
			myConnectionSocket.setSoTimeout(1000 * 60 * 60);
			System.out.println("Shopping Application server ready.");
			while (true) {		// forever loop to accept incoming connections
				// wait to accept a connection
				System.out.println("Waiting for a connection.");
				MyStreamSocketMEP myDataSocket = new MyStreamSocketMEP(myConnectionSocket.accept());
				System.out.println("connection accepted");
				// Start a thread to handle this client's session
				Thread theThread = new Thread(new ServerThread(myDataSocket));
				theThread.start();
				// and go on to the next client
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}