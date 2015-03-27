import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Responsible for handling controllers and actions
 * 
 * @author Piotr Wrona
 * @version 1.1
 *
 */
public class ClientController extends JFrame implements Runnable{
	
	
	public static void main(String[] args) {		
		ClientGUI clientGUI = new ClientGUI();
		clientGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		clientGUI.setVisible(true);
		clientGUI.run();
	}

	
	/**
	 * Action handling (buttons)
	 * @param zdarzenie pressed button
	 */
	public void actionPerformed(ActionEvent zdarzenie) {
		Object zrodlo = zdarzenie.getSource();
		
		
		if (zrodlo==ClientGUI.connectButton){					
			try {
				connectWithServer();
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
		}
		
		else if (zrodlo==ClientGUI.disconnectButton){
			disconnectFromServer();
		}
		
		else if (zrodlo==ClientGUI.loggedButton){
			askForLogged();
		}
		
		else if (zrodlo==ClientGUI.sendButton && connected == true){
			sendMessage();
		}
		
		else if (zrodlo==ClientGUI.sendButton && connected == false){
			if (JOptionPane.showConfirmDialog(null, "Connect with the server?", "Not connected",
			    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				connectWithServer();
				sendMessage();
			}
		}
				
		else if (zrodlo==ClientGUI.exitButton){
			disconnectFromServer();
			clientSocketReceive.close();	
			System.exit(0);
		}
		
	}


	InetAddress IPAddress;
	DatagramSocket clientSocketSend;
	DatagramSocket clientSocketReceive;
	byte[] sendData = new byte[1024];
	DatagramPacket sendPacket;
	boolean portOpen = true;
	boolean connected = false;
	int clientListenPort = 7002;
	String usersInfo = "##&&***users***##";
	
	public void run() {
		
		DatagramPacket receivePacket;
		String content;
		InetAddress ipInput;
		int portInput;
		
		try {
			clientSocketReceive = new DatagramSocket(clientListenPort);				
		} catch (SocketException e1) {
			System.err.println("Err03 client: No port number");
			e1.printStackTrace();
		}
		
		// RECEIVING DATA
		while (portOpen) {
			
			byte[] receiveData = new byte[1024];			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);			
			try {
				clientSocketReceive.receive(receivePacket);
			} catch (IOException e1) {
				System.out.println("Logged out...");	
				e1.printStackTrace();
			}                
			content = new String(receiveData, receiveData.length);       
			ipInput = receivePacket.getAddress();						
			portInput = receivePacket.getPort();
			if(content.startsWith(usersInfo)){
				ClientGUI.loggedText.setText("");
				ClientGUI.loggedText.append(content.substring(usersInfo.length()).trim() + "\n");
			}
			else{
				ClientGUI.outputChatText.append(content.trim() + "\n");
				ClientGUI.outputChatText.setCaretPosition(ClientGUI.outputChatText.getDocument().getLength());
			}
		}
	
	}
	

	
	String witamInfo = "##&&***witam***##"; 
	/**
	 * Connecting with server
	 */
	private void connectWithServer() {
		if (ClientGUI.nickText.getText().equals("")){
			String nickString = JOptionPane.showInputDialog("Please enter your Nick: ");
			ClientGUI.nickText.setText(nickString);
		}
		try {			
			IPAddress = InetAddress.getByName(ClientGUI.ipText.getText());		
			clientSocketSend = new DatagramSocket();
			ClientGUI.port = Integer.parseInt(ClientGUI.portText.getText());
			sendData = (witamInfo + clientListenPort + ClientGUI.nickText.getText()).getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, ClientGUI.port);
			clientSocketSend.send(sendPacket);
			
			ClientGUI.statusLabel.setText("connected");
			ClientGUI.statusLabel.setForeground(Color.green);
			portOpen = true;
			connected = true;
			ClientGUI.connectButton.setEnabled(false);
			ClientGUI.disconnectButton.setEnabled(true);
			ClientGUI.nickText.setEnabled(false);
			ClientGUI.ipText.setEnabled(false);
			ClientGUI.portText.setEnabled(false);
			
		} catch (UnknownHostException e) {
			System.out.println("Err01a: UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Err02a: No client input");
			e.printStackTrace();
		}		
	}
	

	String zegnamInfo = "##&&***zegnam***##";
	/**
	 * Disconnecting from server
	 */
	private void disconnectFromServer() {
		try {			
			IPAddress = InetAddress.getByName(ClientGUI.ipText.getText());	
			clientSocketSend = new DatagramSocket();
			ClientGUI.port = Integer.parseInt(ClientGUI.portText.getText());
			sendData = (zegnamInfo + ClientGUI.nickText.getText()).getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, ClientGUI.port);
			clientSocketSend.send(sendPacket);
			
			ClientGUI.statusLabel.setText("disconnected");
			ClientGUI.statusLabel.setForeground(Color.red);
			portOpen = false;
			connected = false;
			ClientGUI.connectButton.setEnabled(true);
			ClientGUI.disconnectButton.setEnabled(false);
			ClientGUI.nickText.setEnabled(true);
			ClientGUI.ipText.setEnabled(true);
			ClientGUI.portText.setEnabled(true);
			
		} catch (UnknownHostException e) {
			System.out.println("Err01b: UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Err02b: No client input");
			e.printStackTrace();
		}
		ClientGUI.loggedText.setText("");
	}

	
	String loggedInfo = "##&&***logged***##"; 	
	/**
	 * Asking server who is logged (provides: nick, IP, port)
	 */
	private void askForLogged() {
		try {
			IPAddress = InetAddress.getByName(ClientGUI.ipText.getText());		
			clientSocketSend = new DatagramSocket();
			ClientGUI.port = Integer.parseInt(ClientGUI.portText.getText());
			sendData = (loggedInfo.getBytes());
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, ClientGUI.port);
			clientSocketSend.send(sendPacket);
			
		} catch (UnknownHostException e) {
			System.out.println("Err01a: UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Err02a: No client input");
			e.printStackTrace();
		}		
	}
	


	/**
	 * Sending a message
	 */
	private void sendMessage() {
		try {			
			IPAddress = InetAddress.getByName(ClientGUI.ipText.getText());		
			clientSocketSend = new DatagramSocket();
			ClientGUI.port = Integer.parseInt(ClientGUI.portText.getText());
			sendData = (ClientGUI.inputChatText.getText()).getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, ClientGUI.port);
			clientSocketSend.send(sendPacket);		
			ClientGUI.inputChatText.setText("");
			
		} catch (UnknownHostException e) {
			System.out.println("Err01c: UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Err02c: No client input");
			e.printStackTrace();
		}		
	}


	
}
