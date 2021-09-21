// By Iacon1
// Created 09/12/2021
//

package Modules.HexUtilities;

import Utils.JSONManager;
import Utils.Logging;
import Utils.MiscUtils;

public class HexConfigManager
{
	private static class HexConfig
	{
		protected int hexSize = 19;
	}
	private static HexConfig hexConfig;
	
	public static void init(String server) // Loads from a server folder, or uses the defaults if no path is provided
	{
		String path = null;
		hexConfig = null;
		if (server != null)
		{
			path = "Resources/Server Packs/" + server + "/HexConfig.json";
			hexConfig = new HexConfig();
			hexConfig = JSONManager.deserializeJSON(MiscUtils.readText(path), hexConfig.getClass());
			if (hexConfig == null) // Save the default into there
			{
				Logging.logError("No hex config found for server " + server + ". Generating one...");
				hexConfig = new HexConfig();
				Logging.logError("Done.");
				MiscUtils.saveText(path, JSONManager.serializeJSON(hexConfig));
			}
		}
		else hexConfig = new HexConfig();
	}
	
	public static int multSqrt3(int x)
	{
		return (int) (x * Math.sqrt(3));
	}
	public static int divSqrt3(int x)
	{
		return (int) (x / Math.sqrt(3));
	}
	
	public static int getHexRadius() // Hex radius in pixels
	{
		return hexConfig.hexSize;
	}
	public static int getHexWidth() // Hex width in pixels
	{
		return getHexRadius() * 2;
	}
	public static int getHexHeight() // Hex height in pixels
	{
		return (int) multSqrt3(getHexRadius());
	}
	
}
