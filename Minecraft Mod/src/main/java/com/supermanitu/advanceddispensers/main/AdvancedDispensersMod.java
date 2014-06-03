package com.supermanitu.advanceddispensers.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.common.config.Property;
import com.supermanitu.advanceddispensers.breaker.BlockBreaker;
import com.supermanitu.advanceddispensers.breaker.TileEntityBreaker;
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
	public static final String VERSION = "1.2.0";
	
	@Instance("advanceddispensers")
    public static AdvancedDispensersMod instance;
	
	@SidedProxy(clientSide = "com.supermanitu.advanceddispensers.proxies.ClientProxy", serverSide= "com.supermanitu.advanceddispensers.proxies.CommonProxy")
    public static CommonProxy proxy;
	
	//Blocks
	public static BlockBreaker[] blockBreaker;
	
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
		 int breakerTick = breakerConfig(config);
		 
		 //Initialization
		 advancedDispensersTab = new AdvancedDispensersTab("advanceddispenserstabs");
		 
		 if(!isActualVersion())
		 {
			 FMLCommonHandler.instance().bus().register(new NewVersionEvent());
		 }
		 
		 proxy.registerRenderers();
		 
		 NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
		 
		 //Breaker
		 blockBreaker = new BlockBreaker[BlockBreaker.getTierCount()];
		 int counter = 0;
		 for(int i = 0; i < 2; i++)
		 {
			 for(int j = 0; j < 4; j++)
			 {
				 blockBreaker[counter] = new BlockBreaker(i+2, j, false, breakerTick);
				 GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
				 GameRegistry.registerTileEntity(TileEntityBreaker.class, "tileEntityBreaker" + counter);
				 GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
				 counter++;
			 }
		 }
		 for(int i = 0; i < 2; i++)
		 {
			blockBreaker[counter] = new BlockBreaker(i+2, 0, true, breakerTick);
			GameRegistry.registerBlock(blockBreaker[counter], blockBreaker[counter].getUnlocalizedName().substring(5));
			GameRegistry.registerTileEntity(TileEntityBreaker.class, "tileEntityBreaker" + counter);
			GameRegistry.addShapedRecipe(new ItemStack(blockBreaker[counter], 1), blockBreaker[counter].getRecipe());
			counter++;
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
	 
	 private int breakerConfig(Configuration config)
	 {
		 Property p = config.get(Configuration.CATEGORY_GENERAL, "breakerTick", 4);
		 p.comment = "This value is the minimum delay between two activations (20 equals 1 time per second)";
		 return p.getInt();
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
			 System.err.println("Error: Fehler bei der Versionsprüfung:");
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
			 if(in.readLine().equals(VERSION)) 
			 {
				 in.close();
				 is.close();
				 return true;
			 }
		 } 
		 catch (IOException e) 
		 {
			 System.out.println("Error: Fehler beim Schließen der Verbindung zum Internet:");
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
			 System.out.println("Error: Fehler beim Schließen der Verbindung zum Internet:");
			 e.printStackTrace();
			 return true;
		 }

		 return false;
	 }
}
