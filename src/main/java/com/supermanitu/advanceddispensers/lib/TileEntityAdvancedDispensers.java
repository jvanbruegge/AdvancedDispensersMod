package com.supermanitu.advanceddispensers.lib;

import java.util.Hashtable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityAdvancedDispensers extends TileEntity implements IInventory
{
	protected ItemStack[] inventory;
	private String invName;
	private int invSize, ownerID, maxBlockCount;
	private static Hashtable<Class<? extends TileEntityAdvancedDispensers>, Hashtable<Integer, Integer>> blocksPerPlayer; //The Kind of block itself, the ownerID and the amount of Blocks
	
	public TileEntityAdvancedDispensers(String invName, int invSize)
	{
		this.invName = invName;
		this.invSize = invSize;
		this.inventory = new ItemStack[invSize];
		this.ownerID = -1;
		this.maxBlockCount = 0;
		
		if(blocksPerPlayer == null) blocksPerPlayer = new Hashtable<Class<? extends TileEntityAdvancedDispensers>, Hashtable<Integer, Integer>>();
	}
	
	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	@Override
	public int getSizeInventory() 
	{
		return invSize;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inventory[i];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (this.inventory[slot] != null)
        {
            ItemStack itemstack;

            if (this.inventory[slot].stackSize <= amount)
            {
                itemstack = this.inventory[slot];
                this.inventory[slot] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0)
                {
                    this.inventory[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (this.inventory[slot] != null)
        {
            ItemStack itemstack = this.inventory[slot];
            this.inventory[slot] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		this.inventory[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
	}
	
	@Override
	public String getInventoryName()
	{
		return this.invName;
	}
	
	@Override
	public boolean hasCustomInventoryName() 
	{
		return true;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) 
	{
		return true;
	}
	
	@Override
	public void openInventory() { }

	@Override
	public void closeInventory() { }
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        NBTTagList nbttaglist = tagCompound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length)
            {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        if (tagCompound.hasKey("CustomName", 8))
        {
            this.invName = tagCompound.getString("CustomName");
        }
        
        if(tagCompound.hasKey("Owner", 3))
        {
        	this.ownerID = tagCompound.getInteger("Owner");
        }
    }
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tagCompound.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName())
        {
            tagCompound.setString("CustomName", this.invName);
        }
        
        if(ownerID != -1)
        {
        	tagCompound.setInteger("Owner", ownerID);
        }
    }
	
	public ItemStack[] getInventory()
	{
		return inventory;
	}
	
	public int getOwnerID()
	{
		return ownerID;
	}
	
	public boolean onBlockPlaced(int max, int owner)
	{
		this.maxBlockCount = max;
		this.ownerID = owner;
		
		Hashtable<Integer, Integer> blockCounts = null;
		
		if(blocksPerPlayer.get(this.getClass()) == null)
		{
			blockCounts = new Hashtable<Integer, Integer>();
			blocksPerPlayer.put(this.getClass(), blockCounts);
		}
		else
		{
			blockCounts = blocksPerPlayer.get(this.getClass());
		}
		
		if(maxBlockCount != 0) //User can have infinite Blocks
		{
			if(blockCounts.get(owner) != null && blockCounts.get(owner) < maxBlockCount)
			{
				blockCounts.put(owner, blockCounts.get(owner) + 1);
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	public void onBlockDestroyed(int owner)
	{
		Hashtable<Integer, Integer> blockCounts = blocksPerPlayer.get(this.getClass());
		
		int i = 0;
		if(blockCounts.get(owner) != null)
		{
			i = blockCounts.get(owner);
		}
		if(i - 2 >= 0 && this.ownerID == owner)
		{
			blockCounts.put(owner, i - 2);
		}
	}
}
