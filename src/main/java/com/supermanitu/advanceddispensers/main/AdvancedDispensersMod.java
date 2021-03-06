package com.supermanitu.advanceddispensers.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.BooleanUtils;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.supermanitu.advanceddispensers.autocrafting.AutoCraftingConfig;
import com.supermanitu.advanceddispensers.autocrafting.BlockAutoCrafting;
import com.supermanitu.advanceddispensers.autocrafting.TileEntityAutoCrafting;
import com.supermanitu.advanceddispensers.breaker.BlockBreaker;
import com.supermanitu.advanceddispensers.breaker.BreakerConfig;
import com.supermanitu.advanceddispensers.breaker.TileEntityBreaker;
import com.supermanitu.advanceddispensers.proxies.CommonProxy;
import com.supermanitu.advanceddispensers.user.BlockUser;
import com.supermanitu.advanceddispensers.user.UserConfig;
import com.supermanitu.advanceddispensers.user.TileEntityUser;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = AdvancedDispensersMod.MODID, version = AdvancedDispensersMod.VERSION)
public class AdvancedDispensersMod 
{
	public static final String MODID = "advanceddispensers";
	public static final String VERSION = "1.2.0-b8 for 1.7.2";
	
	@Instance("advanceddispensers")
    public static AdvancedDispensersMod instance;
	
	@SidedProxy(clientSide = "com.supermanitu.advanceddispensers.proxies.ClientProxy", serverSide= "com.supermanitu.advanceddispensers.proxies.CommonProxy")
    public static CommonProxy proxy;
	
	//Blocks
	public static BlockBreaker[] blockBreaker;
	public static BlockUser blockUser;
	public static BlockAutoCrafting blockAutoCrafting;
	
	//Items
	
	//Misc
	public static CreativeTabs advancedDispensersTab;
	public static int renderID;
	
	//Rest
	
	 @EventHandler
	 public void preInit(FMLPreInitializationEvent event)
	 {
		 Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		 config.load();
		 
		 BreakerConfig breakerConfig = new BreakerConfig(config);
		 UserConfig userConfig = new UserConfig(config);
		 AutoCraftingConfig autoCraftingConfig = new AutoCraftingConfig(config);
		 
		 //Initialization
		 advancedDispensersTab = new AdvancedDispensersTab("advanceddispenserstabs");
		 
		 if(!isActualVersion())
		 {
			 FMLCommonHandler.instance().bus().register(new NewVersionEvent());
		 }
		 
		 proxy.registerRenderers();
		 
		 NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());

		 //Breaker
		 if(breakerConfig.isEnabled())
		 {
			 blockBreaker = new BlockBreaker[BlockBreaker.getTierCount()];
			 int counter = 0;
			 for(int i = 0; i < 2; i++)
			 {
				 if(breakerConfig.areEnchantmentsEnabled())
				 {
					 for(int j = 0; j < 4; j++)
					 {
						 blockBreaker[counter] = new BlockBreaker(i+2, j, false, breakerConfig.getTick(), breakerConfig.getMaxBlockCount());
						 GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
						 GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
						 counter++;
					 }
				 }
				 else
				 {
					 blockBreaker[counter] = new BlockBreaker(i+2, 0, false, breakerConfig.getTick(), breakerConfig.getMaxBlockCount());
					 GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
					 GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
					 counter++;
				 }
			 }
			 if(breakerConfig.areEnchantmentsEnabled())
			 {
				 for(int i = 0; i < 2; i++)
				 {
					 blockBreaker[counter] = new BlockBreaker(i+2, 0, true, breakerConfig.getTick(), breakerConfig.getMaxBlockCount());
					 GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
					 GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
					 counter++;
				 }
			 }
			 GameRegistry.registerTileEntity(TileEntityBreaker.class, "tileEntityBreaker");
		 }

		 //User
		 if(userConfig.isEnabled())
		 {
			 blockUser = new BlockUser(userConfig.getTick(), userConfig.isFakePlayerEnabled(), userConfig.getMaxBlockCount());
			 GameRegistry.registerBlock(blockUser, blockUser.getUnlocalizedName().substring(5));
			 GameRegistry.registerTileEntity(TileEntityUser.class, "tileEntityUser");
			 GameRegistry.addShapedRecipe(new ItemStack(blockUser, 1), blockUser.getRecipe());
		 }
		 
		 //Automated Crafting Table
		 if(autoCraftingConfig.isEnabled())
		 {
			 blockAutoCrafting = new BlockAutoCrafting(autoCraftingConfig.getTick(), autoCraftingConfig.getMaxBlockCount());
			 GameRegistry.registerBlock(blockAutoCrafting, blockAutoCrafting.getUnlocalizedName().substring(5));
			 GameRegistry.registerTileEntity(TileEntityAutoCrafting.class, "tileEntityAutoCrafting");
			 GameRegistry.addShapedRecipe(new ItemStack(blockAutoCrafting, 1), blockAutoCrafting.getRecipe());
		 }
		 
		 config.save();
	 }

	@EventHandler
	 public void init(FMLInitializationEvent event)
	 {
		 
	 }
	 
	 @EventHandler
	 public void postInit(FMLPostInitializationEvent event)
	 {
		 
	 }
	 
	 private boolean isActualVersion()
	 {
		 URL url = null;
		 try 
		 {
			 url = new URL("http://www.wasdgames.de/Version/advanceddispensers.html");
		 } 
		 catch (MalformedURLException e) 
		 {
			 System.err.println(I18n.format("error.versionCheck"));
			 e.printStackTrace();
			 return true;
		 }

		 InputStreamReader is = null;
		 try 
		 {
			 is = new InputStreamReader(url.openStream());
		 }
		 catch (IOException e) 
		 {
			 System.out.println(I18n.format("error.versionCheck"));
			 return true;
		 }
		 BufferedReader in = new BufferedReader(is);

		 try 
		 {
			 if(VERSION.contains(in.readLine()))
			 {
				 in.close();
				 is.close();
				 return true;
			 }
		 } 
		 catch (IOException e) 
		 {
			 System.out.println(I18n.format("error.versionCheck"));
			 e.printStackTrace();
			 return true;
		 }

		 try 
		 {
			 in.close();
			 is.close();
		 }
		 catch (IOException e)
		 {
			 System.out.println(I18n.format("error.versionCheck"));
			 e.printStackTrace();
			 return true;
		 }

		 return false;
	 }
}
