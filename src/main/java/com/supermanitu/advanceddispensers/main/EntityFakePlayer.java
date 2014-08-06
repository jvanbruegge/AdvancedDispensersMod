package com.supermanitu.advanceddispensers.main;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class EntityFakePlayer extends EntityPlayer
{
	public EntityFakePlayer(World world, TileEntityAdvancedDispensers tileEntity, int x, int y, int z, int meta)
	{
		super(world, new GameProfile(UUID.randomUUID(), "fakePlayer"));
		
		this.inventory = new InventoryFakePlayer(this, tileEntity);
		
		this.initPosition(x, y, z, meta);
	}

	@Override
	public void addChatMessage(IChatComponent chatComponent) 
	{
		
	}

	@Override
	public boolean canCommandSenderUseCommand(int id, String command)
	{
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() 
	{
		return null;
	}
	
	private void initPosition(int x, int y, int z, int meta)
	{
		double var3 = (double)x + 0.5D;
        double var5 = (double)y - 1.1D;
        double var7 = (double)z + 0.5D;
        float var1;
        float var2;
        
        System.out.println("Meta: " + (meta - 8));

        switch (meta - 8)
        {
            case 0:
                var1 = -90.0F;
                var2 = 0.0F;
                var5 += 0.51D;
                break;

            case 1:
                var1 = 90.0F; 
                var2 = 0.0F;
                var5 -= 0.51D;
                break;

            case 2:
                var1 = 0.0F;
                var2 = 0.0F;
                var7 += 0.51D;
                break;

            case 3:
                var1 = 0.0F;
                var2 = 180.0F;
                var7 -= 0.51D;
                break;

            case 4:
                var1 = 0.0F;
                var2 = 90.0F;
                var3 -= 0.51D;
                break;

            default:
                var1 = 0.0F;
                var2 = 270.0F; //90
                var3 += 0.51D; //-=
        }
        
        this.setLocationAndAngles(var3, var5, var7, var2, var1);
	}
}
