package com.supermanitu.advanceddispensers.user;

import com.supermanitu.advanceddispensers.lib.AdvancedDispensersLib;
import com.supermanitu.advanceddispensers.main.EntityFakePlayer;
import com.supermanitu.advanceddispensers.main.TileEntityAdvancedDispensers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityUser extends TileEntityAdvancedDispensers
{
	private EntityFakePlayer fakePlayer;
	
	public TileEntityUser(World world)
	{
		super("container.user", 9);
		fakePlayer = new EntityFakePlayer(world, this, xCoord, yCoord, zCoord, world.getBlockMetadata(xCoord, yCoord, zCoord));
	}
	
	public void useItem(World world, int x, int y, int z, int meta, int slot)
	{
		Item item = this.getStackInSlot(slot).getItem();
		
		int i = x, j = y, k = z, c = 0;
		
		do
		{
			i = AdvancedDispensersLib.INSTANCE.getI(meta, i);
			j = AdvancedDispensersLib.INSTANCE.getJ(meta, j);
			k = AdvancedDispensersLib.INSTANCE.getK(meta, k);
			c++;
		}while(c < fakePlayer.getRange() && !world.getBlock(i, j, k).equals(Blocks.air));
		
		int side;
		
		if(meta % 2 == 0) side = meta + 1;
		else side = meta - 1;
		
		if(item.onItemUseFirst(this.getStackInSlot(slot), fakePlayer, world, x, y, z, side, 0.5f, 1.0f, 0.5f))
		{
			//Extra Stuff maybe
		}
		else if(item.onItemUse(this.getStackInSlot(slot), fakePlayer, world, i, j, k, side, 0.5f, 1.0f, 0.5f))
		{
			//Extra Stuff maybe
		}
		else
		{
			this.setInventorySlotContents(slot, item.onItemRightClick(this.getStackInSlot(slot), world, fakePlayer));
			if(this.getStackInSlot(slot).stackSize == 0) this.setInventorySlotContents(slot, null);
		}
	}
}