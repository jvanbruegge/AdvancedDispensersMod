package com.supermanitu.advanceddispensers.main;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class AdvancedDispensersTab extends CreativeTabs
{
	public AdvancedDispensersTab(String label) 
	{
		super(label);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() 
	{
		return Item.getItemFromBlock(AdvancedDispensersMod.blockUser);
	}

}
