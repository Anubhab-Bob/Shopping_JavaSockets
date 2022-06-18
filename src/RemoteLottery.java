// Importing necessary classes
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implements the remote interface
 * @author Anubhab
 *
 */
public class RemoteLottery extends UnicastRemoteObject implements Lottery {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 * @throws RemoteException
	 */
	protected RemoteLottery() throws RemoteException {
		super();
	}

	@Override
	public int getLuckyItem() throws RemoteException {
		int max = 4;
		int min = 2;
		return (int) ((Math.random() * (max - min)) + min);
	}

}

