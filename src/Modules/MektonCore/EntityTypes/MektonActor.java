// By Iacon1
// Created 09/12/2021
// An actor is an entity with standard Mekton actions like shooting and moving and stuff

package Modules.MektonCore.EntityTypes;

import java.util.LinkedList;
import java.util.Map;

import Utils.MiscUtils;
import Utils.SimpleTimer;

import GameEngine.Animation;
import GameEngine.EntityTypes.CommandRunner;
import Modules.BaseModule.Commands.ParsingCommand;
import Modules.BaseModule.Commands.ParsingCommandBank;
import Modules.HexUtilities.HexDirection;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;

import Modules.MektonCore.MektonMap;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;

public abstract class MektonActor extends MapEntity implements CommandRunner
{
	public enum Scale
	{
		human,
		striker,
		mek,
		ship
	}
	
	private float actionPoints;
	private transient SimpleTimer actionTimer;
	protected transient ParsingCommandBank commandBank;
	
	protected enum ActorAnim // Animations
	{
		idle,
		
		walk,
		fly,
		
		attackH2H,
		attackSword,
		attackEMW,
		attackMissile,
		attackGun,
		attackBeam,
		
		blockStandard,
		blockActive,
		blockReactive;
		
		public Animation animation;
	}
	// Commands
	private void moveFunction(Object caller, Map<String, String> parameters, Map<String, Boolean> flags)
	{
		if (caller != this) return; // The only thing that should run *our* moveFunction is *us*
			
		if (parameters.get("q") != null && parameters.get("r") != null)
		{
			AxialHexCoord3D target = new AxialHexCoord3D(Integer.valueOf(parameters.get("q")), Integer.valueOf(parameters.get("r")), hexPos.z);
			movePath(mapToken.get().pathfind(hexPos, target), 2);
		}
		else
		{
			if (flags.get("north") && flags.get("west")) moveDirectionalAct(HexDirection.northWest, 1, 2);
			else if (flags.get("north") && flags.get("east")) moveDirectionalAct(HexDirection.northEast, 1, 2);
			else if (flags.get("north")) moveDirectionalAct(HexDirection.north, 1, 2);

			else if (flags.get("south") && flags.get("west")) moveDirectionalAct(HexDirection.southWest, 1, 2);
			else if (flags.get("south") && flags.get("east")) moveDirectionalAct(HexDirection.southEast, 1, 2);
			else if (flags.get("south")) moveDirectionalAct(HexDirection.south, 1, 2);
			
			if (flags.get("up")) moveDirectionalAct(HexDirection.up, 1, 2);
			if (flags.get("down")) moveDirectionalAct(HexDirection.down, 1, 2);
		}
	}

	protected void registerCommands()
	{
		ParsingCommand moveCommand = new ParsingCommand(
				new String[] {"move", "Move"},
				"", // TODO
				new String[][] {new String[] {"q"}, new String[] {"r"}},
				new String[][] {
					new String[]{"north", "North", "n"},
					new String[]{"west", "West", "w"},
					new String[]{"east", "East", "e"},
					new String[]{"south", "South", "s"},
					new String[]{"up", "Up", "u"},
					new String[]{"down", "Down", "d"}},
				(caller, parameters, flags) -> {moveFunction(caller, parameters, flags);});
		commandBank.registerCommand(moveCommand);
	}
		
	// Protected abstracts
		
	protected abstract int getMA(); // Speed in human hexes	
		
	// Constructor
	
	public MektonActor()
	{
		super();
		actionPoints = 0f;
		commandBank = new ParsingCommandBank();
		
		registerCommands();
	}
	public MektonActor(String owner, MektonMap map)
	{
		super(owner, map);
		actionTimer = new SimpleTimer();
		commandBank = new ParsingCommandBank();
		
		resetActionPoints();
		
		registerCommands();
	}

	// Actions system
	
	public void resetActionPoints()
	{
		actionPoints = 2f;
		actionTimer.start();
		resume();
	}
	public float remainingActions()
	{
		return actionPoints;
	}
	public boolean takeAction(float cost)
	{
		if (actionPoints >= cost - MiscUtils.floatTolerance) // Float accuracy seems to get weird here
		{
			actionPoints = Math.max(actionPoints - cost, 0);
			return true;
		}
		else return false;
	}

	// Public abstracts
	
	public abstract void takeDamage(Damage damage);
	public abstract void defend(MektonActor aggressor);
	public abstract void attack(MektonActor defender);
	
	// Conscious movement
	
	public void moveTargetHexAct(AxialHexCoord3D target, int speed)
	{
		float moveCost = Math.abs((float) hexPos.distance(target)) / (float) getMA(); // TODO assumes walking
		if (takeAction(moveCost)) super.moveTargetHex(target, speed);
		else pause();
	}
	public void moveDeltaHexAct(AxialHexCoord3D delta, int speed)
	{
		moveTargetHexAct(hexPos.rAdd(delta), speed);
	}
	public void moveDirectionalAct(HexDirection dir, int distance, int speed)
	{
		AxialHexCoord3D delta = hexPos.getUnitVector(dir).rMultiply(distance);
		setDirection(dir);
		
		moveDeltaHexAct(delta, speed);
	}
	
	
	
	// Overridden functions
	
	@Override
	public void movePath(LinkedList<AxialHexCoord3D> path, int speed)
	{
		this.path = path;
		moveTargetHexAct(path.getFirst(), speed);
	}
	@Override
	public void updatePath()
	{
		if (this.path != null && this.hexPos == this.path.getFirst()) // Ready for next step of path
		{
			this.path.remove();
			if (this.path.isEmpty()) this.path = null;
			else moveTargetHexAct(path.getFirst(), baseSpeed);
		}
		else if (this.path != null)
		{
			moveTargetHexAct(path.getFirst(), baseSpeed);
		}
	}
	
	@Override
	public void onResume()
	{
		updatePath();
	}
	
	@Override
	public void update()
	{
		super.update();
		if (actionTimer.checkTime(10000))
		{
			resetActionPoints();
		}
	}
	
	@Override
	public boolean runCommand(String... words)
	{
		if (commandBank.recognizes(words[0]))
		{
			commandBank.execute(this, words);
			return true;
			
		}
		else return false;
	}
}