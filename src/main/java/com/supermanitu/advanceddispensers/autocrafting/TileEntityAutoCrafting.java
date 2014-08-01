package com.supermanitu.advanceddispensers.autocrafting;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityAutoCrafting extends TileEntity implements ISidedInventory
{
	private ItemStack[] inventory = new ItemStack[34];
	private String invName;
	
	public TileEntityAutoCrafting()
	{
		this.invName = "container.autoCrafting";
	}

	@Override
	public int getSizeInventory() 
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return inventory[slot];
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
	public int getInventoryStackLimit() 
	{
		return 64;
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
    }

	
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[]{10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33};
	}
	

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) 
	{
		if(slot > 9 && slot < 25) return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		if(slot > 24) return true;
		return false;
	}
	
	public void craft()
	{
		if(inventory[9] != null) //Check if recipe available
		{
			if(enoughForRecipe()) //Check if there is enough Material for crafting
			{
				if(spaceInOutput()) //Check if there is enough space in the Output slots
				{
					putResultInOutput();
					decreaseInputStacks();
				}
			}
		}
	}
	
	private void putResultInOutput()
	{
		ItemStack stack = inventory[9].copy();
		
		if(containsItem(stack))
		{
			putStack(stack);
		}
		else
		{
			putInEmptySlot(stack);
		}
	}
	
	private void putStack(ItemStack stack)
	{
		int cp = stack.stackSize;
		
		for(int i = 25; i < 34; i++)
		{
			if(inventory[i] != null && inventory[i].getItem().equals(stack.getItem()) && inventory[i].getItemDamage() == stack.getItemDamage() && inventory[i].stackSize <= inventory[i].getMaxStackSize() - stack.stackSize)
			{
				inventory[i].stackSize += stack.stackSize;
				return;
			}
			else if(inventory[i] != null && inventory[i].stackSize < inventory[i].getMaxStackSize() && inventory[i].getItem().equals(stack.getItem()) && inventory[i].getItemDamage() == stack.getItemDamage())
			{
				for(int j = 0; j < cp; j++)
				{
					if(inventory[i].stackSize == inventory[i].getMaxStackSize())
					{
						if(containsItem(stack))
						{
							System.out.println("putStack: " + stack.stackSize);
							putStack(stack);
						}
						else
						{
							System.out.println("putInEmptySlot: " + stack.stackSize);
							putInEmptySlot(stack);
						}
						return;
					}
					if(stack.stackSize == 0) return;
					stack.stackSize--;
					inventory[i].stackSize++;
					System.out.println("Stack.stacksize: " + stack.stackSize);
				}
			}
		}
	}
	
	private void putInEmptySlot(ItemStack stack) 
	{
		System.out.println("Stack.Stacksize: " + stack.stackSize);
		for(int i = 25; i < 34; i++)
		{
			if(inventory[i] == null)
			{
				inventory[i] = stack;
				return;
			}
		}
	}

	private boolean containsItem(ItemStack stack)
	{
		for(int i = 25; i < 34; i++)
		{
			if(inventory[i] != null && inventory[i].stackSize < inventory[i].getMaxStackSize() && inventory[i].getItem().equals(stack.getItem()) && inventory[i].getItemDamage() == stack.getItemDamage())
			{
				return true;
			}
		}
		return false;
	}

	private void decreaseInputStacks()
	{
		HashMap<Item, Integer> materials = new HashMap<Item, Integer>();
		
		for(int i = 0; i < 9; i++)
		{
			if(inventory[i] != null && !materials.containsKey(inventory[i].getItem()))
			{
				materials.put(inventory[i].getItem(), 1);
			}
			else if(inventory[i] != null)
			{
				materials.put(inventory[i].getItem(), materials.get(inventory[i].getItem()) + 1);
			}
		}
		
		HashMap<Item, Integer> cp = (HashMap<Item, Integer>) materials.clone();
		
		for(int i = 10; i < 25; i++)
		{
			for(Entry<Item, Integer> entry : cp.entrySet())
			{
				if(inventory[i] != null && inventory[i].getItem().equals(entry.getKey()))
				{
					if(materials.get(entry.getKey()) > 0 && inventory[i].stackSize >= materials.get(entry.getKey()))
					{
						inventory[i].stackSize -= materials.get(entry.getKey());
						if(inventory[i].stackSize == 0) inventory[i] = null;
						materials.put(entry.getKey(), 0);
					}
					else if(materials.get(entry.getKey()) > 0)
					{
						int k = materials.get(entry.getKey());
						for(int j = 0; j < k; j++)
						{
							materials.put(entry.getKey(), materials.get(entry.getKey()) - 1);
							inventory[i].stackSize--;
							if(inventory[i].stackSize == 0)
							{
								inventory[i] = null;
								break;
							}
							if(materials.get(entry.getKey()) == 0) break;
						}
					}
				}
				boolean allZero = true;
				for(int k : materials.values())
				{
					if(k > 0) allZero = false;
				}
				if(allZero) return;
			}
		}
	}
	
	private boolean spaceInOutput()
	{
		ItemStack stack = inventory[9].copy();
		
		for(int i = 25; i < 34; i++) //Output field
		{
			if(inventory[i] == null || (inventory[i].getItem().equals(stack.getItem()) && inventory[i].getItemDamage() == stack.getItemDamage() && inventory[i].stackSize <= inventory[i].getMaxStackSize() - stack.stackSize))
			{
				return true;
			}
			else if(inventory[i].getItem().equals(stack.getItem()))
			{
				stack.stackSize -= inventory[i].getMaxStackSize() - inventory[i].stackSize;
			}
			if(stack == null || stack.stackSize <= 0)
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean enoughForRecipe()
	{
		HashMap<Item, Integer> materialsNeeded = new HashMap<Item, Integer>();
		
		for(int i = 0; i < 9; i++)
		{
			if(inventory[i] != null && !materialsNeeded.containsKey(inventory[i].getItem()))
			{
				materialsNeeded.put(inventory[i].getItem(), 1); //adding new Material
			}
			else if(inventory[i] != null)
			{
				materialsNeeded.put(inventory[i].getItem(), materialsNeeded.get(inventory[i].getItem()) + 1); //increasing amount
			}
		}
		
		HashMap<Item, Integer> cp = (HashMap<Item, Integer>) materialsNeeded.clone();
		
		for(int i = 10; i < 25; i++) //Input field
		{
			for(Entry<Item, Integer> entry : cp.entrySet())
			{
				if(inventory[i] != null && materialsNeeded.get(inventory[i].getItem()) != null && materialsNeeded.get(inventory[i].getItem()) > 0 && inventory[i].getItem().equals(entry.getKey()))
				{
					if(inventory[i].stackSize >= entry.getValue())
					{
						materialsNeeded.put(inventory[i].getItem(), 0);
					}
					else
					{
						materialsNeeded.put(inventory[i].getItem(), materialsNeeded.get(inventory[i].getItem()) - inventory[i].stackSize);
					}
				}
			}
		}
		for(int i : materialsNeeded.values())
		{
			if(i > 0) return false;
		}
		return true;
	}
}