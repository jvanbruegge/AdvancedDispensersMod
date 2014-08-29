package com.supermanitu.advanceddispensers.user;

import java.util.Random;

import com.supermanitu.advanceddispensers.autocrafting.TileEntityAutoCrafting;
import com.supermanitu.advanceddispensers.lib.AdvancedDispensersLib;
import com.supermanitu.advanceddispensers.lib.BlockAdvancedDispensers;
import com.supermanitu.advanceddispensers.lib.TileEntityAdvancedDispensers;
import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;
import com.supermanitu.advanceddispensers.main.EntityFakePlayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUser extends BlockAdvancedDispensers
{
	private int tickRate;
	
	private UserTextureHelper textureHelper;
	private boolean enableFakePlayer;
	
	public BlockUser(int tickRate, boolean enableFakePlayer) 
	{
		super(Material.wood);
		this.tickRate = tickRate;
		this.setHardness(2f);
		this.setBlockName("blockUser");
		this.setStepSound(soundTypeWood);
		
		this.textureHelper = new UserTextureHelper();
		this.enableFakePlayer = enableFakePlayer;
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
			TileEntityUser tileEntity = (TileEntityUser) world.getTileEntity(x, y, z);
			int slot = getFirstSlot(tileEntity);
			if(slot < 0) return;
			Item item = tileEntity.getStackInSlot(slot).getItem();
			int meta = world.getBlockMetadata(x, y, z);
			
			if(enableFakePlayer)
			{
				tileEntity.useItem(world, x, y, z, meta, slot);
			}
			else if(!Block.getBlockFromItem(item).equals(Blocks.air))
			{
				int i = AdvancedDispensersLib.INSTANCE.getI(meta, x);
				int j = AdvancedDispensersLib.INSTANCE.getJ(meta, y);
				int k = AdvancedDispensersLib.INSTANCE.getK(meta, z);
				this.placeBlockInFront(world, tileEntity, x, y, z, i, j, k, meta, slot); //Old Placing function
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2)
	{
		return new TileEntityUser(world);
	}
	
	public Object[] getRecipe()
	{
		return new Object[]{"XCX", "CSC", "FCF", 'X', Blocks.planks, 'C', Items.redstone, 'S', Blocks.dispenser, 'F', Blocks.stone};
	}
	
	private void placeBlockInFront(World world, TileEntityUser tileEntity, int x, int y, int z, int i, int j, int k, int meta, int slot)
	{		
		if(slot == -1) return;
		
		Block block = null;
		
		ItemStack itemStack = tileEntity.getStackInSlot(slot);
		int itemMetadata = itemStack.getItemDamage();
		
		if(itemStack.getItem() instanceof IPlantable)
		{
			block = ((IPlantable) itemStack.getItem()).getPlant(world, i, j, k);
			
			if(!world.getBlock(i, j-1, k).canSustainPlant(world, i, j, k, ForgeDirection.UP, (IPlantable) block)) return;
		}
		else
		{
			block = Block.getBlockFromItem(itemStack.getItem());
		}
		
		if(world.getBlock(i, j, k).equals(Blocks.air) && block != null)
		{
			world.setBlock(i, j, k, block);
			world.setBlockMetadataWithNotify(i, j, k, itemMetadata, 2);
			
			tileEntity.getStackInSlot(slot).stackSize--;
			if(tileEntity.getStackInSlot(slot).stackSize == 0) tileEntity.setInventorySlotContents(slot, null);
		}
	}
	
	private int getFirstSlot(TileEntityUser tileEntityUser) 
	{
		int slot = -1;
		for(int i = 0; i < 9; i++)
		{
			if(tileEntityUser.getStackInSlot(i) != null && tileEntityUser.getStackInSlot(i).stackSize > 0)
			{
				return i;
			}
		}
		return slot;
	}
}
