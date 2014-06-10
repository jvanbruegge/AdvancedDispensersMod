package com.supermanitu.advanceddispensers.autocrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.world.World;

public class ContainerAutoCrafting extends Container
{
	private TileEntityAutoCrafting tileEntityAutoCrafting;
	private InventoryCrafting crafting = new InventoryCrafting(this, 3, 3);
	private InventoryCraftResult result = new InventoryCraftResult();
	private World worldObj;
	private static final String __OBFID = "CL_00001763";

	public ContainerAutoCrafting(IInventory inventory, TileEntityAutoCrafting tileEntityAutoCrafting, World world)
	{
		this.tileEntityAutoCrafting = tileEntityAutoCrafting;
		this.worldObj = world;

		int counter = 0;

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				System.out.print(tileEntityAutoCrafting.getStackInSlot(i*3 + j) + " ");
				this.addSlotToContainer(new Slot(crafting, i*3 + j, 30 + j * 18, 17 + i * 18));
				crafting.setInventorySlotContents(i*3 + j, tileEntityAutoCrafting.getStackInSlot(i*3 + j));
				counter++;
			}
			System.out.print("\n");
		}
		System.out.print("\n");

		this.addSlotToContainer(new SlotAutoCrafting(crafting, result, counter, 124, 35));
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

		this.onCraftMatrixChanged(this.crafting);
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntityAutoCrafting.isUseableByPlayer(par1EntityPlayer);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		super.onCraftMatrixChanged(par1IInventory);
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(crafting.getStackInSlot(i*3 + j) == null) tileEntityAutoCrafting.setInventorySlotContents(i*3 + j, null);
				else tileEntityAutoCrafting.setInventorySlotContents(i*3 + j, crafting.getStackInSlot(i*3 + j).copy());
				//System.out.print(tileEntityAutoCrafting.getStackInSlot(i*3 + j) +  " ");
			}
			//System.out.print("\n");
		}
		//System.out.print("\n");

		this.result.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.crafting, this.worldObj));

		if(result.getStackInSlot(0) == null) tileEntityAutoCrafting.setInventorySlotContents(9, null);
		else tileEntityAutoCrafting.setInventorySlotContents(9, result.getStackInSlot(0).copy());
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 34)
			{
				if (!this.mergeItemStack(itemstack1, 10, 70, true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 10, 25, false))
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

	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
	{
		return par2Slot.inventory != this.result && super.func_94530_a(par1ItemStack, par2Slot);
	}
}