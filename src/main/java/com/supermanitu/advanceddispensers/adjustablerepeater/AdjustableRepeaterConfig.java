package com.supermanitu.advanceddispensers.adjustablerepeater;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AdjustableRepeaterConfig 
{
	private boolean enabled;
		
	public AdjustableRepeaterConfig(Configuration config)
	{	
		Property enabled = config.get(Configuration.CATEGORY_GENERAL, "enableAdjustableRepeater", true);
		enabled.comment = "true if the adjustable repeater should be enabled";
		this.enabled = enabled.getBoolean(true);
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
}
