package com.supermanitu.advanceddispensers.breaker;

import com.supermanitu.advanceddispensers.lib.TileEntityAdvancedDispensers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBreaker extends TileEntityAdvancedDispensers
{
	public TileEntityBreaker()
	{
		super("container.breaker", 9);
	}
}
