// By Iacon1
// Created 06/17/2021
// Basic functionality

package Modules.BaseModule;

import GameEngine.GameEntity;
import GameEngine.GameWorld;
import GameEngine.Configurables.Module;
import GameEngine.Configurables.ModuleManager;
import Modules.BaseModule.ClientStates.ClientStateFactory;
import Modules.BaseModule.HandlerStates.HandlerStateFactory;
import Net.StateFactory;
import Net.Server.Server;
import Server.Account;
import Server.GameServer;

public class BaseModule implements Module
{
	private ModuleConfig config_;
	
	@Override
	public ModuleConfig getConfig()
	{
		config_ = new ModuleConfig();
		
		config_.doesImplement_.put("makeServer", false);
		config_.doesImplement_.put("setup", false);
		config_.doesImplement_.put("loadWorld", false);
		
		config_.doesImplement_.put("makePlayer", false);
		config_.doesImplement_.put("wakePlayer", false);
		config_.doesImplement_.put("sleepPlayer", false);
		config_.doesImplement_.put("deletePlayer", false);
		
		config_.doesImplement_.put("clientFactory", true);
		config_.doesImplement_.put("handlerFactory", true);
		
		return config_;
	}

	@Override
	public void init() {}
	
	@SuppressWarnings("rawtypes")
	@Override
	public GameServer makeServer()
	{
		return null;
	}
	@Override
	public GameWorld setup()
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
public GameWorld loadWorld(String server)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public GameEntity makePlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public GameEntity wakePlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public GameEntity sleepPlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public GameEntity deletePlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public StateFactory clientFactory()
	{
		return new ClientStateFactory();
	}

	@Override
	public StateFactory handlerFactory()
	{
		return new HandlerStateFactory();
	}

}