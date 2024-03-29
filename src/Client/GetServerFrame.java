// By Iacon1
// Created 04/23/2021
// Gets server data from user

package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import GameEngine.GameInfo;
import GameEngine.Client.GameClientThread;
import GameEngine.Net.Client.Client;
import Utils.Logging;
import Utils.MiscUtils;
import Utils.DataManager;

import javax.swing.JLabel;
import javax.swing.SpringLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

/** The frame for getting the server.
 * 
 */
@SuppressWarnings("serial")
public class GetServerFrame extends JFrame
{
	private static class SavedServer
	{
		public String name;
		public String ip;
		public int port;
	}
	
	private JPanel contentPane;
	private final JLabel headerLabel = new JLabel("Mekton Online Client");
	private final JLabel portLabel = new JLabel("Server Port");
	private final JSpinner portSpinner = new JSpinner();
	private final JLabel serverAddressLabel = new JLabel("Server Address");
	private final JTextField serverIPBox = new JTextField();
	private final JList<String> savedServerList = new JList<String>();
	private final JLabel savedServerLabel = new JLabel("Saved Servers");
	private final JButton saveServerButton = new JButton("Save server as...");
	private final JButton removeServerButton = new JButton("Remove server...");
	private final JButton connectServerButton = new JButton("Connect to server");
	private List<SavedServer> savedServerArray;
	// Functionality
	
	private void populateList() // Populates savedServerList from file
	{
		savedServerArray = (ArrayList<SavedServer>) DataManager.deserializeCollectionList(MiscUtils.readText("Local Data/Client/SavedServers.JSON"), ArrayList.class, SavedServer.class);
		if (savedServerArray == null)
		{
			savedServerArray = new ArrayList<SavedServer>();
			return;
		}
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		
		for (int i = 0; i < savedServerArray.size(); ++i)
		{
			model.addElement(savedServerArray.get(i).name);
		}
		
		savedServerList.setModel(model);
	}
	
	/** Saves the currently-defined server to the saved server list.
	 * 
	 *  @param name Name to save the server under.
	 */
	public void saveServer(String name) // Saves current server w/ provided name
	{
		SavedServer server = new SavedServer();
		server.name = name;
		server.ip = serverIPBox.getText();
		server.port = (Integer) portSpinner.getValue();
		
		boolean foundServer = false;
		for (int i = 0; i < savedServerArray.size(); ++i)
		{
			if (savedServerArray.get(i).name.equals(server.name))
			{
				savedServerArray.set(i, server);
				foundServer = true;
			}
		}
		if (!foundServer) savedServerArray.add(server);
		
		String serialized = DataManager.serialize(savedServerArray);
		MiscUtils.saveText("Local Data/Client/SavedServers.JSON", serialized);
		populateList();
	}
	/** Removes a server from the saved server list.
	 * 
	 *  @param name Name of the server to remove.
	 */
	public void removeServer(String name) // Removes selected server
	{
		for (int i = 0; i < savedServerArray.size(); ++i)
		{
			if (savedServerArray.get(i).name.equals(name))
			{
				savedServerArray.remove(i);
				MiscUtils.saveText("Local Data/Client/SavedServers.JSON", DataManager.serialize(savedServerArray));
				populateList();
				return;
			}
		}
	}
	private void onSave() // When save button is pressed
	{
		SaveServerDialog.main(this); // Hacky, but works
	}
	private void onRemove() // When remove button is pressed
	{
		RemoveServerDialog.main(this);
	}
	private void onConnect() // When connect button is pressed
	{
		Client<GameClientThread> client = new Client<GameClientThread>(serverIPBox.getText(), (Integer) portSpinner.getValue(), new GameClientThread());
		client.run();
		this.setVisible(false);
		this.dispose();
	}
	private void onSelect() // When a saved server is selected
	{
		for (int i = 0; i < savedServerArray.size(); ++i)
		{
			if (savedServerArray.get(i).name.equals(savedServerList.getSelectedValue()))
			{
				serverIPBox.setText(savedServerArray.get(i).ip);
				portSpinner.setValue(savedServerArray.get(i).port);
			}
		}
	}
	
	/** Runs the frame.
	 * 
	 *  @param args Not used.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					GetServerFrame frame = new GetServerFrame();
					frame.setVisible(true);
				}
				catch (Exception e) {Logging.logException(e);}
			}
		});
	}

	/** Constructor.
	 * 
	 */
	public GetServerFrame()
	{
		setIconImages(GameInfo.getIcons(GameInfo.ExecType.client));
		setTitle(GameInfo.getProgramName() + " Client: Join Server");
		serverIPBox.setColumns(10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout(); // Set up layout
		sl_contentPane.putConstraint(SpringLayout.NORTH, connectServerButton, 10, SpringLayout.SOUTH, saveServerButton);
		//sl_contentPane.putConstraint(SpringLayout.WEST, connectServerButton, 0, SpringLayout.WEST, saveServerButton);
		sl_contentPane.putConstraint(SpringLayout.EAST, connectServerButton, 0, SpringLayout.EAST, removeServerButton);
		
		sl_contentPane.putConstraint(SpringLayout.VERTICAL_CENTER, saveServerButton, 0, SpringLayout.VERTICAL_CENTER, removeServerButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, saveServerButton, 0, SpringLayout.WEST, serverIPBox);
	
		sl_contentPane.putConstraint(SpringLayout.SOUTH, savedServerLabel, -10, SpringLayout.NORTH, savedServerList);
		sl_contentPane.putConstraint(SpringLayout.HORIZONTAL_CENTER, savedServerLabel, 0, SpringLayout.HORIZONTAL_CENTER, savedServerList);
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, savedServerList, -75, SpringLayout.SOUTH, portSpinner);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, savedServerList, 90, SpringLayout.SOUTH, portSpinner);
		sl_contentPane.putConstraint(SpringLayout.WEST, savedServerList, -100, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, savedServerList, -10, SpringLayout.EAST, contentPane);
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, serverAddressLabel, 0, SpringLayout.NORTH, portLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, serverAddressLabel, 0, SpringLayout.WEST, serverIPBox);
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, serverIPBox, 0, SpringLayout.NORTH, portSpinner);
		sl_contentPane.putConstraint(SpringLayout.WEST, serverIPBox, -140, SpringLayout.EAST, serverIPBox);
		sl_contentPane.putConstraint(SpringLayout.EAST, serverIPBox, -10, SpringLayout.WEST, portSpinner);
		
		sl_contentPane.putConstraint(SpringLayout.SOUTH, removeServerButton, -50, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, removeServerButton, 0, SpringLayout.WEST, portSpinner);
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, portSpinner, 10, SpringLayout.SOUTH, portLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, portSpinner, 0, SpringLayout.WEST, portLabel);
		
		sl_contentPane.putConstraint(SpringLayout.HORIZONTAL_CENTER, portLabel, -30, SpringLayout.HORIZONTAL_CENTER, contentPane);
		sl_contentPane.putConstraint(SpringLayout.VERTICAL_CENTER, portLabel, 0, SpringLayout.VERTICAL_CENTER, contentPane);
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, headerLabel, 25, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.HORIZONTAL_CENTER, headerLabel, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
		
		contentPane.setLayout(sl_contentPane);
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(headerLabel);
		portLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		contentPane.add(portLabel);
		portSpinner.setModel(new SpinnerNumberModel(0, 0, 65535, 1));
		portSpinner.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		contentPane.add(portSpinner);
		
		contentPane.add(serverAddressLabel);
		
		contentPane.add(serverIPBox);
		savedServerList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		savedServerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		populateList();
		
		saveServerButton.addActionListener(e -> {onSave();});
		removeServerButton.addActionListener(e -> {onRemove();});
		connectServerButton.addActionListener(e -> {onConnect();});
		savedServerList.addListSelectionListener(e -> {onSelect();});
		contentPane.add(savedServerList);
		
		contentPane.add(savedServerLabel);
		
		contentPane.add(saveServerButton);
		
		contentPane.add(removeServerButton);
		
		contentPane.add(connectServerButton);
	}
}
