// By Iacon1
// Created 06/17/2021
// Test account implementation

package Modules.TestModule;

import javax.swing.JPanel;

import GameEngine.GameInfo;
import GameEngine.Point2D;
import GameEngine.Configurables.ConfigManager;
import GameEngine.EntityTypes.CommandRunner;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.Server.Account;
import Modules.BaseModule.CommandParser;
import Modules.HexUtilities.HexDirection;
import Modules.HexUtilities.HexEntity;

public class TestAccount extends Account
{

	@Override
	public void runCommand(String[] params)
	{
		CommandParser.ParsedCommand command = CommandParser.parseCommand(params);
		if (command.target == -1) command.target = possessee;
		GameEntity entity = GameInfo.getWorld().getEntity(command.target);
		if (CommandRunner.class.isAssignableFrom(entity.getClass()))
		{
			((CommandRunner) entity).runCommand(command.params);
		}
	}

	@Override
	public Point2D getCamera()
	{
		HexEntity hexObject = (HexEntity) getPossessee();
//		return new Point2D(0, 0);
		return hexObject.getPos().subtract(new Point2D(ConfigManager.getScreenWidth(), ConfigManager.getScreenHeight()).divide(2));
	}

	@Override
	public JPanel serverPanel()
	{
		String status = null;
		if (loggedIn == true) status = "Online";
		else status = "Offline";
		return new AccountPanel(username, status);
	}

}
