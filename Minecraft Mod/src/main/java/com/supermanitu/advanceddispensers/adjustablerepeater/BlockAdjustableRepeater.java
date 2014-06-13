package com.supermanitu.advanceddispensers.adjustablerepeater;

import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAdjustableRepeater extends BlockRedstoneDiode
{
	public BlockAdjustableRepeater(boolean powered)
	{
		super(powered);
		this.setCreativeTab(AdvancedDispensersMod.advancedDispensersTab);
		this.setHardness(2f);
		this.setBlockName("blockAdjustableRepeater");
	}
	
	public Object[] getRecipe()
	{
		return new Object[]{Items.clock, Items.repeater};
	}

	@Override
	protected int func_149901_b(int var1)
	{
		return 0;
	}

	@Override
	protected BlockRedstoneDiode getBlockPowered()
	{
		return AdvancedDispensersMod.adjustableRepeater[1];
	}

	@Override
	protected BlockRedstoneDiode getBlockUnpowered()
	{
		return AdvancedDispensersMod.adjustableRepeater[0];
	}
}
