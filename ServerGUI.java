import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Creates GUI for the server
 * 
 * @author Piotr Wrona
 * @version 1.1
 *
 */
public class ServerGUI extends ServerController implements ActionListener{

	static JButton saveLogButton;
	static JLabel portLabel;
	static JTextArea serverText;
	static JTextField portText;
	static boolean nickTest = false;
	static String nickListOut = "";
	static int defaultPort = 7000;    
	
	/**
	 * Constructs screen
	 */
	public ServerGUI(){
		
		setSize(400,500);
        setLocation(100, 50);
        setResizable(false);
        setTitle("Chat UDP [Server]");
        setLayout(null);

        serverText = new JTextArea("");
        serverText.setEditable(false);
        serverText.setLineWrap(true);
        JScrollPane scrollServerTxt = new JScrollPane(serverText);
        scrollServerTxt.setBounds(20,125,350,320);
        add(scrollServerTxt);		

        saveLogButton = new JButton("save log");
        saveLogButton.setBounds(20,100,100,20);
        saveLogButton.addActionListener(this);
        add(saveLogButton);	
        
        portLabel = new JLabel("listening port");
        portLabel.setBounds(150,20,100,20);
        add(portLabel);		

        portText = new JTextField(Integer.toString(defaultPort));
        portText.setBounds(150,40,100,20);
        portText.setEnabled(false);
        add(portText);
	}

	
}
