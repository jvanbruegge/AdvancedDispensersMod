package com.supermanitu.advanceddispensers.user;

import java.util.List;

import com.supermanitu.advanceddispensers.lib.AdvancedDispensersLib;
import com.supermanitu.advanceddispensers.lib.TileEntityAdvancedDispensers;
import com.supermanitu.advanceddispensers.main.EntityFakePlayer;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityUser extends TileEntityAdvancedDispensers
{
	private EntityFakePlayer fakePlayer;
	
	public TileEntityUser()
	{
		super("container.user", 9);
		fakePlayer = null;
	}
	
	public boolean useItem(int slot)
	{
		if(fakePlayer == null)
		{
			fakePlayer = new EntityFakePlayer(worldObj, this, xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		}

		Item item = this.getStackInSlot(slot).getItem();

		int i = xCoord, j = yCoord, k = zCoord, meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		i = AdvancedDispensersLib.INSTANCE.getI(meta, i);
		j = AdvancedDispensersLib.INSTANCE.getJ(meta, j);
		k = AdvancedDispensersLib.INSTANCE.getK(meta, k);
		
		Block user = worldObj.getBlock(xCoord, yCoord, zCoord);
		int userMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		AxisAlignedBB rect = null;
		
		int deltaX = xCoord - i, deltaY = yCoord - j, deltaZ = zCoord - k;
		
		if(deltaX > 0 || deltaY > 0 || deltaZ > 0)
		{
			rect = AxisAlignedBB.getBoundingBox(xCoord - deltaX*4 - 0.5, yCoord - deltaY*4 - 0.5, zCoord - deltaZ*4 - 0.5, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
		}
		else
		{
			rect = AxisAlignedBB.getBoundingBox(xCoord - 0.5, yCoord - 0.5, zCoord - 0.5, xCoord - deltaX*4 + 0.5, yCoord - deltaY*4 + 0.5, zCoord - deltaZ*4 + 0.5);
		}
		List<EntityLivingBase> near = worldObj.getEntitiesWithinAABB(EntitySheep.class, rect);
		
		for(EntityLivingBase base : near)
		{
			if(item.itemInteractionForEntity(this.getStackInSlot(slot), fakePlayer, base))
			{
				return true;
			}
		}

		if(item.onItemUseFirst(this.getStackInSlot(slot), fakePlayer, worldObj, i, j-1, k, 1, 0.5f, 0.5f, 0.5f))
		{
			//Extra Stuff maybe
		}
		else if(item.onItemUse(this.getStackInSlot(slot), fakePlayer, worldObj, i, j-1, k, 1, 0.5f, 0.5f, 0.5f))
		{
			//Extra Stuff maybe
		}
		else
		{
			this.setInventorySlotContents(slot, item.onItemRightClick(this.getStackInSlot(slot), worldObj, fakePlayer));
		}
		AdvancedDispensersLib.INSTANCE.deleteEmptyStacks(this);
		
		return true;
	}
}