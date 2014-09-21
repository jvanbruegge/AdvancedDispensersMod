package com.supermanitu.advanceddispensers.user;

import com.supermanitu.advanceddispensers.lib.AdvancedDispensersLib;
import com.supermanitu.advanceddispensers.lib.TileEntityAdvancedDispensers;
import com.supermanitu.advanceddispensers.main.EntityFakePlayer;

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
	
	public TileEntityUser()
	{
		super("container.user", 9);
		fakePlayer = null;
	}
	
	@Override
	public void setWorldObj(World world)
	{
		super.setWorldObj(world);
	}
	
	public void useItem(int slot)
	{
		if(fakePlayer == null)
		{
			fakePlayer = new EntityFakePlayer(worldObj, this, xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		}
		
		Item item = this.getStackInSlot(slot).getItem();
		
		int i = xCoord, j = yCoord, k = zCoord, c = 0, meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		do
		{
			i = AdvancedDispensersLib.INSTANCE.getI(meta, i);
			j = AdvancedDispensersLib.INSTANCE.getJ(meta, j);
			k = AdvancedDispensersLib.INSTANCE.getK(meta, k);
			c++;
		}while(c < fakePlayer.getRange() && !worldObj.getBlock(i, j, k).equals(Blocks.air));
		
		int side;
		
		if(meta % 2 == 0) side = meta + 1;
		else side = meta - 1;
		
		if(item.onItemUseFirst(this.getStackInSlot(slot), fakePlayer, worldObj, i, j, k, side, 0.5f, 0.5f, 0.5f))
		{
			//Extra Stuff maybe
		}
		else if(item.onItemUse(this.getStackInSlot(slot), fakePlayer, worldObj, i, j, k, side, 0.5f, 0.5f, 0.5f))
		{
			//Extra Stuff maybe
		}
		else
		{
			this.setInventorySlotContents(slot, item.onItemRightClick(this.getStackInSlot(slot), worldObj, fakePlayer));
			if(this.getStackInSlot(slot).stackSize == 0) this.setInventorySlotContents(slot, null);
		}
	}
}