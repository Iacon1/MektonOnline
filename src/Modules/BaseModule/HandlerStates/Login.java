package Modules.BaseModule.HandlerStates;

import Client.GameClientThread;
import GameEngine.Configurables.ModuleManager;
import GameEngine.PacketTypes.LoginFeedbackPacket;
import GameEngine.PacketTypes.LoginPacket;
import Modules.BaseModule.ClientHandlerThread;
import Modules.TestModule.TestAccount;
import Net.StateFactory;
import Net.ThreadState;
import Server.Account;
import Utils.Logging;
import Utils.MiscUtils;

public class Login implements ThreadState<ClientHandlerThread>
{
	boolean send_;
	private StateFactory factory_;
	LoginFeedbackPacket loginFeedback_;
	public Login(StateFactory factory)
	{
		factory_ = factory;
		
		loginFeedback_ = new LoginFeedbackPacket();
		send_ = false;
	}
	
	@Override
	public void onEnter(ClientHandlerThread parentThread) {}

	public void processInput(String input, ClientHandlerThread parentThread, boolean mono)
	{
		if (send_) return; // Don't take more packets while still giving feedback on one
		
		LoginPacket packet = new LoginPacket();
		packet = (LoginPacket) packet.fromJSON(input);
		
		Account account = new TestAccount(); // TODO Modularize
		account.username = packet.username; 
		account.setHash(packet);
		
		if (packet.newUser)
		{
			if (parentThread.getParent().getAccount(packet.username) != null) return; // Don't overwrite an old account!
			loginFeedback_.successful = parentThread.getParent().addAccount(account);
			if (loginFeedback_.successful)
			{
				parentThread.setUsername(account.username);
				Logging.logNotice("Client " + parentThread.getSocket().getInetAddress() + " has made account \"" + parentThread.getUsername() + "\".");
				
				ModuleManager.makePlayer(parentThread.getParent(), parentThread.getAccount());
			}
		}
		else
		{
			loginFeedback_.successful = parentThread.getParent().login(packet.username, packet);
			if (loginFeedback_.successful)
			{
				parentThread.setUsername(account.username);
				Logging.logNotice("Client " + parentThread.getSocket().getInetAddress() + " has logged in as account \"" + parentThread.getUsername() + "\".");
				
				ModuleManager.wakePlayer(parentThread.getParent(), parentThread.getAccount());
			}
		}
		
		send_ = true;
	}
	
	public String processOutput(ClientHandlerThread parentThread, boolean mono)
	{
		if (send_)
		{
			send_ = false;
			if (loginFeedback_.successful) parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(MainScreen.class)));
			return loginFeedback_.toJSON();
		}
		else return null;
	}

	@Override
	public void processInputTrio(String input, ClientHandlerThread parentThread) {processInput(input, parentThread, false);}
	@Override
	public String processOutputTrio(ClientHandlerThread parentThread) {return processOutput(parentThread, false);}
	
	@Override
	public void processInputMono(String input, ClientHandlerThread parentThread) {processInput(input, parentThread, true);}
	@Override
	public String processOutputMono(ClientHandlerThread parentThread) {return processOutput(parentThread, true);}
	
	@Override
	public StateFactory getFactory()
	{
		return factory_;
	}
}
