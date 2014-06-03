package com.supermanitu.advanceddispensers.proxies;

import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;
import com.supermanitu.advanceddispensers.main.BlockRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers() 
	{
		AdvancedDispensersMod.renderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(AdvancedDispensersMod.renderID, new BlockRenderer());
	}
}
