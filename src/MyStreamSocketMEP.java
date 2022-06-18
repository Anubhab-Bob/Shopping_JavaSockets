// Importing necessary classes
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Takes care of the Services Layer for sending and receiving messages using Stream Sockets
 * @author Anubhab
 *
 */
public class MyStreamSocketMEP extends Socket {	
	
	/**
	 * Socket connecting client and server
	 */
	private Socket socket;
	/**
	 * Input buffer to receive messages 
	 */
	private BufferedReader input;
	/**
	 * Output buffer to send messages
	 */
	private PrintWriter output;
	/**
	 * Timeout duration after which the a client gets disconnected from server
	 */
	private static final int TIMEOUT = 120000;	// timeout = 2 minutes
	
	/**
	 * Constructor to create socket and set input and output streams
	 * @param acceptorHost IP address of the server
	 * @param acceptorPort Port number of the server
	 * @throws SocketException
	 * @throws IOException
	 */
	MyStreamSocketMEP(InetAddress acceptorHost, int acceptorPort) 
	throws SocketException,	IOException {
		socket = new Socket(acceptorHost, acceptorPort);
		socket.setSoTimeout(TIMEOUT);
		setStreams();
	}
	
	/**
	 * Constructor to create socket and set input and output streams
	 * @param socket Socket connecting to server
	 * @throws IOException
	 */
	MyStreamSocketMEP(Socket socket) throws IOException {
		this.socket = socket;
		socket.setSoTimeout(TIMEOUT);
		setStreams();
	}
	
	/**
	 * Sets the input and output streams for the created socket
	 * @throws IOException
	 */
	private void setStreams() throws IOException {
		// get an input stream for reading from the data socket
		InputStream inStream = socket.getInputStream();	
		input =	new BufferedReader(new InputStreamReader(inStream));
		
		OutputStream outStream = socket.getOutputStream();
		// create a PrinterWriter object for character-mode output
		output = new PrintWriter(new OutputStreamWriter(outStream));
	}
	
	/**
	 * Sends a message through the socket
	 * @param message The message to send
	 * @throws IOException
	 */
	public void sendMessage(String message)	throws IOException {
		output.println(message);
		// The flush method call is necessary for the data to be written to
		// the socket data stream before the socket is closed.
		output.flush();
	}
	
	/**
	 * Receives a message through the socket
	 * @return The received message as a string
	 * @throws IOException
	 */
	public String receiveMessage()	throws IOException {
		// read a line from the data stream
		String message = input.readLine();
		return message;
	}
}