// By Iacon1
// Created 04/26/2021
// All data about the game world and such

package GameEngine;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import GameEngine.Client.GameFrame;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.PacketTypes.ClientInputPacket;
import GameEngine.Server.Account;
import GameEngine.Server.GameServer;
import Utils.GappyArrayList;
import Utils.Logging;
import Utils.DataManager;
import Utils.MiscUtils;

public class GameInfo
{
	// Program info
	
	public enum ExecType
	{
		client,
		server,
		editor;
	}
	/** Returns all versions of the icon for either client or server
	 * 
	 *  @param execType The set of icons to get.
	 *  @return List of icons.
	 */
	public static List<Image> getIcons(ExecType execType) // Returns all versions of the icon for client, server, or editor
	{
		List<Image> icons = new ArrayList<Image>();
		String pathPrefix;
		if (execType == ExecType.client) pathPrefix = "Icons/Client Icons/";
		else if (execType == ExecType.server) pathPrefix = "Icons/Server Icons/";
		else if (execType == ExecType.editor) pathPrefix = "Icons/Editor Icons/";
		else return null;
		try
		{
			icons.add(ImageIO.read(ClassLoader.getSystemResourceAsStream(pathPrefix + "Icon16.PNG")));
			icons.add(ImageIO.read(ClassLoader.getSystemResourceAsStream(pathPrefix + "Icon32.PNG")));
			icons.add(ImageIO.read(ClassLoader.getSystemResourceAsStream(pathPrefix + "Icon64.PNG")));
			icons.add(ImageIO.read(ClassLoader.getSystemResourceAsStream(pathPrefix + "Icon128.PNG")));
		}
		catch (Exception e) {Logging.logException(e); return null;}
		return icons;
	}
	
	/** Returns the program version.
	 *  @return The version of the program.
	 */
	public static String getVersion() // Gets version
	{
		return "V0.X";
	}
	/** Returns the program name.
	 *  @return The program's name.
	 */
	public static String getProgramName() // What is this program (inc. version)?
	{
		return "Mekton Online " + getVersion();
	}
	
	// Things that might need to be communicated
	public static class GameWorld
	{
		protected GappyArrayList<GameEntity> instances; // List of game instances; Important that we note it to be a gappy array list and not a normal one.
		public GameWorld()
		{
			instances = new GappyArrayList<GameEntity>();
		}
		
		public int addEntity(GameEntity entity)
		{
			int index = instances.getFirstGap();
			instances.add(entity);
			return index;
		}
		public int findEntity(GameEntity entity)
		{
			return instances.indexOf(entity);
		}
		public GameEntity getEntity(int id)
		{
			return instances.get(id);
		}
		public void removeEntity(int id, boolean recurse) // Removes an entity and, if needed, its children
		{
			GameEntity entity = getEntity(id);
			if (entity.getParent() != null) entity.getParent().removeChild(entity, false);
			entity.clearChildren(true);
			instances.remove(id);
			
		}
		public void removeEntity(GameEntity entity, boolean recurse)
		{
			removeEntity(entity.getId(), recurse);
		}
		
		public List<GameEntity> getRootEntities() // Returns every instance with no parent
		{
			List<GameEntity> array = new ArrayList<GameEntity>();
			for (int i = 0; i < instances.size(); ++i)
			{
				GameEntity instance = instances.get(i);
				if (instance == null) continue;
				if (instance.getParent() == null) array.add(instance);
			}
			
			return array;
		}
		
		/** Returns the list of instances of a certain type.
		 * 
		 *  @param type The class of the entity type to search for.
		 */
		public <T extends GameEntity> List<T> getEntitiesOfType(Class<T> type)
		{
			List<T> array = new ArrayList<T>();
			
			for (int i = 0; i < instances.size(); ++i)
			{
				GameEntity instance = instances.get(i);
				if (instance == null) continue;
				if (type.isAssignableFrom(instance.getClass())) array.add((T) instance);
			}
			
			return array;
		}
		
		/** Returns the list of instances.
		 */
		public List<GameEntity> getEntities() // Shows every instance
		{
			return instances; //getEntitiesOfType(GameEntity.class);
		}
	}
	private static transient GameWorld world = new GameWorld();
	
	// Things that don't need to be communicated
	
	// Client-side
	private static transient GameFrame frame;
	private static transient boolean isClient; // On if client
	public static transient ClientInputPacket clientInput = new ClientInputPacket();;
	private static transient int possessee; // Client's current player form
//	private static transient Point2D camera;
	private static transient String serverPackPrefix; // Server pack's path

	// Server-side
	private static transient GameServer server;
	public static transient final Object updateLock = new Object();
	
	public static void initWorld(GameWorld world)
	{
		world = new GameWorld();
	}
	public static boolean loadWorld()
	{
		String path = MiscUtils.getAbsolute("Local Data/Server/world.JSON");
		world = DataManager.deserialize(MiscUtils.readText(path), GameWorld.class);
		if (world == null) return false;
		else return true;
	}
	public static void saveWorld()
	{
		String path = MiscUtils.getAbsolute("Local Data/Server/world.JSON");
		MiscUtils.saveText(path, DataManager.serialize(world));
	}
	public static void setWorld(GameWorld world)
	{
		synchronized (GameInfo.updateLock)
		{
			GameInfo.world = world;
		}
	}
	public static GameWorld getWorld()
	{
		return world;
	}
	public static void updateWorld()
	{
		synchronized (GameInfo.updateLock)
		{
			for (int i = 0; i < GameInfo.world.getEntities().size(); ++i)
			{
				GameEntity entity = GameInfo.world.getEntities().get(i);
				if (entity != null) entity.update();
			}
		}
	}
	
	public static void setFrame(GameFrame frame)
	{
		GameInfo.frame = frame;
	}
	public static GameFrame getFrame()
	{
		return frame;
	}
	
	public static void setClient(boolean isClient)
	{
		GameInfo.isClient = isClient;
	}
	public static boolean isClient()
	{
		return isClient;
	}
	
	public static void setServer(GameServer server)
	{
		GameInfo.server = server;
	}
	public static GameServer getServer()
	{
		return server;
	}
	public static Account getAccount(int possessorID)
	{
		return server.getAccount(possessorID);
	}
	
	public static void setPossessee(int id)
	{
		GameInfo.possessee = id;
	}
	public static int getPossessee()
	{
		return possessee;
	}
	public static void clearInput()
	{
		clientInput.inputs.clear();
	}
	
	public static void setServerPack(String serverPack)
	{
		GameInfo.serverPackPrefix = "Resources/Server Packs/" + serverPack + "/";
	}
	public static boolean hasServerPack()
	{
		return serverPackPrefix != null;
	}
	/** Returns the absolute location of a server-pack-relative file or folder.
	 * 
	 */
	public static String inServerPack(String resourceName)
	{
		return MiscUtils.getAbsolute(serverPackPrefix + "/" + resourceName);
	}
}
