package com.supermanitu.advanceddispensers.placer;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class PlacerConfig 
{
	private boolean enabled;
	private int tick;
	
	public PlacerConfig(Configuration config)
	{
		Property tick = config.get(Configuration.CATEGORY_GENERAL, "autoCraftingTick", 4);
		tick.comment = "This value is the minimum delay between two activations (20 equals 1 time per second)";
		this.tick = tick.getInt();
		
		Property enabled = config.get(Configuration.CATEGORY_GENERAL, "enableAutoCrafting", true);
		enabled.comment = "true if the automated crafting table should be enabled";
		this.enabled = enabled.getBoolean(true);
	}
	
	public int getTick()
	{
		return tick;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
}
