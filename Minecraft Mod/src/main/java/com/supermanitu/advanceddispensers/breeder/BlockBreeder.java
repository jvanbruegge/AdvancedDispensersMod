package com.supermanitu.advanceddispensers.breeder;

import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBreeder extends BlockContainer
{
	public BlockBreeder()
	{
		super(Material.wood);
		this.setCreativeTab(AdvancedDispensersMod.advancedDispensersTab);
		this.setHardness(2f);
		this.setStepSound(soundTypeWood);
		this.setBlockName("blockBreeder");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		return new TileEntityBreeder();
	}
	
	public Object[] getRecipe()
	{
		return new Object[]{"I I", "III", "S S", 'I', Items.iron_ingot, 'S', Items.stick};
	}
}
