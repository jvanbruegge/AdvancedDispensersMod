package com.supermanitu.advanceddispensers.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class NewVersionEvent 
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Advanced Dispensers Mod"));
		event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "New version available: " + getActualVersion()));
	}
	
	private String getActualVersion()
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
			return "Error in loading new version number";
		}
		
		InputStreamReader is = null;
		try 
		{
			is = new InputStreamReader(url.openStream());
		}
		catch (IOException e) 
		{
			System.out.println("Error: Fehler bei der Verbindung zum Internet:");
			return "Error in loading new version number";
		}
        BufferedReader in = new BufferedReader(is);
        
        String h = null;
		try 
		{
			h = in.readLine();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return "Error in loading new version number";
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
			 return "Error in loading new version number";
		 }
        
		return h;
	}
}
