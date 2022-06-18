// Importing necessary classes
import java.util.Arrays;

/**
 * Handles cart object for shopping
 * @author Anubhab
 *
 */
public class Cart {
	
	/**
	 * Item name added to cart
	 */
	private String items[];
	/**
	 * Item price added to cart
	 */
	private int prices[];
	/**
	 * Number of units added to cart for an item
	 */
	private int quantity[];
	/**
	 * Number of items in cart
	 */
	private int item;
	/**
	 * Hard-coded maximum capacity of cart
	 */
	static final int MAXITEMS = 10;
	
	/**
	 * Constructor to initialize cart attributes
	 */
	Cart() {
		items = new String[MAXITEMS];
		prices = new int[MAXITEMS];
		quantity = new int[MAXITEMS];
		Arrays.fill(items, "");
		Arrays.fill(prices, 0);
		Arrays.fill(quantity, 0);
		item = 0;		
	}
	
	/**
	 * Adds an item to the cart
	 * @param it Name of item to add
	 * @param qt Quantity of the item added
	 * @param pr Price of the item added
	 */
	public void addItem(String it, int qt, int pr) {
		// increase quantity only if item already added to cart
		for(int i = 0; i < item; i++) {		
			if (items[i].equals(it)) {
				quantity[i] += qt;
				return;
			}
		}
		// add new item to cart
		items[item] = it;
		prices[item] = pr;
		quantity[item] = qt;
		item++;
	}
	
	/**
	 * Gives the number of items in the cart
	 * @return Number of items in the cart
	 */
	public int getCartSize() {
		return item;
	}
	
	/**
	 * Calculates total price of cart items
	 * @return Total price of cart items
	 */
	public int getCartTotal() {
		int total = 0;
		for(int i = 0; quantity[i] > 0; i++) {
			total += prices[i] * quantity[i];
		}
		return total;
	}
	
	/**
	 * Calculates final discounted amount to pay at checkout
	 * @param lucky Lucky item to be discounted
	 * @return Final discounted amount to pay at checkout
	 */
	public int getFinalAmount(int lucky) {
		int total = getCartTotal();
		lucky--;	// as index starts from 0
		if(lucky <= item) {
			int discount = prices[lucky] * quantity[lucky];
			total -= discount;
		}
		return total;
	}
	
	/**
	 * Prepares a string of the cart items
	 * @return Cart items and total price as a string
	 */
	public String showCart() {
		String contents = "";
		int total = 0;
		for(int i = 0; quantity[i] > 0; i++) {
			contents += items[i] + " x " + quantity[i] + "\n";
			total += prices[i] * quantity[i];
		}
		contents += "Total = \tRs. " + total;
		return contents;
	}
}