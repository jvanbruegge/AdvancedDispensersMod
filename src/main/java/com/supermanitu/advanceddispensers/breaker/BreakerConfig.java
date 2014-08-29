package com.supermanitu.advanceddispensers.breaker;

import com.supermanitu.advanceddispensers.lib.AdvancedDispensersConfig;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class BreakerConfig extends AdvancedDispensersConfig
{
	private boolean enchantmentsEnabled;
	private int tick;
	
	public BreakerConfig(Configuration config)
	{
		super(config, "Breaker");
		
		Property tick = config.get(Configuration.CATEGORY_GENERAL, "breakerTick", 4);
		tick.comment = "This value is the minimum delay between two activations (20 equals 1 time per second)";
		this.tick = tick.getInt();
		
		Property enchantmentsEnabled = config.get(Configuration.CATEGORY_GENERAL, "enableEnchantments", true);
		enchantmentsEnabled.comment = "true if the enchantments of the breaker should be enabled";
		this.enchantmentsEnabled = enchantmentsEnabled.getBoolean(true);
	}
	
	public int getTick()
	{
		return tick;
	}
	
	public boolean areEnchantmentsEnabled()
	{
		return enchantmentsEnabled;
	}
}
