// Importing necessary classes
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Takes care of the Presentation layer for the Client
 * @author Anubhab
 *
 */
public class ClientMEP {
	
	/**
	 * Message string to terminate connection with server
	 */
	static final String endMessage = ".";
	/**
	 * Message string to start communication with server
	 */
	static final String greetMessage = "Hello";
	/**
	 * The number of options in the main menu
	 */
	static private int opCount;
	
	public static void main(String[] args) {
		// BufferedReader object to read input from console
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Welcome to Shopping Application.\n" +
					"What is the name of the server host for the Application? (keep blank if unsure)");
			String hostName = br.readLine();
			if (hostName.length() == 0) 	// if user did not enter a name
				hostName = "localhost"; 	// use the default host name
			
			System.out.println("What is the port number of the server host? (keep blank if unsure)");
			String portNum = br.readLine();
			if (portNum.length() == 0)  	// if user did not enter a port number
				portNum = "7"; 				// use the default port number
			
			ClientHelper helper = new ClientHelper(hostName, portNum);	// create helper object
			
			System.out.println("Type \"Hello\" to connect to server or enter a single period to quit.");
			
			// declare necessary variables
			boolean done = false, greeted = false, listShown = false;
			String message;
			
			while (!done) {		// iterate communication with server
				message = br.readLine();
				
				if(message.trim().equalsIgnoreCase("B")) {		// come back to main menu
					listShown = false;
					greeted = false;
					message = greetMessage;
				}
				if ((message.trim()).equals (endMessage)){		// terminate connection with server
					done = true;
					helper.done();
				}
				else if(listShown) {							// show item details
					String it = message.trim();		// receive item number
					// perform client-side validation checks
					if(it.length() != 1) {
						System.out.println("Wrong input given! Enter B to go back to previous menu.");
						continue;
					}
					else if(!Character.isDigit(it.charAt(0))) {
						System.out.println("Wrong input given! Enter B to go back to previous menu.");
						continue;
					}
					
					helper.send("It" + it);			// send item number to server
					System.out.println(helper.receive());	// receive item details
					System.out.println("Choose Item No. for more details.\n" +
							"Enter B to go to previous menu.");					
				}
				else if(greeted) {								// choose option from menu
					String op = message.trim();
					// perform client-side validation checks
					if(op.length() != 1) {
						System.out.println("Wrong input given! Enter B to go back to previous menu.");
						continue;
					}
					else if(!Character.isDigit(op.charAt(0))) {
						System.out.println("Wrong input given! Enter B to go back to previous menu.");
						continue;
					}
					else if(Character.getNumericValue(op.charAt(0)) < 1 || 
							Character.getNumericValue(op.charAt(0)) > opCount) {
						System.out.println("Wrong input given! Enter B to go back to previous menu.");
						continue;
					}
					
					helper.send("Op" + op);		// send operation if it is valid
					
					switch (op) {				// handle receiving based on operation sent
					case "1" :
						// Show list of items to customer
						helper.receiveList();
						System.out.println("Choose Item No. for more details.\n" +
								"Enter B to go to previous menu.");
						listShown = true;
						break;
				
					case "2" :
						// Add item to cart
						helper.receiveList();
						System.out.println("Enter Item No. <Space> Quantity");
						String bought = br.readLine();
						helper.send(bought);
						System.out.println(helper.receive());
						System.out.println("Enter B to go back to previous menu.");	
						break;
						
					case "3" : 
						// Show cart
						System.out.println("Your cart contains - ");
						helper.receiveCart();
						System.out.println("Enter B to go back to previous menu.");
						break;
						
					case "4": 
						// Receive amount to pay
						System.out.println("Proceeding to checkout ...");
						System.out.println("Your cart contains - ");
						helper.receiveCart();
						helper.receiveCheckout();	
						done = true;		// terminate session
						helper.done();
						break;
					}
					
				}
				else if ((message.trim().equalsIgnoreCase(greetMessage))) {	
					// initiate communication protocol
					helper.send(greetMessage);
					opCount = helper.receiveOptions();
					System.out.println("Or enter a single period to quit.");
					greeted = true;
				}
				else {
					helper.send(message);
					System.out.println(helper.receive());
				}
			} // end while
		} // end try
		catch (Exception ex) {
			System.out.println("Error : " + ex);
		} //end catch
	}
}
