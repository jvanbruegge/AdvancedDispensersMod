package com.supermanitu.advanceddispensers.user;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class UserConfig 
{
	private boolean enabled, fakeEnabled;
	private int tick;
	
	public UserConfig(Configuration config)
	{
		Property tick = config.get(Configuration.CATEGORY_GENERAL, "userTick", 4);
		tick.comment = "This value is the minimum delay between two activations (20 equals 1 time per second)";
		this.tick = tick.getInt();
		
		Property enabled = config.get(Configuration.CATEGORY_GENERAL, "enableUser", true);
		enabled.comment = "true if the User should be enabled";
		this.enabled = enabled.getBoolean(true);
		
		Property fakeEnabled = config.get(Configuration.CATEGORY_GENERAL, "enableFakePlayer", true);
		enabled.comment = "true if the User should have a fake player, nessecary for item right click";
		this.fakeEnabled = fakeEnabled.getBoolean(true);
	}
	
	public int getTick()
	{
		return tick;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public boolean isFakePlayerEnabled()
	{
		return fakeEnabled;
	}
}
