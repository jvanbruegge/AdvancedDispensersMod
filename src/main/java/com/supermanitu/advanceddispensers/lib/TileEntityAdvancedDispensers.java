package com.supermanitu.advanceddispensers.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.UUID;

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
	private int invSize, maxBlockCount;
	private UUID ownerID;
	private static Hashtable<Class<? extends TileEntityAdvancedDispensers>, Hashtable<UUID, Integer>> blocksPerPlayer; //The Kind of block itself, the ownerID and the amount of Blocks
	private static boolean read = false, saved = false;
	
	public TileEntityAdvancedDispensers(String invName, int invSize)
	{
		this.invName = invName;
		this.invSize = invSize;
		this.inventory = new ItemStack[invSize];
		this.ownerID = null;
		this.maxBlockCount = 0;
		
		if(blocksPerPlayer == null) blocksPerPlayer = new Hashtable<Class<? extends TileEntityAdvancedDispensers>, Hashtable<UUID, Integer>>();
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
        	String owner = tagCompound.getString("Owner");
        	this.ownerID = UUID.fromString(owner);
        }
        
        if(!read && tagCompound.hasKey("blocksPerPlayer"))
        {
        	read = true;
        	byte[] array = tagCompound.getByteArray("blocksPerPlayer");
        	try
        	{
        		ByteArrayInputStream in = new ByteArrayInputStream(array);
        		ObjectInputStream objIn = new ObjectInputStream(in);
        		
        		blocksPerPlayer = (Hashtable<Class<? extends TileEntityAdvancedDispensers>, Hashtable<UUID, Integer>>) objIn.readObject(); //cast of Doom :D
        		
        		objIn.close();
        		in.close();
        	}
        	catch(IOException e) {e.printStackTrace();} 
        	catch (ClassNotFoundException e) {e.printStackTrace();}
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
        
        if(ownerID != null)
        {
        	tagCompound.setString("Owner", ownerID.toString());
        }
        if(!saved)
        {
        	saved = true;
        	try
            {
            	ByteArrayOutputStream out = new ByteArrayOutputStream();
            	ObjectOutputStream objOut = new ObjectOutputStream(out);

            	objOut.writeObject(blocksPerPlayer);
            	objOut.flush();

            	byte[] byteArray = out.toByteArray();

            	objOut.close();
            	out.close();

            	tagCompound.setByteArray("blocksPerPlayer", byteArray);
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }
	
	public ItemStack[] getInventory()
	{
		return inventory;
	}
	
	public boolean onBlockPlaced(int max, UUID owner)
	{
		this.maxBlockCount = max;
		this.ownerID = owner;
		
		Hashtable<UUID, Integer> blockCounts = null;
		
		if(blocksPerPlayer.get(this.getClass()) == null)
		{
			blockCounts = new Hashtable<UUID, Integer>();
			blocksPerPlayer.put(this.getClass(), blockCounts);
		}
		else
		{
			blockCounts = blocksPerPlayer.get(this.getClass());
		}
		
		if(maxBlockCount != 0) //User can have infinite Blocks
		{
			if(blockCounts.get(owner) == null)
			{
				blockCounts.put(owner, 1);
			}
			else if(blockCounts.get(owner) < maxBlockCount)
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
	
	public void onBlockDestroyed(UUID owner)
	{
		Hashtable<UUID, Integer> blockCounts = blocksPerPlayer.get(this.getClass());
		
		int i = 0;
		if(blockCounts != null && blockCounts.get(owner) != null)
		{
			i = blockCounts.get(owner);
		}
		if(i - 2 >= 0 && this.ownerID == owner)
		{
			blockCounts.put(owner, i - 2);
		}
	}
}
