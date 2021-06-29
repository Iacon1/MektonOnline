// By Iacon1
// Created 06/16/2021
// Login

package Modules.BaseModule.ClientStates;

import Client.GameClientThread;
import GameEngine.PacketTypes.LoginFeedbackPacket;
import GameEngine.PacketTypes.LoginPacket;
import Modules.BaseModule.ClientFrames.LoginDialog;
import Net.StateFactory;
import Net.ThreadState;

public class Login implements ThreadState<GameClientThread>
{
	private StateFactory factory_;
	
	public Login(StateFactory factory)
	{
		factory_ = factory;
	}
	
	@Override
	public void onEnter(GameClientThread parentThread)
	{
		LoginDialog.main(parentThread);
	}

	@Override
	public void processInput(String input, GameClientThread parentThread)
	{
		LoginFeedbackPacket packet = new LoginFeedbackPacket();
		packet = (LoginFeedbackPacket) packet.fromJSON(input);
		
		if (packet.successful)
		{
			LoginDialog dialog = (LoginDialog) parentThread.getContainer();
			dialog.setVisible(false);
			dialog.dispose();
			parentThread.queueStateChange(getFactory().getState(MainScreen.class.getCanonicalName()));
		}
		else
		{
			LoginDialog dialog = (LoginDialog) parentThread.getContainer();
			dialog.onFail("Login failed.");
		}
	}
	
	@Override
	public String processOutput(GameClientThread parentThread)
	{
		LoginDialog dialog = (LoginDialog) parentThread.getContainer();
		LoginPacket packet = dialog.getPacket();
		if (packet != null)
			return packet.toJSON();
		else return null;
	}

	@Override
	public StateFactory getFactory()
	{
		return factory_;
	}

}