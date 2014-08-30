package com.supermanitu.advanceddispensers.autocrafting;

import java.util.ArrayList;
import java.util.Random;

import com.supermanitu.advanceddispensers.breaker.TileEntityBreaker;
import com.supermanitu.advanceddispensers.lib.BlockAdvancedDispensers;
import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockAutoCrafting extends BlockAdvancedDispensers
{
	private int tickRate;
	
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide;
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	
	public BlockAutoCrafting(int tickRate, int maxBlockCount) 
	{
		super(Material.iron, maxBlockCount);
		this.setHardness(2f);
		this.setBlockName("autoCrafting");
		this.setStepSound(soundTypeMetal);
		
		this.tickRate = tickRate;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		textureBottom = register.registerIcon(AdvancedDispensersMod.MODID+":"+"Autocrafting_bottom");
		textureSide = register.registerIcon(AdvancedDispensersMod.MODID+":"+"Autocrafting_side");
		textureTop = register.registerIcon(AdvancedDispensersMod.MODID+":"+"Autocrafting_top");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta)
	{
		switch(side)
		{
		case 0: return textureBottom;
		case 1: return textureTop;
		
		default: return textureSide;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, x, y, z, livingBase, itemStack);
		setDefaultDirection(world, x, y, z, livingBase);
	}
	
	@Override
	public int tickRate(World world)
	{
		return tickRate;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if(!world.isRemote)
		{
			TileEntityAutoCrafting tileEntity = (TileEntityAutoCrafting) world.getTileEntity(x, y, z);
			tileEntity.craft();
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) 
	{
		return new TileEntityAutoCrafting();
	}
	
	public Object[] getRecipe()
	{
		return new Object[]{"XZX","ZOZ","XYX", 'Z', Items.redstone, 'Y', Blocks.piston, 'X', Items.iron_ingot, 'O', Blocks.crafting_table};
	}
}
