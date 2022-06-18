// Importing necessary classes
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;

/**
 * Takes care of the Application Layer for the client
 * @author Anubhab
 *
 */
public class ClientHelper {
	
	/**
	 * Message string to terminate connection with server
	 */
	static final String endMessage = ".";
	/**
	 * Socket connecting client and server
	 */
	private MyStreamSocketMEP mySocket;
	/**
	 * IP Address of the server
	 */
	private InetAddress serverHost;
	/**
	 * Port number of the server
	 */
	private int serverPort;
	
	/**
	 * Constructor to create socket and request connection with server
	 * @param hostName Name of the server host
	 * @param portNum Port number running the server
	 * @throws SocketException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	ClientHelper(String hostName, String portNum) 
	throws SocketException, UnknownHostException, IOException {
		this.serverHost = InetAddress.getByName(hostName);
		this.serverPort = Integer.parseInt(portNum);
		//Instantiates a stream-mode socket and wait for a connection.
		this.mySocket = new MyStreamSocketMEP(this.serverHost, this.serverPort);
		System.out.println("Connection request made");
	} // end constructor
	
	/**
	 * Send message to the server
	 * @param message Message to send to the server
	 * @throws SocketException
	 * @throws IOException
	 */
	public void send( String message) throws SocketException, IOException {
		mySocket.sendMessage(message);
	}
	
	/**
	 * Receive a single message from server
	 * @return The received message from the server
	 * @throws SocketException
	 * @throws IOException
	 */
	public String receive() throws SocketException,
	IOException {
		String response = mySocket.receiveMessage();
		return response;
	}
	
	/**
	 * Receive checkout details from the server
	 * @throws SocketException
	 * @throws IOException
	 */
	public void receiveCheckout() throws SocketException, IOException {
		String response = mySocket.receiveMessage();	// receive discount
		System.out.println(response);
		response = mySocket.receiveMessage();	// receive final amount
		System.out.println(response);
	}
	
	/**
	 * Receive menu options from the server
	 * @return The number of options
	 * @throws SocketException
	 * @throws IOException
	 */
	public int  receiveOptions() throws SocketException, IOException {
		// receive the number of options in the menu
		int opSize = Integer.parseInt(mySocket.receiveMessage().trim());
		for(int i = 0; i < opSize; i++) {	// receive individual options
			String response = mySocket.receiveMessage();
			System.out.println(response);
		}
		return opSize;
	}
	
	/**
	 * Receives Item list from the server
	 * @throws SocketException
	 * @throws IOException
	 */
	public void receiveList() throws SocketException, IOException {
		// receive the number of items in the list
		int listSize = Integer.parseInt(mySocket.receiveMessage().trim());
		for(int i = 0; i < listSize; i++) {	// receive individual items
			String response = mySocket.receiveMessage();
			System.out.println(response);
		}
	}
	
	/**
	 * Receive cart details from the server
	 * @throws SocketException
	 * @throws IOException
	 */
	public void receiveCart() throws SocketException, IOException {
		// get cart size
		int listSize = Integer.parseInt(mySocket.receiveMessage().trim());
		String response;
		if(listSize == 0) {
			System.out.println("Your cart is empty!");
			return;
		}
		for(int i = 0; i < listSize; i++) {		// get cart elements
			response = mySocket.receiveMessage();
			System.out.println(response);
		}
		response = mySocket.receiveMessage();	// get total price
		System.out.println(response);
	}
	
	/**
	 * Terminate connection with server
	 * @throws SocketException
	 * @throws IOException
	 */
	public void done() throws SocketException, IOException{
		mySocket.sendMessage(endMessage);
		mySocket.close( );
	}
}