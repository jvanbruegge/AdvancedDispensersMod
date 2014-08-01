package com.supermanitu.advanceddispensers.breaker;

import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BreakerTextureHelper 
{
	private int tier;
	
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide_left;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide_right;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide_up;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide_down;
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	
	public BreakerTextureHelper(int tier)
	{
		this.tier = tier;
	}
	
	public void registerBlockIcons(IIconRegister register)
	{
		textureBottom = register.registerIcon(AdvancedDispensersMod.MODID+":"+"breaker/Breaker_" + tier + "_bottom");
		textureSide_up = register.registerIcon(AdvancedDispensersMod.MODID+":"+"breaker/Breaker_" + tier + "_side_up");
		textureSide_right = register.registerIcon(AdvancedDispensersMod.MODID+":"+"breaker/Breaker_" + tier + "_side_right");
		textureSide_left = register.registerIcon(AdvancedDispensersMod.MODID+":"+"breaker/Breaker_" + tier + "_side_left");
		textureSide_down = register.registerIcon(AdvancedDispensersMod.MODID+":"+"breaker/Breaker_" + tier + "_side_down");
		textureTop = register.registerIcon(AdvancedDispensersMod.MODID+":"+"breaker/Breaker_" + tier + "_top");
	}
	
	public IIcon getIcon(int side, int meta)
	{
		switch(meta)
		{
		case 0: return down(side);
		case 1: return up(side);
		case 2: return left(side);
		case 3: return right(side);
		case 4: return front(side);
		case 5: return behind(side);
		
		case 8: return down(side);
		case 9: return up(side);
		case 10: return left(side);
		case 11: return right(side);
		case 12: return front(side);
		case 13: return behind(side);
		
		default: return textureSide_up;
		}
	}
	
	private IIcon behind(int side)
	{
		switch(side)
		{
		case 4: return textureBottom;
		case 5: return textureTop;
		
		case 2: return textureSide_left;
		
		default: return textureSide_right;
		}
	}
	
	private IIcon front(int side)
	{
		switch(side)
		{
		case 5: return textureBottom;
		case 4: return textureTop;
		
		case 2: return textureSide_right;
		
		default: return textureSide_left;
		}
	}
	
	private IIcon right(int side)
	{
		switch(side)
		{
		case 2: return textureBottom;
		case 3: return textureTop;
		case 0: return textureSide_down;
		case 1: return textureSide_down;
		
		case 5: return textureSide_left;
		
		default: return textureSide_right;
		}
	}
	
	private IIcon left(int side)
	{
		switch(side)
		{
		case 3: return textureBottom;
		case 2: return textureTop;
		case 0: return textureSide_up;
		case 1: return textureSide_up;
		
		case 5: return textureSide_right;
		
		default: return textureSide_left;
		}
	}
	
	private IIcon down(int side)
	{
		switch(side)
		{
		case 1: return textureBottom;
		case 0: return textureTop;
		
		default: return textureSide_down;
		}
	}
	
	private IIcon up(int side)
	{
		switch(side)
		{
		case 0: return textureBottom;
		case 1: return textureTop;
		
		default: return textureSide_up;
		}
	}
}
