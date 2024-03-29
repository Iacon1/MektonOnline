// By Iacon1
// Created 04/25/2021
// Notifies that connection failed, then resets

package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import GameEngine.GameInfo;
import Utils.Logging;
import Utils.MiscUtils;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/** The dialog that shows when the client failed to connect.
*
*/
@SuppressWarnings("serial")
public class ConnectFailDialog extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private final JLabel failureLabel = new JLabel("Could not connect to server!");
	private final JLabel errorLabel = new JLabel("New label");
	
	/** Shows the dialog.
	 *
	 *  @param reason Reason it failed to connect.
	 */
	public static void main(String reason)
	{
		try
		{
			ConnectFailDialog dialog = new ConnectFailDialog();
			dialog.errorLabel.setText("<HTML>Reason: " + reason);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e) {Logging.logException(e);}
	}

	private void onClickOK() // When OK is pressed
	{
		this.setVisible(false);
		this.dispose();
		GetServerFrame.main(null);
	}
	
	/** Constructor.
	 * 
	 */
	public ConnectFailDialog()
	{
		setIconImages(GameInfo.getIcons(GameInfo.ExecType.client));
		setTitle(GameInfo.getProgramName() + " Client: Connection failed.");
		setBounds(100, 100, 300, 175);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		
		sl_contentPanel.putConstraint(SpringLayout.NORTH, errorLabel, 10, SpringLayout.SOUTH, failureLabel);
		sl_contentPanel.putConstraint(SpringLayout.HORIZONTAL_CENTER, errorLabel, 0, SpringLayout.HORIZONTAL_CENTER, contentPanel);
		
		sl_contentPanel.putConstraint(SpringLayout.NORTH, failureLabel, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.HORIZONTAL_CENTER, failureLabel, 0, SpringLayout.HORIZONTAL_CENTER, contentPanel);
		
		contentPanel.setLayout(sl_contentPanel);
		failureLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		contentPanel.add(failureLabel);
		
		contentPanel.add(errorLabel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(e -> {onClickOK();});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}
}
