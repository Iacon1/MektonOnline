// By Iacon1
// Created 04/26/2021
//

package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import GameEngine.GameWorld;
import GameEngine.Hexmap;
import Net.Server.ServerThread;
import Utils.*;

// TODO these are all dummy values
public class Server
{
	private ServerThread<ClientHandlerThread> serverThread_;
	private static ClientHandlerThread template_;
	private HashMap<String, Account> accounts_;
	
	public Server()
	{
		GameWorld.init();
		Hexmap map = new Hexmap();
		map.setDimensions(18, 9, 1);
		template_ = new ClientHandlerThread();
		template_.setParent(this);

		java.lang.reflect.Type accountsType = new TypeToken<HashMap<String, Account>>(){}.getType();
		try {accounts_ = JSONManager.deserializeArrayJSON(MiscUtils.readText("LocalData/Server/Accounts.json"), accountsType);}
		catch (Exception e) {Logging.logException(e);}
		
		if (accounts_ == null) accounts_ = new HashMap<String, Account>();
	}
	
	public boolean login(String username, String password)
	{
		if (accounts_.get(username) == null) return false;
		else return accounts_.get(username).eqHash(password);
	}
	public Account getAccount(String username)
	{
		return accounts_.get(username);
	}
	public ArrayList<Account> getAccounts()
	{
		ArrayList<Account> accountList = new ArrayList<Account>();
		
		for (Map.Entry<String, Account> entry : accounts_.entrySet())
		{
			accountList.add(entry.getValue());
		}
		
		return accountList;
	}
	public boolean addAccount(Account account) // Returns true if successful
	{
		accounts_.put(account.username, account);
		MiscUtils.saveText("LocalData/Server/Accounts.json", JSONManager.serializeJSON(accounts_));
		return true; // TODO whitelist / blacklist
	}
	
	public void start(int port)
	{
		serverThread_ = new ServerThread<ClientHandlerThread>(port, template_);
		serverThread_.open();
		serverThread_.start();
		if (serverThread_.isAlive()) Logging.logNotice("Server started on IP " + MiscUtils.getExIp() + "<br> & port " + port);
	}
	public String getName() // Get server name
	{
		return null;
	}
	
	public String getResourceFolder() // Get path for resources
	{
		return "Resources/Server Packs/Default";
	}
	
	public int currentPlayers()
	{
		return 0;
	}
	public int maxPlayers()
	{
		return 10;
	}
}
