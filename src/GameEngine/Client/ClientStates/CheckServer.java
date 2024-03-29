package GameEngine.Client.ClientStates;

import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;
import GameEngine.PacketTypes.ClientInfoPacket;
import GameEngine.PacketTypes.ServerInfoPacket;
import GameEngine.DiffieHellman;
import GameEngine.GameInfo;
import GameEngine.Client.GameClientThread;
import GameEngine.Configurables.ModuleManager;
import Utils.DataManager;
import Utils.MiscUtils;

public class CheckServer implements ThreadState<GameClientThread>
{
	private enum Result
	{
		waiting,
		login,
		bad,
		done
	}
	private StateFactory factory;
	private volatile Result result; // 0 - Waiting for result; 1 - Login; 2 - Bad Server; 3 - Done
	private GameEngine.DiffieHellman diffieHellman;
	
	public CheckServer(StateFactory factory)
	{
		this.factory = factory;
		diffieHellman = new DiffieHellman();
		diffieHellman.start();
		
		result = Result.waiting;
	}
	
	@Override
	public void onEnter(GameClientThread parentThread) {parentThread.setEncrypt(false);}
	
	@Override
	public void processInput(String input, GameClientThread parentThread)
	{
		ServerInfoPacket packet = DataManager.deserialize(input, ServerInfoPacket.class);
		
		if (packet == null)
		{
			result = Result.bad;
			parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(BadServer.class)));
		}
		else
		{
			parentThread.setInfo(packet.getInfo());
			
			if (parentThread.getSocket() == null || !packet.version.equals(GameInfo.getVersion()))
			{
				result = Result.bad;
				parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(BadServer.class)));
			}
			else
			{
				result = Result.login;
				diffieHellman.start();
				diffieHellman.end(packet.mix, parentThread);
				GameInfo.setServerPack(packet.resourceFolder);
				ModuleManager.loadModules();
				parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(Login.class)));
			}
		}
	}
	@Override
	public String processOutput(GameClientThread parentThread)
	{
		if (result == Result.waiting) return null;
		
		if (result == Result.login) // We decided to go forward!
		{
			result = Result.done;
			ClientInfoPacket packet = new ClientInfoPacket();
			packet.version = GameInfo.getVersion();
			packet.mix = diffieHellman.getPublicMix();
			
			return DataManager.serialize(packet);
		}
		else if (result == Result.bad)
		{
			result = Result.done;
			return null; // We have nothing to say to them
		}
		else if (result == Result.done) return null;
		else return null; // ???
	}
	
	@Override
	public StateFactory getFactory()
	{
		return factory;
	}
}
