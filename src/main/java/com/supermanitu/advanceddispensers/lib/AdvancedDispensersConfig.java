package com.supermanitu.advanceddispensers.lib;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AdvancedDispensersConfig 
{
	private boolean enabled;
	
	public AdvancedDispensersConfig(Configuration config, String name)
	{
		Property enabled = config.get(Configuration.CATEGORY_GENERAL, "enable" + name, true);
		enabled.comment = "true if the " + name + " should be enabled";
		this.enabled = enabled.getBoolean(true);
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
}
