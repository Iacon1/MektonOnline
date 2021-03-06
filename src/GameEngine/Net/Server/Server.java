// By Iacon1
// Created 04/26/2021
//

package GameEngine.Net.Server;

import java.util.function.Supplier;

import GameEngine.Net.ConnectionPairThread;
import Utils.Logging;
import Utils.MiscUtils;

public abstract class Server<T extends ConnectionPairThread>
{
	private ServerThread<T> serverThread;
	
	public Server(Supplier<T> supplier)
	{	
		serverThread = new ServerThread<T>(-1, supplier);
	}
	
	public void start(int port)
	{
		serverThread.setPort(port);
		serverThread.open();
		serverThread.start();
		if (serverThread.isAlive()) Logging.logNotice("Server started on IP " + MiscUtils.getExIp() + "<br> & port " + port);
	}
	
	public abstract String getName();
	public abstract int currentPlayers();
	public abstract int maxPlayers();

}
