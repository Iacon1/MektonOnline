// By Iacon1
// Created 06/17/2021
// Basic functionality & utilities not covered by the engine (i. e. might be changed by more unusual applications of the engine)

package Modules.BaseModule;

import GameEngine.Configurables.ModuleTypes.Module;
import GameEngine.Configurables.ModuleTypes.StateGiverModule;
import GameEngine.Configurables.ModuleTypes.Module.ModuleConfig;
import GameEngine.Net.StateFactory;
import Modules.BaseModule.ClientStates.ClientStateFactory;
import Modules.BaseModule.HandlerStates.HandlerStateFactory;

public class BaseModule implements Module, StateGiverModule
{

	@Override
	public ModuleConfig getModuleConfig()
	{
		ModuleConfig config = new ModuleConfig();
		config.moduleName = "MtO Base Module";
		config.moduleVersion = "V0.X";
		config.moduleDescription = "Miscellaneous functionality that isn't\nreally general enough in application to be in the engine proper.";
		
		return config;
	}

	@Override
	public void initModule()
	{
		ChatLog.init();
	}
	
	@Override
	public StateFactory clientFactory()
	{
		return new ClientStateFactory();
	}

	@Override
	public StateFactory handlerFactory()
	{
		return new HandlerStateFactory();
	}
}
