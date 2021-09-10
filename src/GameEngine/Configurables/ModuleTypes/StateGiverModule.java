// By Iacon1
// Created 09/10/2021
// Provides state factories

package GameEngine.Configurables.ModuleTypes;

import Net.StateFactory;

public interface StateGiverModule
{
	public StateFactory clientFactory(); // Client factory
	public StateFactory handlerFactory(); // Handler factory
}