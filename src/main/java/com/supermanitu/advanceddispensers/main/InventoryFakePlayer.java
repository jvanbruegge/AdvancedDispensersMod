package com.supermanitu.advanceddispensers.main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class InventoryFakePlayer extends InventoryPlayer
{
	private TileEntityAdvancedDispensers tileEntity;
	
	public InventoryFakePlayer(EntityPlayer entityPlayer, TileEntityAdvancedDispensers tileEntity) 
	{
		super(entityPlayer);
		
		this.tileEntity = tileEntity;
		
		this.mainInventory = tileEntity.getInventory();
		this.armorInventory = null;
	}
	
	@Override
	public ItemStack getCurrentItem()
	{
		return this.currentItem < tileEntity.getSizeInventory() && this.currentItem >= 0 ? this.mainInventory[currentItem] : null;
	}
	
	@Override
	public void changeCurrentItem(int i)
	{
		if(i < tileEntity.getSizeInventory() && i >= 0) this.currentItem = i;
	}
	
	@Override
	public int getSizeInventory()
	{
		return tileEntity.getSizeInventory();
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return tileEntity.getStackInSlot(i);
	}
	
	@Override
	public String getInventoryName()
	{
		return tileEntity.getInventoryName();
	}
	
	@Override
	public boolean hasCustomInventoryName()
    {
        return tileEntity.hasCustomInventoryName();
    }
	
	@Override
	public int getInventoryStackLimit()
	{
		return tileEntity.getInventoryStackLimit();
	}
	
	@Override
	public ItemStack armorItemInSlot(int i)
    {
        return null;
    }
	
	@Override
	public int getTotalArmorValue()
    {
        return 0;
    }
	
	@Override
	public void damageArmor(float damage) {  }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return tileEntity.isUseableByPlayer(player);
	}
}
