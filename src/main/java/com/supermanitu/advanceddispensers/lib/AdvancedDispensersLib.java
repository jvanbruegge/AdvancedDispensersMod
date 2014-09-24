package com.supermanitu.advanceddispensers.lib;

/**
 * Includes often used functions
 */
public class AdvancedDispensersLib 
{
	public static final AdvancedDispensersLib INSTANCE = new AdvancedDispensersLib();
	
	private AdvancedDispensersLib() //no other instance but the static one
	{
		
	}
	
	public int getI(int meta, int x)
	{
		switch(meta)
		{
		case 4: return x-1;
		case 5: return x+1;
		
		case 12: return x-1;
		case 13: return x+1;
		default: return x;
		}
	}
	
	public int getJ(int meta, int y)
	{
		switch(meta)
		{
		case 0: return y-1;
		case 1: return y+1;
		
		case 8: return y-1;
		case 9: return y+1;
		default: return y;
		}
	}

	public int getK(int meta, int z) 
	{
		switch(meta)
		{
		case 2: return z-1;
		case 3: return z+1;
		
		case 10: return z-1;
		case 11: return z+1;
		default: return z;
		}
	}
	
	public void deleteEmptyStacks(TileEntityAdvancedDispensers tileEntity)
	{
		for(int i = 0; i < tileEntity.getSizeInventory(); i++)
		{
			if(tileEntity.getStackInSlot(i) != null && tileEntity.getStackInSlot(i).stackSize == 0)
			{
				tileEntity.setInventorySlotContents(i, null);
			}
		}
	}
}
