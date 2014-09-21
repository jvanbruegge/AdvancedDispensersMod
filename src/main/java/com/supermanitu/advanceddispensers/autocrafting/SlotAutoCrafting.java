package com.supermanitu.advanceddispensers.autocrafting;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotAutoCrafting extends Slot
{
	/** The craft matrix inventory linked to this result slot. */
	private final IInventory craftMatrix;
	/**
	 * The number of items that have been crafted so far. Gets passed to ItemStack.onCrafting before being reset.
	 */
	private int amountCrafted;
	private static final String __OBFID = "CL_00001761";

	public SlotAutoCrafting(IInventory craftMatrix, IInventory craftResult, int id, int x, int y)
	{
		super(craftResult, id, x, y);
		this.craftMatrix = craftMatrix;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
	 * stack.
	 */
	public ItemStack decrStackSize(int par1)
	{
		if (this.getHasStack())
		{
			this.amountCrafted += Math.min(par1, this.getStack().stackSize);
		}

		return super.decrStackSize(par1);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
	 * internal count then calls onCrafting(item).
	 */
	protected void onCrafting(ItemStack par1ItemStack, int par2)
	{
		this.amountCrafted += par2;
		this.onCrafting(par1ItemStack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	protected void onCrafting(ItemStack par1ItemStack)
	{
		this.amountCrafted = 0;
	}

	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
	{
		FMLCommonHandler.instance().firePlayerCraftingEvent(par1EntityPlayer, par2ItemStack, craftMatrix);
		this.onCrafting(par2ItemStack);

		for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i)
		{
			ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);

			if (itemstack1 != null)
			{
				this.craftMatrix.decrStackSize(i, 1);

				if (itemstack1.getItem().hasContainerItem(itemstack1))
				{
					ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

					if (itemstack2 != null && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage())
					{
						continue;
					}

					if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1))
					{
						if (this.craftMatrix.getStackInSlot(i) == null)
						{
							this.craftMatrix.setInventorySlotContents(i, itemstack2);
						}
						/*else
						{
							this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
						}*/
					}
				}
			}
		}
	}
}