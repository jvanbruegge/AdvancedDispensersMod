package com.supermanitu.advanceddispensers.main;

import com.supermanitu.advanceddispensers.breaker.ContainerBreaker;
import com.supermanitu.advanceddispensers.breaker.GuiBreaker;
import com.supermanitu.advanceddispensers.breaker.TileEntityBreaker;
import com.supermanitu.advanceddispensers.placer.ContainerPlacer;
import com.supermanitu.advanceddispensers.placer.GuiPlacer;
import com.supermanitu.advanceddispensers.placer.TileEntityPlacer;

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
		else if(tileentity instanceof TileEntityPlacer)
		{
			return new ContainerPlacer(player.inventory, (TileEntityPlacer) tileentity);
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
		else if(tileentity instanceof TileEntityPlacer)
		{
			return new GuiPlacer(player.inventory, (TileEntityPlacer) tileentity);
		}
		return null;
	}
}
