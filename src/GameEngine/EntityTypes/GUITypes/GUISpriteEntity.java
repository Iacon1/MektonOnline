// By Iacon1
// Created 09/10/2021
// Has a sprite but doesn't respond to camera

package GameEngine.EntityTypes.GUITypes;

import GameEngine.Point2D;
import GameEngine.ScreenCanvas;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.EntityTypes.SpriteEntity;
import GameEngine.Managers.GraphicsManager;

public abstract class GUISpriteEntity extends SpriteEntity
{
	@Override
	public void render(ScreenCanvas canvas, Point2D camera) 
	{
		canvas.drawImageScaled(texturePath_, pos_, texturePos_, textureSize_);
	}
}
