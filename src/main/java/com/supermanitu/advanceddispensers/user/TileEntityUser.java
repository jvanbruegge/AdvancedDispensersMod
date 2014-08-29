package com.supermanitu.advanceddispensers.user;

import com.supermanitu.advanceddispensers.main.EntityFakePlayer;
import com.supermanitu.advanceddispensers.main.TileEntityAdvancedDispensers;

import net.minecraft.entity.player.EntityPlayer;
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
	
	public TileEntityUser()
	{
		super("container.user", 9);
		this.fakePlayer = null;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() { }

	@Override
	public void closeInventory() { }

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) 
	{
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
    }
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
    }
	
	public void useItem(World world, int x, int y, int z, int meta, int slot, int i, int j, int k)
	{
		Item item = this.getStackInSlot(slot).getItem();
		
		if(fakePlayer == null) fakePlayer = new EntityFakePlayer(world, (TileEntityAdvancedDispensers) world.getTileEntity(x, y, z), x, y, z, meta);
		
		int side;
		
		if(meta % 2 == 0) side = meta + 1;
		else side = meta - 1;
		
		if(item.onItemUseFirst(this.getStackInSlot(slot), fakePlayer, world, x, y, z, side, 0.5f, 1.0f, 0.5f))
		{
			System.out.println("UseFirst");
		}
		else if(item.onItemUse(this.getStackInSlot(slot), fakePlayer, world, i, j-1, k, side, 0.5f, 1.0f, 0.5f))
		{
			System.out.println("Use");
		}
		else
		{
			this.setInventorySlotContents(slot, item.onItemRightClick(this.getStackInSlot(slot), world, fakePlayer));
			if(this.getStackInSlot(slot).stackSize == 0) this.setInventorySlotContents(slot, null);
		}
	}
}