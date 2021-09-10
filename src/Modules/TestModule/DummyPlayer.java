// By Iacon1
// Created 04/26/2021
//

package Modules.TestModule;

import GameEngine.GameWorld;
import GameEngine.GraphicsManager;
import GameEngine.KeyBindingManager;
import GameEngine.ClientInfo;
import GameEngine.GameCanvas;
import GameEngine.SpriteEntity;
import GameEngine.SoundManager;
import GameEngine.TransSerializable;
import GameEngine.Configurables.ConfigManager;
import Modules.MektonCore.HexEntity;

public class DummyPlayer extends HexEntity implements TransSerializable
{	
	public DummyPlayer(GameWorld world)
	{
		super(world);
		setSprite("Resources/Server Packs/Default/DummyPlayer.PNG", 0, 0, ConfigManager.getHexWidth(), ConfigManager.getHexHeight());
	}
	
	@Override
	public String getName()
	{
		return "Dummy Player";
	}

	@Override
	public void preSerialize() {}

	@Override
	public void postDeserialize()
	{
	}
}
