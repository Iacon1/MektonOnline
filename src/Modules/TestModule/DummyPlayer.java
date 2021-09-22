// By Iacon1
// Created 04/26/2021
//

package Modules.TestModule;

import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;
import Modules.MektonCore.MektonMap;
import Modules.MektonCore.EntityTypes.MapEntity;
import Modules.MektonCore.EntityTypes.MektonActor;

import java.awt.Color;
import java.awt.event.KeyEvent;

import GameEngine.GameInfo;
import GameEngine.Point2D;
import GameEngine.ScreenCanvas;
import GameEngine.EntityTypes.CommandRunner;
import GameEngine.EntityTypes.InputGetter;

import Modules.HexUtilities.HexConfigManager;
import Modules.HexUtilities.HexDirection;

public class DummyPlayer extends MektonActor implements InputGetter, CommandRunner
{	
	public DummyPlayer()
	{
		super();
		setSprite("Resources/Server Packs/Default/DummyPlayer.PNG", 0, 0, HexConfigManager.getHexWidth(), HexConfigManager.getHexHeight());
	}
	public DummyPlayer(String owner, MektonMap map)
	{
		super(owner, map);
		setSprite("Resources/Server Packs/Default/DummyPlayer.PNG", 0, 0, HexConfigManager.getHexWidth(), HexConfigManager.getHexHeight());
	}
	
	@Override
	public String getName()
	{
		return "Dummy Player";
	}
	
	@Override
	public void onAnimStop() {}

	@Override
	public void onMouseClick(int mX, int mY, int button)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMousePress(int mX, int mY, int button)
	{
		AxialHexCoord3D point = mapToken.get().fromPixel(new Point2D(mX, mY));
		if (button == 0) GameInfo.setCommand("moveMouse " + point.q + " " + point.r + " " + point.z);
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
	public void onStart()
	{
	}
	@Override
	public void onStop()
	{
		super.onStop();
	}

	@Override
	public void runCommand(String[] params)
	{
		{
			switch (params[0])
			{
			case "moveMouse":
					AxialHexCoord3D target = new AxialHexCoord3D(Integer.valueOf(params[1]), Integer.valueOf(params[2]), hexPos.z);
					movePath(mapToken.get().pathfind(hexPos, target), 2);
			case "move": // TODO objects with MA
				switch (params[1])
				{
				case "north": case "no": case "n":
					moveDirectional(HexDirection.north, 1, 2);
					break;
				case "northwest": case "nw":
					moveDirectional(HexDirection.northWest, 1, 2);
					break;
				case "northeast": case "ne":
					moveDirectional(HexDirection.northEast, 1, 2);
					break;
					
				case "south": case "so": case "s":
					moveDirectional(HexDirection.south, 1, 2);
					break;
				case "southwest": case "sw":
					moveDirectional(HexDirection.southWest, 1, 2);
					break;
				case "southeast": case "se":
					moveDirectional(HexDirection.southEast, 1, 2);
					break;
					
				case "up":
					moveDirectional(HexDirection.up, 1, 2);
					break;
				case "down": case "do":
					moveDirectional(HexDirection.down, 1, 2);
					break;
				}
				break;
			}
		}
	}

	@Override
	public void render(ScreenCanvas canvas, Point2D camera)
	{
		super.render(canvas, camera);
		
		if (isPossessee()) canvas.drawText("Action points: " + remainingActions(), "MicrogrammaNormalFix.TTF", Color.red, new Point2D(0, 0), 50);
	}
	@Override
	public void onPause()
	{
		// TODO Auto-generated method stub
		
	}
}
