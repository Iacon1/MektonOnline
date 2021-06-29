// By Iacon1
// Created 06/17/2021
//

package Modules.MektonCore;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import GameEngine.GameEntity;
import GameEngine.GameWorld;
import GameEngine.GraphicsCanvas;
import GameEngine.Configurables.ConfigManager;
import GameEngine.Configurables.Module;
import GameEngine.Configurables.ModuleManager;
import GameEngine.Configurables.Module.ModuleConfig;
import Modules.BaseModule.TabPopulator;
import Modules.BaseModule.ClientFrames.ClientMainGameFrame;
import Net.StateFactory;
import Server.Account;
import Server.GameServer;

public class MektonCore implements Module, TabPopulator
{
	private ModuleConfig config_;
	
	@Override
	public void populateTabs(ClientMainGameFrame frame, JTabbedPane tabbedPane)
	{
		JPanel mapViewPanel = new JPanel();
		JPanel inventoryPanel = new JPanel();
		JPanel characterPanel = new JPanel();
		JPanel mekPanel = new JPanel();
		
		GraphicsCanvas hexmapCanvas = new GraphicsCanvas();
		
		mapViewPanel.setLayout(null);
		mapViewPanel.setSize(tabbedPane.getWidth(), tabbedPane.getHeight() - 25); // TODO calculate size of tabs
		hexmapCanvas.setBounds(0, 0, ConfigManager.getScreenWidth(), ConfigManager.getScreenHeight());
		mapViewPanel.add(hexmapCanvas);
		
		frame.addTab("Map", mapViewPanel);
		frame.addTab("Inventory", inventoryPanel);
		frame.addTab("Character", characterPanel);
		frame.addTab("Mech", mekPanel);
	}

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
		
		config_.doesImplement_.put("clientFactory", false);
		config_.doesImplement_.put("handlerFactory", false);
		
		return config_;
	}

	@Override
	public void init()
	{
		ModuleManager.attemptRegister("populateTabs", this);
	}

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

	@Override
	public GameEntity makePlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameEntity wakePlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameEntity sleepPlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameEntity deletePlayer(GameServer server, Account account)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StateFactory clientFactory()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StateFactory handlerFactory()
	{
		// TODO Auto-generated method stub
		return null;
	}
}