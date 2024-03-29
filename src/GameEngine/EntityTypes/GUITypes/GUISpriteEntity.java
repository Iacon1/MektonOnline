// By Iacon1
// Created 09/10/2021
// Has a sprite but doesn't respond to camera
// Has an owner, and won't appear to other players

package GameEngine.EntityTypes.GUITypes;

import GameEngine.GameInfo;
import GameEngine.IntPoint2D;
import GameEngine.Graphics.Camera;
import GameEngine.Graphics.ScreenCanvas;
import GameEngine.EntityTypes.SpriteEntity;
import GameEngine.Server.Account;

public abstract class GUISpriteEntity extends SpriteEntity
{
	private int ownerID = -1;
	
	public GUISpriteEntity()
	{
		super();
	}

	public void setOwnerID(int ownerID)
	{
		this.ownerID = ownerID;
	}
	public int getOwnerID()
	{
		return ownerID;
	}
	public Account getOwner()
	{
		return GameInfo.getServer().getAccount(getOwnerID());
	}
	@Override
	public void render(ScreenCanvas canvas, Camera camera) 
	{
		if (sprite != null) sprite.render(canvas, getPos().add(spriteOff));
	}

}
