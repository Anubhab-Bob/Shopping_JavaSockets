// Importing necessary classes

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server that creates and binds remote object to RMI Registry
 * @author Anubhab
 *
 */
public class RMIServer {
	
	public static void main(String args[]){
		try {
			Lottery stub = new RemoteLottery();
			// Binding the remote object (stub) in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Lottery", stub);
			System.out.println("Server ready ...");
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}

