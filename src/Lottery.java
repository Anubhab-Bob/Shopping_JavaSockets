// Importing necessary classes
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote Interface for calculating the discount
 * @author Anubhab
 *
 */
public interface Lottery extends Remote {
	/**
	 * Gives the winning item in Lucky Draw 
	 * @return The winning item in Lucky Draw 
	 * @throws RemoteException
	 */
	int getLuckyItem() throws RemoteException;
}

