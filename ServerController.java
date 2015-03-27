import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Responsible for handling controllers and actions
 * 
 * @author Piotr Wrona
 * @version 1.1
 *
 */
public class ServerController extends JFrame implements Runnable{

	public static void main(String[] args) {			
		ServerGUI serverGUI = new ServerGUI();
		serverGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverGUI.setVisible(true);
		serverGUI.run();
	}
	
	public static ArrayList<ServerGuest> guestList = new ArrayList<ServerGuest>();
	
	String witamInfo = "##&&***witam***##"; 
	String zegnamInfo = "##&&***zegnam***##";
	String loggedInfo = "##&&***logged***##";
	String usersInfo = "##&&***users***##";
	
	DatagramSocket clientSocketSend;
	DatagramSocket clientSocketSendUsers;
	DatagramSocket clientSocketReceive;
	
	public void run() {	
		try {
			clientSocketReceive = new DatagramSocket(ServerGUI.defaultPort);
		} catch (SocketException e1) {
			System.err.println("Err03 server: No port number");
			e1.printStackTrace();
		}
	
		DatagramPacket receivePacket;		
		String content;
		InetAddress ipInput;
		int portInput;
		String guestNick = null;
		String message = null;		

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		boolean onlyForOneClient = false;
		boolean usersChanged = false;
		
		while (true) {
				
			byte[] receiveData = new byte[1024];			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);			
			try {
				clientSocketReceive.receive(receivePacket);
			} catch (IOException e1) {
				System.err.println("Err03d server: No port number");
				e1.printStackTrace();
			}                
			content = new String(receiveData, receiveData.length);       
			ipInput = receivePacket.getAddress();						
			portInput = receivePacket.getPort();  
				
		
			if (content.startsWith(witamInfo)){
				ServerGUI.nickTest = true;
				String guestportTemp = content.substring(17,21); // cutting in port
				String guestNickTemp = content.substring(21).trim(); // cutting out witamInfo&port
				message = "***Connected: " + content.substring(21).trim();
				ServerGUI.serverText.append(message + "\n");
				guestList.add(new ServerGuest(guestNickTemp, ipInput, Integer.parseInt(guestportTemp)));
				ServerGUI.serverText.append("{ " + guestNickTemp + "  " + ipInput + ":" + Integer.parseInt(guestportTemp) + " }\n");
				usersChanged = true;
			}
				
			else if (content.startsWith(zegnamInfo)){
				String guestNickTemp = content.substring(zegnamInfo.length()).trim(); // cutting out zegnamInfo
				for (int counter=0; counter < guestList.size(); counter++){
					if ( (guestList.get(counter).guestNick.equals(guestNickTemp)) 
							&& (guestList.get(counter).ipInput.equals(ipInput))  ){
						guestList.remove(counter);
						message = "***Disconnected: " + guestNickTemp.trim();
						ServerGUI.serverText.append(message + "\n");
					}
				}
				usersChanged = true;
			}			
			
			else if (content.startsWith(loggedInfo)){
				String messageWhoLogged = "=======================================\nosoby zalogowane:\n";
				System.out.println("jest logged");
				// checking who wants to know
				int counter=0;
				while (counter < guestList.size()){			
					if (guestList.get(counter).ipInput.equals(ipInput)){
						break;
					}
					else { counter++; }
				}	
				try {
					clientSocketSend = new DatagramSocket();
				} catch (SocketException e) {
					System.err.println("Err03e server: No port number");
					e.printStackTrace();
				}
				// buiding messageWhoLogged
				for (int j=0; j < guestList.size(); j++){
					messageWhoLogged += "Nick: " + guestList.get(j).guestNick + "\tIP: " + guestList.get(j).ipInput + "\tPort: " + guestList.get(j).portInput + "\n";			
				}
				messageWhoLogged += "=======================================";
				ServerGUI.serverText.append(messageWhoLogged + "\n");
				onlyForOneClient = true;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// sending
				byte[] sendData = new byte[1024];
				InetAddress IPAddress2Send = guestList.get(counter).ipInput;			
				int port2Send = guestList.get(counter).portInput;
				sendData = messageWhoLogged.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2Send, port2Send);
				try {
					clientSocketSend.send(sendPacket);
					clientSocketSend.close();
				} catch (IOException e) {
					System.err.println("Err03f server: No port number");
					e.printStackTrace();
				}
				
			}			
			
			else {
				for (int counter=0; counter < guestList.size(); counter++){
					if (guestList.get(counter).ipInput.equals(ipInput)){
						Date date = new Date();
						message = "[" + guestList.get(counter).guestNick + "] [" + dateFormat.format(date) + "] " + content.trim();
						ServerGUI.serverText.append(message + "\n");
						break;
					}			
				}			
			}								
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// message to every client
			if (!onlyForOneClient){
				InetAddress IPAddress2Send;
				int port2Send;
				byte[] sendData = new byte[1024];
				DatagramPacket sendPacket;
				byte[] sendDataUsers = new byte[1024];
				DatagramPacket sendPacketUsers;
				String messageUsers;
				
				// sending message to every client
				for (int counter=0; counter < guestList.size(); counter++){				
					messageUsers = usersInfo;
					try {
						clientSocketSend = new DatagramSocket();
						clientSocketSendUsers = new DatagramSocket();
					} catch (SocketException e) {
						System.err.println("Err03g server: No port number");
						e.printStackTrace();
					}
					IPAddress2Send = guestList.get(counter).ipInput;			
					port2Send = guestList.get(counter).portInput;
					sendData = message.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2Send, port2Send);
					
					// loop logged users (nicks)
					for (int j=0; j < guestList.size(); j++){
						messageUsers += guestList.get(j).guestNick + "\n";
					}
					sendDataUsers = messageUsers.getBytes();
					sendPacketUsers = new DatagramPacket(sendDataUsers, sendDataUsers.length, IPAddress2Send, port2Send);
					
					try {
						clientSocketSend.send(sendPacket);
						if (usersChanged) { clientSocketSendUsers.send(sendPacketUsers); } // if users has changes then send updated users (nick) list
						
						clientSocketSend.close();
					} catch (IOException e) {
						System.err.println("Err03h server: No port number");
						e.printStackTrace();
					}	
				}		
				usersChanged = false;
			}
			onlyForOneClient = false;
			ServerGUI.serverText.setCaretPosition(ServerGUI.serverText.getDocument().getLength());
		}
		
		
	}
	
	
	/**
	 * Action handling (buttons)
	 * @param zdarzenie pressed button
	 */
	public void actionPerformed(ActionEvent zdarzenie) {
		Object zrodlo = zdarzenie.getSource();
		
		if (zrodlo==ServerGUI.saveLogButton){
			if (JOptionPane.showConfirmDialog(null, "Save server log?", "Save..",
			    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				
				try {
		            File file = new File("serverLog.txt");
		            BufferedWriter output = new BufferedWriter(new FileWriter(file));
		            output.write(ServerGUI.serverText.getText());
		            output.close();
		            JOptionPane.showMessageDialog(null, "Server log has been saved", "..Saved", JOptionPane.INFORMATION_MESSAGE);
		        } catch ( IOException e ) {
		            e.printStackTrace();
		        }
			}
		}
	}
	

	

}
