import java.net.InetAddress;

/**
 * Creates list of users connected to server
 * 
 * @author Piotr Wrona
 * @version 1.1
 *
 */
public class ServerGuest {
	
	String guestNick;
	InetAddress ipInput;						
	int portInput; 
	
	/**
	 * Constructs user connected to server
	 * 
	 * @param guestNick users nick
	 * @param ipInput users IP adress
	 * @param portInput users listen-port number 
	 */
	public ServerGuest(String guestNick, InetAddress ipInput, int portInput){
		this.guestNick = guestNick;		
		this.ipInput = ipInput;
		this.portInput = portInput;
	}
	
}
