// By Iacon1
// Created 09/17/2021
// A test

package Modules.TestModule;

import GameEngine.GameInfo;
import GameEngine.ImageSprite;
import GameEngine.EntityTypes.GUITypes.GUISpriteEntity;
import Modules.HexUtilities.HexConfigManager;

public class TestMenu extends GUISpriteEntity
{
	public TestMenu(String owner)
	{
		super(owner);
		setSprite(new ImageSprite(GameInfo.getServerPackResource("DummyPlayer.PNG")));
		setSpriteParams(0, 0, HexConfigManager.getHexWidth(), 2 * HexConfigManager.getHexHeight());
		align(AlignmentPoint.northEast, null, AlignmentPoint.northEast);
	}
	
	@Override
	public void onStart()
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void onStop()
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void onAnimStop()
	{
		// TODO Auto-generated method stub

	}
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}


}