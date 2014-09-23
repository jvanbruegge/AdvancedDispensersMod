package com.supermanitu.advanceddispensers.breaker;

import com.supermanitu.advanceddispensers.lib.TileEntityAdvancedDispensers;
import com.supermanitu.advanceddispensers.main.EntityFakePlayer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBreaker extends TileEntityAdvancedDispensers
{
	private EntityFakePlayer fakePlayer;
	
	public TileEntityBreaker()
	{
		super("container.breaker", 9);
	}
	
	public boolean canSilkHarvest(Block block, World world, int i, int j, int k, int blockmeta)
	{
		if(fakePlayer == null)
		{
			fakePlayer = new EntityFakePlayer(worldObj, this, xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		}
		
		return block.canSilkHarvest(world, fakePlayer, i, j, k, blockmeta);
	}
}
