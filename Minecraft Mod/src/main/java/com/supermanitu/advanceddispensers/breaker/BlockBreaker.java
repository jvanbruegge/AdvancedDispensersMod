package com.supermanitu.advanceddispensers.breaker;

import java.util.ArrayList;
import java.util.Random;

import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;

import cpw.mods.fml.common.registry.LanguageRegistry;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BlockBreaker extends BlockContainer
{
	private int tier, tickRate, fortune;
	private boolean silkTouch;
	private BreakerTextureHelper textureHelper;
	
	public BlockBreaker(int tier, int fortune, boolean silkTouch, int tickRate) 
	{
		super(Material.rock);
		this.setCreativeTab(AdvancedDispensersMod.advancedDispensersTab);
		this.setHardness(2f);
		this.setStepSound(soundTypeStone);
		this.setBlockName("blockBreaker");
		
		this.fortune = fortune;
		this.silkTouch = silkTouch;
		this.tier = tier;
		this.tickRate = tickRate;
		this.textureHelper = new BreakerTextureHelper(tier);
	}

	public static int getTierCount()
	{
		return 10;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		textureHelper.registerBlockIcons(register);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return textureHelper.getIcon(side, meta);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType()
	{
		return AdvancedDispensersMod.renderID;
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
	public String getUnlocalizedName()
	{
		String s = super.getUnlocalizedName();
		s += "." + tier;
		
		if(silkTouch) s += ".s";
		else if(fortune > 0) s += "." + fortune;
		
		return s;
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
			this.breakBlockInFront(world, x, y, z);
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
		return new TileEntityBreaker();
	}
	
	public int getTier()
	{
		return tier;
	}
	
	public Object[] getRecipe()
	{
		return new Object[]{"XZX","ZOZ","BYB", 'Z', Items.redstone, 'Y', Blocks.piston, 'X', Items.iron_ingot, 'O', this.getItemByTier(), 'B', Blocks.stone};
	}
	
	private Object getItemByTier() 
	{
		switch(tier)
		{
		case 2: return getIromItem();
		case 3: return getDiamondItem();
		
		default: return Items.iron_pickaxe;
		}
	}
	
	private Object getIromItem() 
	{
		if(!silkTouch && fortune == 0) return Items.iron_pickaxe;
		else if(!silkTouch)
		{
			ItemStack stack = new ItemStack(Items.iron_pickaxe, 1);
			stack.addEnchantment(Enchantment.fortune, fortune);
			return stack;
		}
		else
		{
			ItemStack stack = new ItemStack(Items.iron_pickaxe, 1);
			stack.addEnchantment(Enchantment.silkTouch, 1);
			return stack;
		}
	}
	
	private Object getDiamondItem() 
	{
		if(!silkTouch && fortune == 0) return Items.diamond_pickaxe;
		else if(!silkTouch)
		{
			ItemStack stack = new ItemStack(Items.diamond_pickaxe, 1);
			stack.addEnchantment(Enchantment.fortune, fortune);
			return stack;
		}
		else
		{
			ItemStack stack = new ItemStack(Items.diamond_pickaxe, 1);
			stack.addEnchantment(Enchantment.silkTouch, 1);
			return stack;
		}
	}

	private void setDefaultDirection(World world, int x, int y, int z, EntityLivingBase livingBase)
	{
		int l = BlockPistonBase.determineOrientation(world, x, y, z, livingBase);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
	}
	
	private void breakBlockInFront(World world, int x, int y, int z) 
	{
		int meta = world.getBlockMetadata(x, y, z);
		int i = getI(x, y, z, meta);
		int j = getJ(x, y, z, meta);
		int k = getK(x, y, z, meta);
		
		Block block = world.getBlock(i, j, k);
		int blockmeta = world.getBlockMetadata(i, j, k);
		
		if(!block.equals(Blocks.air) && block.getHarvestLevel(blockmeta) <= this.tier)
		{
			ArrayList<ItemStack> drops = null;
			
			if(!silkTouch) drops = block.getDrops(world, i, j, k, blockmeta, fortune);
			else
			{
				drops = new ArrayList<ItemStack>();
				drops.add(new ItemStack(block, 1));
			}
			
			TileEntityBreaker tileEntityBreaker = (TileEntityBreaker) world.getTileEntity(x, y, z);
			
			for(ItemStack stack : drops)
			{
				if(getSlotsForItemStack(stack, tileEntityBreaker))
				{
					world.setBlock(i, j, k, Blocks.air);
				}
			}
		}
	}

	private boolean getSlotsForItemStack(ItemStack stack, TileEntityBreaker tileEntityBreaker)
	{
		int s = vorhanden(tileEntityBreaker, stack);
		
		if(s > -1)
		{
			if(tileEntityBreaker.getStackInSlot(s).stackSize + stack.stackSize <= tileEntityBreaker.getInventoryStackLimit())
			{
				tileEntityBreaker.getStackInSlot(s).stackSize += stack.stackSize;
				return true;
			}
			else
			{
				while(stack.stackSize > 0)
				{
					int oldSize = tileEntityBreaker.getStackInSlot(s).stackSize;
					tileEntityBreaker.getStackInSlot(s).stackSize = tileEntityBreaker.getInventoryStackLimit();
					stack.stackSize -= oldSize - tileEntityBreaker.getStackInSlot(s).stackSize;
					
					s = vorhanden(tileEntityBreaker, stack);
					if(s == -1) break;
				}
			}
		}
		else
		{
			for(int i = 0; i < tileEntityBreaker.getSizeInventory(); i++)
			{
				if(tileEntityBreaker.getStackInSlot(i) == null || tileEntityBreaker.getStackInSlot(i).stackSize == 0)
				{
					tileEntityBreaker.setInventorySlotContents(i, stack);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private int vorhanden(TileEntityBreaker tileEntityBreaker, ItemStack stack)
	{
		for(int i = 0; i < tileEntityBreaker.getSizeInventory(); i++)
		{
			if(tileEntityBreaker.getStackInSlot(i) != null && tileEntityBreaker.getStackInSlot(i).getItem().equals(stack.getItem()) && tileEntityBreaker.getStackInSlot(i).stackSize < tileEntityBreaker.getInventoryStackLimit())
			{
				return i;
			}
		}
		return -1;
	}

	private int getI(int x, int y, int z, int meta) 
	{
		switch(meta)
		{
		case 12: return x-1;
		case 13: return x+1;
		default: return x;
		}
	}

	private int getJ(int x, int y, int z, int meta)
	{
		switch(meta)
		{
		case 8: return y-1;
		case 9: return y+1;
		default: return y;
		}
	}

	private int getK(int x, int y, int z, int meta) 
	{
		switch(meta)
		{
		case 10: return z-1;
		case 11: return z+1;
		default: return z;
		}
	}
}
