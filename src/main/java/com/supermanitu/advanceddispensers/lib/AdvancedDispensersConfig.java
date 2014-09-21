package com.supermanitu.advanceddispensers.lib;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AdvancedDispensersConfig 
{
	private boolean enabled;
	private int maxBlockCount;
	
	public AdvancedDispensersConfig(Configuration config, String name)
	{
		Property enabled = config.get(Configuration.CATEGORY_GENERAL, "enable" + name, true);
		enabled.comment = "true if the " + name + " should be enabled";
		this.enabled = enabled.getBoolean(true);
		
		Property maxBlockCount = config.get(Configuration.CATEGORY_GENERAL, "max" + name + "BlockCount", 0);
		maxBlockCount.comment = "set the max amount of Blocks per player of the " + name + ". Set to 0 for unlimited";
		this.maxBlockCount = maxBlockCount.getInt(0);
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public int getMaxBlockCount()
	{
		return maxBlockCount;
	}
}
