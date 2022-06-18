// Importing necessary classes
import java.net.SocketTimeoutException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Handles threads for the server
 * @author Anubhab
 *
 */
class ServerThread implements Runnable {
	
	/**
	 * Message string to terminate connection with client
	 */
	static final String endMessage = ".";
	/**
	 * Message string to start communication with client
	 */
	static final String greetMessage = "Hello";
	/**
	 * Message string to send for improper message received
	 */
	static final String errorMessage = "Send proper message!";
	/**
	 * List of items in the catalog
	 */
	static String items[];
	/**
	 * Details of items in the catalog
	 */
	static String itemDetails[];
	/**
	 * Prices of items in the catalog
	 */
	static int prices[];
	/**
	 * Hard-coded number of items
	 */
	static final int MAXITEMS = 5;
	/**
	 * Hard-coded number of options
	 */
	static final int OPTION_COUNT = 4;
	/**
	 * Socket to communicate with client
	 */
	MyStreamSocketMEP myDataSocket;
	
	/**
	 * Constructor to initialize socket and inventory
	 * @param myDataSocket Socket connecting server with client
	 */
	ServerThread(MyStreamSocketMEP myDataSocket) {
		this.myDataSocket = myDataSocket;
		items = new String[MAXITEMS];
		itemDetails = new String[MAXITEMS];
		prices = new int[MAXITEMS];
		
		// hard-coded initialization of inventory items and corresponding information
		items[0] = "Gel Pen";
		items[1] = "Ball Pen";
		items[2] = "Pencil Box";
		items[3] = "Notebook";
		items[4] = "Scale";
		itemDetails[0] = "With smooth and fast writing Japanese waterproof ink which gives you a smudge-proof experience";
		itemDetails[1] = "Elasto Grip for Pressure Free Writing with Swiss Metal Clip 0.5mm Nickle Silver";
		itemDetails[2] = "Hard-build quality with double layerd storage for separating pens and pencils";
		itemDetails[3] = "Notebooks with twin-wire winding restricting to keep your notes together";
		itemDetails[4] = "Durable transparent build for clear view with round endges to keep fingers safe";
		prices[0] = 10;
		prices[1] = 4;
		prices[2] = 35;
		prices[3] = 30;
		prices[4] = 5;
	}
	
	/**
	 * Prepares a string of options
	 * @return The string containing all menu options
	 */
	private String showOptions() {
		// hard-coded options list
		String op1 = "Option 1: Get Items' List\n";
		String op2 = "Option 2: Choose Item and Quantity\n";
		String op3 = "Option 3: Show Cart\n";
		String op4 = "Option 4: Checkout";
		String options = op1 + op2 + op3 + op4;
		return options;
	}
	
	/**
	 * Prepares a string of all items in the catalog
	 * @return The string containing all item
	 */
	private String showItems() {
		String its = "";
		for(int i = 0; i < items.length; i++) {
			if(i != (items.length - 1))
				its += "Item " + (i+1) + "\t" +  items[i] + " - Rs. " + prices[i] + "\n";
			else
				its += "Item " + (i+1) + "\t" +  items[i] + " - Rs. " + prices[i];
		}
		return its;
	}
	
	/**
	 * Prepares a string of an item with corresponding details
	 * @param i Item to display
	 * @return Item details
	 */
	private String showItemDetails(int i) {
		String det = "Item " + i + " : " + items[i-1] + " - " + itemDetails[i-1] +  " @ Rs. " + prices[i-1];
		return det;
	}
	
	/**
	 * Checks if a given string contains only numeric characters
	 * @param str The string to check
	 * @return True if str is numeric, False otherwise
	 */
	private boolean isNumeric(String str) {
	    for (char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) 
	        	return false;
	    }
	    return true;
	}
	
	@Override
	public void run() {
		boolean done = false;
		String message;
		Cart newCart = new Cart();	// create a new Cart object for the client
		try {
			while (!done) {		// loop until client ends session or there is a timeout
				message = myDataSocket.receiveMessage().trim();
				System.out.println("message received: " + message); 
				if (message.equals(endMessage)) {
					// Session over; close the data socket.
					System.out.println("Session over.");
					myDataSocket.close();
					done = true;
				}
				else if (message.equals(greetMessage)) {
					// Session begins
					myDataSocket.sendMessage(Integer.toString(OPTION_COUNT));
					myDataSocket.sendMessage(showOptions());
				}
				else if(message.charAt(0) == 'I') {		// items
					// perform server-side validation checks
					if(!Character.isDigit(message.charAt(message.length() - 1))) {
						myDataSocket.sendMessage(errorMessage );
						continue;
					}
					else if(Character.getNumericValue(message.charAt(message.length() - 1)) <= 0 ||
							Character.getNumericValue(message.charAt(message.length() - 1)) > items.length) {
						myDataSocket.sendMessage(errorMessage );
						continue;
					}
					
					System.out.println(message.length() + "\t" + Character.getNumericValue(message.charAt(message.length() - 1)));
					String res = showItemDetails(Character.getNumericValue(message.charAt(message.length() - 1)));
					System.out.println(res);
					myDataSocket.sendMessage(res);
				}
				else if(message.charAt(0) == 'O') {		// options
					String choice = message;
					switch(choice) {		// perform actions based on operation chosen by client
					case "Op1" :		// show items in the catalog
						myDataSocket.sendMessage(Integer.toString(items.length));
						myDataSocket.sendMessage(showItems());
						break;
					case "Op2" :		// add items to cart
						myDataSocket.sendMessage(Integer.toString(items.length));
						myDataSocket.sendMessage(showItems());
						String bought[] = myDataSocket.receiveMessage().trim().split("\\s"); // identify item and quantity
						// perform server-side validation checks
						if (bought.length != 2) {
							myDataSocket.sendMessage(errorMessage);
							break;
						}
						if (!isNumeric(bought[0]) || !isNumeric(bought[1])) {
							myDataSocket.sendMessage(errorMessage);
							break;
						}
						
						System.out.println(bought[0] + "\t" + bought[1]);
						// Now add item to cart
						int index = Integer.parseInt(bought[0]) - 1;
						if(index < 0 || index >= items.length) {	// check if index is valid
							myDataSocket.sendMessage(errorMessage);
						}
						else {
							newCart.addItem(items[index], Integer.parseInt(bought[1]), prices[index]);
							myDataSocket.sendMessage("Item added to cart!");
						}
						break;
					case "Op3" :		// show cart contents
						myDataSocket.sendMessage(Integer.toString(newCart.getCartSize()));
						if(newCart.getCartSize() > 0)
							myDataSocket.sendMessage(newCart.showCart());
						break;
					case "Op4" :		// get discount and checkout client
						// Use RMI to calculate cost
						try {
							// send the cart
							myDataSocket.sendMessage(Integer.toString(newCart.getCartSize()));
							if(newCart.getCartSize() > 0)
								myDataSocket.sendMessage(newCart.showCart());
							// Getting the registry
							Registry registry = LocateRegistry.getRegistry(null);
							// Looking up the registry for the remote object
							Lottery stub = (Lottery) registry.lookup("Lottery");
							int luckyItem = stub.getLuckyItem();
							int totalPrice = newCart.getCartTotal();
							int checkoutPrice = newCart.getFinalAmount(luckyItem);
							String discount = "Your discount is Rs. " + (totalPrice - checkoutPrice);
							String pay = "You need to pay Rs. " + checkoutPrice;
							Thread.sleep(2000);
							myDataSocket.sendMessage(discount);
							myDataSocket.sendMessage(pay);
						}
						catch(Exception e){
							System.out.println("Exception caught in thread: " + e);
						}
						break;
					default :
						myDataSocket.sendMessage(errorMessage);
						break;
					}
				}
				else
					myDataSocket.sendMessage(errorMessage);
				}
		}
		catch (SocketTimeoutException s) {
			try {
				System.out.println("Exception caught in thread: " + s);
				// inform client that session has timed out
				myDataSocket.sendMessage("Your connection timed out!");
			}
			catch (Exception ex) {
				System.out.println("Exception caught in thread: " + ex);
			} 
		}
		catch (Exception ex) {
			System.out.println("Exception caught in thread: " + ex);
		}
	}
}
