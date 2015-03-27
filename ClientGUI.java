import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Creates GUI for the client
 * 
 * @author Piotr Wrona
 * @version 1.1
 *
 */
public class ClientGUI extends ClientController implements ActionListener{


	static JButton connectButton, disconnectButton, exitButton, sendButton, loggedButton;
	static JLabel statusLabel, nickLabel, ipLabel, portLabel;
	static JTextArea inputChatText, outputChatText, loggedText;
	static JTextField nickText, ipText, portText;	
	public static int port = 7000;
	
	
	
	/**
	 * Constructs screen
	 */
	public ClientGUI(){

		setSize(600,580);
		setLocation(600, 50);
		//setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Chat UDP [Client]");
		setLayout(null);
		
		connectButton = new JButton("Connect");
		connectButton.setBounds(20,20,100,30);
		add(connectButton);
		connectButton.addActionListener(this);
				
		disconnectButton = new JButton("Disconnect");		
		disconnectButton.setBounds(20,50,100,30);
		add(disconnectButton);
		disconnectButton.addActionListener(this);
		disconnectButton.setEnabled(false);
		
		exitButton = new JButton("Exit");		
		exitButton.setBounds(460,20,100,30);
		add(exitButton);
		exitButton.addActionListener(this);	
		
		loggedButton = new JButton("Logged");		
		loggedButton.setBounds(460,80,100,30);
		add(loggedButton);
		loggedButton.addActionListener(this);	
		
		statusLabel = new JLabel("..::status::..");
		statusLabel.setBounds(20,100,100,30);
		add(statusLabel);
		
		inputChatText = new JTextArea("");
		inputChatText.setLineWrap(true);
		JScrollPane scrollInTxt = new JScrollPane(inputChatText);
		scrollInTxt.setBounds(20,490,420,50);
		add(scrollInTxt);	
		
		
		outputChatText = new JTextArea("");
		outputChatText.setEditable(false);
		outputChatText.setLineWrap(true);
		JScrollPane scrollOutTxt = new JScrollPane(outputChatText);
		scrollOutTxt.setBounds(20,125,420,350);
		add(scrollOutTxt);	
		
		loggedText = new JTextArea("");
		loggedText.setEditable(false);
		loggedText.setLineWrap(true);
		JScrollPane scrollLoggedTxt = new JScrollPane(loggedText);
		scrollLoggedTxt.setBounds(460,125,100,350);
		add(scrollLoggedTxt);
		
		
		sendButton = new JButton("Send");		
		sendButton.setBounds(460,490,100,50);
		add(sendButton);
		sendButton.addActionListener(this);

		
		
		nickLabel = new JLabel("Nick:");
		nickLabel.setBounds(200,20,50,20);
		add(nickLabel);
		ipLabel = new JLabel("IP:");
		ipLabel.setBounds(200,40,50,20);
		add(ipLabel);
		portLabel = new JLabel("port:");
		portLabel.setBounds(200,60,50,20);
		add(portLabel);
		
		nickText = new JTextField("");
		nickText.setBounds(240,20,100,20);
		add(nickText);
		ipText = new JTextField("localhost");
		ipText.setBounds(240,40,100,20);
		add(ipText);
		portText = new JTextField(Integer.toString(port));
		portText.setBounds(240,60,100,20);
		add(portText);
	}
	

}
