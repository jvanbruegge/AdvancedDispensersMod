package com.supermanitu.advanceddispensers.lib;

import java.util.Hashtable;
import java.util.Random;

import com.supermanitu.advanceddispensers.autocrafting.TileEntityAutoCrafting;
import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public abstract class BlockAdvancedDispensers extends BlockContainer
{
	private Random rand;
	private int maxBlockCount; //not static, could be different for multiple Blocks
	
	public BlockAdvancedDispensers(Material material, int maxBlockCount) 
	{
		super(material);
		this.maxBlockCount = maxBlockCount*2; //doubled method call
		rand = new Random();
		this.setCreativeTab(AdvancedDispensersMod.advancedDispensersTab);
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
		
		TileEntityAdvancedDispensers tileEntity = ((TileEntityAdvancedDispensers)world.getTileEntity(x, y, z));
		
		if(!tileEntity.onBlockPlaced(maxBlockCount, livingBase.getEntityId()))
		{
			if(livingBase instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) livingBase;
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + I18n.format("message.tooManyBlocks", new Object[0])));
				if(!player.capabilities.isCreativeMode) player.inventory.getStackInSlot(player.inventory.currentItem).stackSize++;
			}
			world.setBlock(x, y, z, Blocks.air);
		}
	}
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		super.onBlockHarvested(world, x, y, z, meta, player);
		
		TileEntityAdvancedDispensers tileEntity = ((TileEntityAdvancedDispensers)world.getTileEntity(x, y, z));
		
		tileEntity.onBlockDestroyed(player.getEntityId());
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
	public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
        TileEntityAdvancedDispensers tileEntityAdvancedDispensers = (TileEntityAdvancedDispensers)world.getTileEntity(x, y, z);

        if (tileEntityAdvancedDispensers != null)
        {
            for (int i1 = 0; i1 < tileEntityAdvancedDispensers.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileEntityAdvancedDispensers.getStackInSlot(i1);

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
	
	public void breakOriginal(World world, int x, int y, int z, Block block, int p_149749_6_)
	{
		super.breakBlock(world, x, y, z, block, p_149749_6_);
	}
	
	public abstract Object[] getRecipe();
	
	public Random getRandom()
	{
		return rand;
	}
	
	protected void setDefaultDirection(World world, int x, int y, int z, EntityLivingBase livingBase)
	{
		int l = BlockPistonBase.determineOrientation(world, x, y, z, livingBase);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
	}
}
