// By Iacon1
// Created 4/21/2021
// Updates the game state and receives input to send to the server

package GameEngine.Client;

import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import GameEngine.Client.ClientStates.ClientStateFactory;
import GameEngine.Configurables.ModuleManager;
import GameEngine.Configurables.ModuleTypes.StateGiverModule;
import GameEngine.Net.StateFactory;
import GameEngine.Net.StatefulConnectionPairThread;
import GameEngine.Net.Server.ServerInfo;
import GameEngine.Server.HandlerStates.HandlerStateFactory;

/** Thread that the client uses to communicate with the server and run the game.
 * 
 */
public class GameClientThread extends StatefulConnectionPairThread
{
	private StateFactory stateFactory; // Where we get our states
	
	private ServerInfo serverInfo; // The server's info
	
	private Map<String, Container> containers; // All currently-open UI

	/** Constructor
	 * 
	 */
	public GameClientThread()
	{
		super();
		StateGiverModule stateFactoryFactory = ModuleManager.getHighestOfType(StateGiverModule.class);
		if (stateFactoryFactory == null) // Use default factory
			stateFactory = new ClientStateFactory();
		else stateFactory = stateFactoryFactory.clientFactory(); // Use found factory
		containers = new HashMap<String, Container>();
		initState(stateFactory.getState(0));
	}
	
	@Override
	public void onClose()
	{
		if (containers != null) // Close all containers
		{
			for (String name : containers.keySet())
			{
				containers.get(name).setVisible(false);
				containers.get(name).setEnabled(false);
			}
		}
	}
	
	/** Stores a container.
	 * 
	 *  @param name Name to store the container under.
	 *  @param container Container to store.
	 */
	public void setContainer(String name, Container container)
	{
		containers.put(name, container);
	}
	
	/** Gets a stored container if possible, returning null otherwise.
	 * 
	 *  @param name Name to search for.
	 * 
	 *  @return Either the container listed as name or null if no such container exists.
	 */
	public Container getContainer(String name)
	{
		return containers.get(name);
	}

	/** Stores the server's info packet.
	 * 
	 *  @param serverInfo the received packet.
	 */
	public void setInfo(ServerInfo serverInfo)
	{
		this.serverInfo = serverInfo;
	}
	/** Gets the stored info packet.
	 * 
	 *  @return The stored packet, or null if no packet is stored.
	 */
	public ServerInfo getInfo()
	{
		return serverInfo;
	}
}

