package com.supermanitu.advanceddispensers.autocrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;

public class InventoryAutoCraftResult extends InventoryCraftResult
{
    /** A list of one item containing the result of the crafting formula */
    private static final String __OBFID = "CL_00001760";
    private TileEntityAutoCrafting tileEntityAutoCrafting;

    public InventoryAutoCraftResult(TileEntityAutoCrafting tileEntity)
    {
    	this.tileEntityAutoCrafting = tileEntity;
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 1;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return tileEntityAutoCrafting.getStackInSlot(9);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return "Result";
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (tileEntityAutoCrafting.getStackInSlot(9) != null)
        {
            ItemStack itemstack = tileEntityAutoCrafting.getStackInSlot(9);
            tileEntityAutoCrafting.setInventorySlotContents(9, null);
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (tileEntityAutoCrafting.getStackInSlot(9) != null)
        {
            ItemStack itemstack = tileEntityAutoCrafting.getStackInSlot(9);
            tileEntityAutoCrafting.setInventorySlotContents(9, null);
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
    	tileEntityAutoCrafting.setInventorySlotContents(9, par2ItemStack);
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {}

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }
}