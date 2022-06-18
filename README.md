# Shopping_JavaSockets
A multi-client-server based shopping cart application by using Java Sockets API and discount calculation using Java RMI.


This project is a client-server based shopping application using Java Sockets API.
Stream sockets are used in this implementation.

Multiple clients can connect to the shopping server by sending a "Hello" in parallel 
and ask for different services.
The application uses 'localhost' as default server name and also allows connecting 
to a default port if the port number on which the server is running is not known by 
client.
Each client can disconnect manually from the server by sending a period (.), otherwise 
automatic disconnections occur after the client performs checkout or after 2 minutes 
of inactivity.

On connecting with the server, each client gets the option to - 
1. Receive the list of available items
	1.1. Once the list is received, client can choose a specific 
		 item from the list for more details of the item.
2. Add an item to cart in a specific quantity
3. View the cart at any instant of shopping
4. Checkout to get final amount to pay and terminate the application

After choosing any of the above operations, the client has to send the character 'B' or 
'b' to go back to the main menu.

The checkout procedure uses an RMI service to calculate a discount based on a lottery mechanism. 

The program handles all kinds of incorrect and abnormal messages by directing clients 
towards appropriate messages for proper communication. 

N.B. - This project primarily handles the communication part of the shopping application.
	   The inventory management part is beyond the scope of this project. So, inventory 
	   details along with main menu options are hard-coded at the server side.
