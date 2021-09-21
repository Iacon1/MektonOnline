// By Iacon1
// Created 06/16/2021
// Bad server

package Modules.BaseModule.ClientStates;

import Client.ConnectFailDialog;
import Client.GameClientThread;
import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;
import Utils.MiscUtils;

public class BadServer implements ThreadState<GameClientThread>
{
	private StateFactory factory;
	
	public BadServer(StateFactory factory)
	{
		this.factory = factory;
	}
	
	@Override
	public void onEnter(GameClientThread parentThread)
	{
		if (parentThread.getSocket() == null)
			ConnectFailDialog.main("Server wasn't found.");
		else if (parentThread.getInfo() == null)
			ConnectFailDialog.main("Server wasn't recognized.");
		else if (!parentThread.getInfo().version.equals(MiscUtils.getVersion()))
			ConnectFailDialog.main("Server was version " + parentThread.getInfo().version + ";<br> Should be " + MiscUtils.getVersion());
		parentThread.close();
	}

	public void processInput(String input, GameClientThread parentThread, boolean mono) {}
	public String processOutput(GameClientThread parentThread, boolean mono) {return null;}

	@Override
	public void processInputTrio(String input, GameClientThread parentThread) {processInput(input, parentThread, false);}
	@Override
	public String processOutputTrio(GameClientThread parentThread) {return processOutput(parentThread, false);}
	
	@Override
	public void processInputMono(String input, GameClientThread parentThread) {processInput(input, parentThread, true);}
	@Override
	public String processOutputMono(GameClientThread parentThread) {return processOutput(parentThread, true);}
	
	@Override
	public StateFactory getFactory()
	{
		return factory;
	}

}