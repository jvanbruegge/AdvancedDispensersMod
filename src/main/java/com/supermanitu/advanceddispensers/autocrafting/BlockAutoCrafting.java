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

public class BlockAutoCrafting extends BlockContainer
{
	private int tickRate;
	
	@SideOnly(Side.CLIENT)
	private IIcon textureBottom;
	@SideOnly(Side.CLIENT)
	private IIcon textureSide;
	@SideOnly(Side.CLIENT)
	private IIcon textureTop;
	
	private Random rand = new Random();
	
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
			TileEntityAutoCrafting tileEntity = (TileEntityAutoCrafting) world.getTileEntity(x, y, z);
			tileEntity.craft();
		}
	}
	
	public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
        TileEntityAutoCrafting tileEntityAutoCrafting = (TileEntityAutoCrafting)world.getTileEntity(x, y, z);

        if (tileEntityAutoCrafting != null)
        {
            for (int i1 = 0; i1 < tileEntityAutoCrafting.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileEntityAutoCrafting.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
                    {
                        int j1 = this.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, p_149749_6_);
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
