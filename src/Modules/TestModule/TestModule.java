// By Iacon1
// Created 05/31/2021
// Module file

package Modules.TestModule;

import GameEngine.GameCanvas;
import GameEngine.GameEntity;
import GameEngine.GameWorld;
import GameEngine.SolidEntity;
import GameEngine.Configurables.Module;
import Modules.BaseModule.BaseServer;
import Net.StateFactory;
import Server.Account;

import Server.GameServer;

public class TestModule implements Module
{
	private ModuleConfig config_;
	
	
	public TestModule()
	{
		config_ = new ModuleConfig();
		
		config_.doesImplement_.put("makeServer", true);
		config_.doesImplement_.put("setup", true);
		config_.doesImplement_.put("loadWorld", true);
		
		config_.doesImplement_.put("drawWorld", true);
		
		config_.doesImplement_.put("makePlayer", true);
		config_.doesImplement_.put("wakePlayer", true);
		config_.doesImplement_.put("sleepPlayer", false);
		config_.doesImplement_.put("deletePlayer", false);
		
		config_.doesImplement_.put("clientFactory", false);
		config_.doesImplement_.put("handlerFactory", false);
	}
	
	@Override
	public ModuleConfig getConfig()
	{
		return config_;
	}

	@Override
	public void init()
	{
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public GameServer makeServer()
	{
		return new BaseServer<TestAccount>();
	}
	@Override
	public void setup()
	{
		GameWorld.setWorld(new GameWorld()); // Todo make better
		TestHexmap map = new TestHexmap("Resources/Server Packs/Default/Tilesets/DummyTileset.png", new TestHexData());
		map.setDimensions(18, 9, 1);
	}

	@Override
	public GameWorld loadWorld(String server)
	{
		return null;
	}

	@Override
	public void drawWorld(GameWorld world, GameCanvas canvas)
	{
		world.getRootEntities().get(0).render(0, 0, canvas);
	}

	@Override
	public GameEntity makePlayer(GameServer server, Account account)
	{
		DummyPlayer player = new DummyPlayer(); // Adds a guy to the map
		account.possessee = player.getId();
		GameWorld.getWorld().getRootEntities().get(0).addChild(player);
		player.setPos(2, 2, 0);
		
		return player;
	}

	@Override
	public GameEntity wakePlayer(GameServer server, Account account)
	{
		DummyPlayer player = new DummyPlayer(); // Adds a guy to the map
		account.possessee = player.getId();
		GameWorld.getWorld().getRootEntities().get(0).addChild(player);
		player.setPos(2, 2, 0);
		
		return player;
	}

	@Override
	public GameEntity sleepPlayer(GameServer server, Account account)
	{
		return null;
	}

	@Override
	public GameEntity deletePlayer(GameServer server, Account account)
	{
		return null;
	}

	public StateFactory clientFactory()
	{
		return null;
	}
	public StateFactory handlerFactory()
	{
		return null;
	}

	
}
