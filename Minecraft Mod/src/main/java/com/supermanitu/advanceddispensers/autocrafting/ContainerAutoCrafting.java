package com.supermanitu.advanceddispensers.autocrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;

public class ContainerAutoCrafting extends Container
{
    private TileEntityAutoCrafting tileEntityAutoCrafting;
    private static final String __OBFID = "CL_00001763";

    public ContainerAutoCrafting(IInventory inventory, TileEntityAutoCrafting tileEntityAutoCrafting)
    {
        this.tileEntityAutoCrafting = tileEntityAutoCrafting;
        
        int counter = 0;

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.addSlotToContainer(new Slot(tileEntityAutoCrafting, counter, 30 + j * 18, 17 + i * 18));
                counter++;
            }
        }
        
        this.addSlotToContainer(new Slot(tileEntityAutoCrafting, counter, 124, 35));
        counter++;
        
        for(int i = 0; i < 3; i++)
        {
        	for(int j = 0; j < 5; j++)
        	{
        		this.addSlotToContainer(new Slot(tileEntityAutoCrafting, counter, 8 + j*18, 89 + i*18));
        		counter++;
        	}
        }
        
        for(int i = 0; i < 3; i++)
        {
        	for(int j = 0; j < 3; j++)
        	{
        		this.addSlotToContainer(new Slot(tileEntityAutoCrafting, counter, 116 + j*18, 89 + i*18));
        		counter++;
        	}
        }

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 152 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 210));
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntityAutoCrafting.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < 9)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 9, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}