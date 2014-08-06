package com.supermanitu.advanceddispensers.user;

import java.util.Random;

import com.supermanitu.advanceddispensers.autocrafting.TileEntityAutoCrafting;
import com.supermanitu.advanceddispensers.main.AdvancedDispensersMod;
import com.supermanitu.advanceddispensers.main.EntityFakePlayer;
import com.supermanitu.advanceddispensers.main.TileEntityAdvancedDispensers;

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

public class BlockUser extends BlockContainer
{
	private int tickRate;
	private EntityFakePlayer fakePlayer;
	private UserTextureHelper textureHelper;
	private Random rand = new Random();
	private boolean enableFakePlayer;
	
	public BlockUser(int tickRate, boolean enableFakePlayer) 
	{
		super(Material.wood);
		this.tickRate = tickRate;
		this.setCreativeTab(AdvancedDispensersMod.advancedDispensersTab);
		this.setHardness(2f);
		this.setBlockName("blockUser");
		this.setStepSound(soundTypeWood);
		
		this.textureHelper = new UserTextureHelper();
		this.enableFakePlayer = enableFakePlayer;
		this.fakePlayer = null;
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
	
	public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
        TileEntityUser tileEntityUser = (TileEntityUser)world.getTileEntity(x, y, z);

        if (tileEntityUser != null)
        {
            for (int i1 = 0; i1 < tileEntityUser.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileEntityUser.getStackInSlot(i1);

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
			TileEntityUser tileEntity = (TileEntityUser) world.getTileEntity(x, y, z);
			int slot = getFirstSlot(tileEntity);
			if(slot < 0) return;
			Item item = tileEntity.getStackInSlot(slot).getItem();
			int meta = world.getBlockMetadata(x, y, z);
			
			int i = getI(meta, x);
			int j = getJ(meta, y);
			int k = getK(meta, z);
			
			if(!Block.getBlockFromItem(item).equals(Blocks.air))
			{
				System.out.println("Block: " + Block.getBlockFromItem(item).getUnlocalizedName());
				this.placeBlockInFront(world, tileEntity, x, y, z, i, j, k, meta, slot); //Old Placing functions
			}
			else if(enableFakePlayer)
			{
				if(fakePlayer == null) fakePlayer = new EntityFakePlayer(world, (TileEntityAdvancedDispensers) world.getTileEntity(x, y, z), x, y, z, meta);
				
				if(item.onItemUse(tileEntity.getStackInSlot(slot), fakePlayer, world, i, j, k, 0, 0.5f, 1.0f, 0.5f))
				{
					System.out.println("itemUse");
					return;
				}
				else
				{
					tileEntity.setInventorySlotContents(slot, item.onItemRightClick(tileEntity.getStackInSlot(slot), world, fakePlayer));
					if(tileEntity.getStackInSlot(slot).stackSize == 0) tileEntity.setInventorySlotContents(slot, null);
					System.out.println("itemRightClick");
				}
			}
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
		return new TileEntityUser();
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
	
	private int getI(int meta, int x)
	{
		switch(meta)
		{
		case 13: return x+1;
		case 12: return x-1;
		
		default: return x;
		}
	}
	
	private int getJ(int meta, int y)
	{
		switch(meta)
		{
		case 9: return 1+y;
		case 8: return y-1;
		
		default: return y;
		}
	}
	
	private int getK(int meta, int z)
	{
		switch(meta)
		{
		case 11: return z+1;
		case 10: return z-1;
		
		default: return z;
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

	private void setDefaultDirection(World world, int x, int y, int z, EntityLivingBase livingBase)
	{
		int l = BlockPistonBase.determineOrientation(world, x, y, z, livingBase);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
	}
}
