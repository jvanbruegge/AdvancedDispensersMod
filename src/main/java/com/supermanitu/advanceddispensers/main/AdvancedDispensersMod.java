package com.supermanitu.advanceddispensers.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.BooleanUtils;

import net.minecraft.block.Block;
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
import com.supermanitu.advanceddispensers.placer.BlockPlacer;
import com.supermanitu.advanceddispensers.placer.PlacerConfig;
import com.supermanitu.advanceddispensers.placer.TileEntityPlacer;
import com.supermanitu.advanceddispensers.proxies.CommonProxy;

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
	public static final String VERSION = "1.2.0-b6";
	
	//Test
	
	@Instance("advanceddispensers")
    public static AdvancedDispensersMod instance;
	
	@SidedProxy(clientSide = "com.supermanitu.advanceddispensers.proxies.ClientProxy", serverSide= "com.supermanitu.advanceddispensers.proxies.CommonProxy")
    public static CommonProxy proxy;
	
	//Blocks
	public static BlockBreaker[] blockBreaker;
	public static BlockPlacer blockPlacer;
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
		 PlacerConfig placerConfig = new PlacerConfig(config);
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
						 blockBreaker[counter] = new BlockBreaker(i+2, j, false, breakerConfig.getTick());
						 GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
						 GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
						 counter++;
					 }
				 }
				 else
				 {
					 blockBreaker[counter] = new BlockBreaker(i+2, 0, false, breakerConfig.getTick());
					 GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
					 GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
					 counter++;
				 }
			 }
			 if(breakerConfig.areEnchantmentsEnabled())
			 {
				 for(int i = 0; i < 2; i++)
				 {
					 blockBreaker[counter] = new BlockBreaker(i+2, 0, true, breakerConfig.getTick());
					 GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
					 GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
					 counter++;
				 }
			 }
			 GameRegistry.registerTileEntity(TileEntityBreaker.class, "tileEntityBreaker");
		 }

		 //Placer
		 if(placerConfig.isEnabled())
		 {
			 blockPlacer = new BlockPlacer(placerConfig.getTick());
			 GameRegistry.registerBlock(blockPlacer, blockPlacer.getUnlocalizedName().substring(5));
			 GameRegistry.registerTileEntity(TileEntityPlacer.class, "tileEntityPlacer");
			 GameRegistry.addShapedRecipe(new ItemStack(blockPlacer, 1), blockPlacer.getRecipe());
		 }
		 
		 //Automated Crafting Table
		 if(autoCraftingConfig.isEnabled())
		 {
			 blockAutoCrafting = new BlockAutoCrafting(autoCraftingConfig.getTick());
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
			 System.err.println("Error: Fehler bei der Versionspr�fung:");
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
			 System.out.println("Error: Fehler bei der Verbindung zum Internet:");
			 return true;
		 }
		 BufferedReader in = new BufferedReader(is);

		 try 
		 {
			 if(in.readLine().contains(VERSION)) 
			 {
				 in.close();
				 is.close();
				 return true;
			 }
		 } 
		 catch (IOException e) 
		 {
			 System.out.println("Error: Fehler beim Schlie�en der Verbindung zum Internet:");
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
			 System.out.println("Error: Fehler beim Schlie�en der Verbindung zum Internet:");
			 e.printStackTrace();
			 return true;
		 }

		 return false;
	 }
}
