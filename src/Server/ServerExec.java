// By Iacon1
// Created 04/26/2021
// Runs the server

package Server;

import javax.swing.UIManager;

import GameEngine.DebugLogger;
import Server.Frames.ServerWindow;
import Utils.Logging;

public class ServerExec
{

	public static void main(String[] args)
	{
		//Logging.setLogger(new DebugLogger());
		Logging.setLogger(new ServerLogger());
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Server server = new Server();
			server.start(6666); // TODO changeable
			ServerWindow.main(server);
		}
		catch (Exception e) {Logging.logException(e);}
	}

}