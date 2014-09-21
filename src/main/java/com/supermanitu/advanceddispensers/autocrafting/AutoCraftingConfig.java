package com.supermanitu.advanceddispensers.autocrafting;

import com.supermanitu.advanceddispensers.lib.AdvancedDispensersConfig;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AutoCraftingConfig extends AdvancedDispensersConfig
{
	private int tick;
	
	public AutoCraftingConfig(Configuration config)
	{
		super(config, "AutomatedCraftingTable");
		
		Property tick = config.get(Configuration.CATEGORY_GENERAL, "autoCraftingTick", 4);
		tick.comment = "This value is the minimum delay between two activations (20 equals 1 time per second)";
		this.tick = tick.getInt();
	}
	
	public int getTick()
	{
		return tick;
	}
}
