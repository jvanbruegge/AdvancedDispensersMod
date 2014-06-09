package com.supermanitu.advanceddispensers.placer;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPlacer extends BlockContainer
{
	public BlockPlacer() 
	{
		super(Material.wood);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new TileEntityPlacer();
	}

}
