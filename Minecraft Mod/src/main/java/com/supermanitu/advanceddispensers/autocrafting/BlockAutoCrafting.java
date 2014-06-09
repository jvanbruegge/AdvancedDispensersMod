package com.supermanitu.advanceddispensers.autocrafting;

import java.util.ArrayList;
import java.util.Random;

import com.supermanitu.advanceddispensers.breaker.TileEntityBreaker;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockAutoCrafting extends BlockContainer
{
	private int tickRate;
	
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide;
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	
	public BlockAutoCrafting(int tickRate) 
	{
		super(Material.iron);
		this.setHardness(2f);
		this.setBlockName("autoCrafting");
		this.setCreativeTab(AdvancedDispensersMod.advancedDispensersTab);
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
        	TileEntity tileentity = world.getTileEntity(x, y, z);

            if (tileentity != null && !player.isSneaking())
            {
            	player.openGui(AdvancedDispensersMod.instance, 0, world, x, y, z);
            }
            else
            {
            	return false;
            }

            return true;
        }
    }
	
	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int meta)
	{
		return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
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
			//TODO crafting
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
        int l = world.getBlockMetadata(x, y, z);
        boolean flag1 = (l & 8) != 0;

        if (flag && !flag1)
        {
        	world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
        	world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
        }
        else if (!flag && flag1)
        {
        	world.setBlockMetadataWithNotify(x, y, z, l & -9, 4);
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

	private void setDefaultDirection(World world, int x, int y, int z, EntityLivingBase livingBase)
	{
		int l = BlockPistonBase.determineOrientation(world, x, y, z, livingBase);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
	}
}
