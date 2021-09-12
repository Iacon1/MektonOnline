// By Iacon1
// Created 09/11/2021
// Gets input for the character

package Modules.TestModule;

import java.awt.event.KeyEvent;

import GameEngine.GameCanvas;
import GameEngine.GameInfo;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.EntityTypes.InputGetter;
import GameEngine.Managers.GraphicsManager;
import Modules.MektonCore.HexEntity;

public class TestHandle extends GameEntity implements InputGetter
{
	@Override
	public void onMouseClick(int mX, int mY, int button)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMousePress(int mX, int mY, int button)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseRelease(int mX, int mY, int button)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPress(int code)
	{
		switch (code)
		{
		case KeyEvent.VK_Q: GameInfo.setCommand("move nw"); break;
		case KeyEvent.VK_W: GameInfo.setCommand("move no"); break;
		case KeyEvent.VK_E: GameInfo.setCommand("move ne"); break;
		case KeyEvent.VK_A: GameInfo.setCommand("move sw"); break;
		case KeyEvent.VK_S: GameInfo.setCommand("move so"); break;
		case KeyEvent.VK_D: GameInfo.setCommand("move se"); break;
		case KeyEvent.VK_SPACE: GameInfo.setCommand("move up"); break;
		case KeyEvent.VK_SHIFT: GameInfo.setCommand("move do"); break;
		}
	}

	@Override
	public void onKeyRelease(int code)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {}
	@Override
	public void render(GameCanvas canvas)
	{
		HexEntity possessee = (HexEntity) GameInfo.getWorld().getEntity(GameInfo.getPossessee());
		canvas.drawText("Height: " + possessee.getHZ(), "MicrogrammaNormalFix.TTF", GraphicsManager.getColor(255, 0, 0), 0, 0, 32);
	}

}