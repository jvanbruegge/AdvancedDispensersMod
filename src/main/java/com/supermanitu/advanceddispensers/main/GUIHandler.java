package com.supermanitu.advanceddispensers.main;

import com.supermanitu.advanceddispensers.autocrafting.ContainerAutoCrafting;
import com.supermanitu.advanceddispensers.autocrafting.GuiAutoCrafting;
import com.supermanitu.advanceddispensers.autocrafting.TileEntityAutoCrafting;
import com.supermanitu.advanceddispensers.breaker.ContainerBreaker;
import com.supermanitu.advanceddispensers.breaker.GuiBreaker;
import com.supermanitu.advanceddispensers.breaker.TileEntityBreaker;
import com.supermanitu.advanceddispensers.user.ContainerUser;
import com.supermanitu.advanceddispensers.user.GuiUser;
import com.supermanitu.advanceddispensers.user.TileEntityUser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler 
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) 
	{
		TileEntity tileentity = world.getTileEntity(x, y, z);
		
		if(tileentity instanceof TileEntityBreaker)
		{
			return new ContainerBreaker(player.inventory, (TileEntityBreaker) tileentity);
		}
		else if(tileentity instanceof TileEntityUser)
		{
			return new ContainerUser(player.inventory, (TileEntityUser) tileentity);
		}
		else if(tileentity instanceof TileEntityAutoCrafting)
		{
			return new ContainerAutoCrafting(player.inventory, (TileEntityAutoCrafting) tileentity, world);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) 
	{
		TileEntity tileentity = world.getTileEntity(x, y, z);
		
		if(tileentity instanceof TileEntityBreaker)
		{
			return new GuiBreaker(player.inventory, (TileEntityBreaker) tileentity);
		}
		else if(tileentity instanceof TileEntityUser)
		{
			return new GuiUser(player.inventory, (TileEntityUser) tileentity);
		}
		else if(tileentity instanceof TileEntityAutoCrafting)
		{
			return new GuiAutoCrafting(player.inventory, (TileEntityAutoCrafting) tileentity, world);
		}
		return null;
	}
}
