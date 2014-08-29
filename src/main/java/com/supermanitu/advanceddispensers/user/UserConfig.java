package com.supermanitu.advanceddispensers.user;

import com.supermanitu.advanceddispensers.lib.AdvancedDispensersConfig;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class UserConfig extends AdvancedDispensersConfig
{
	private boolean fakeEnabled;
	private int tick;
	
	public UserConfig(Configuration config)
	{
		super(config, "User");
		
		Property tick = config.get(Configuration.CATEGORY_GENERAL, "userTick", 4);
		tick.comment = "This value is the minimum delay between two activations (20 equals 1 time per second)";
		this.tick = tick.getInt();
		
		Property fakeEnabled = config.get(Configuration.CATEGORY_GENERAL, "enableFakePlayer", true);
		fakeEnabled.comment = "true if the User should have a fake player, nessecary for item right click";
		this.fakeEnabled = fakeEnabled.getBoolean(true);
	}
	
	public int getTick()
	{
		return tick;
	}
	
	public boolean isFakePlayerEnabled()
	{
		return fakeEnabled;
	}
}
