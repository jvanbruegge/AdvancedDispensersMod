package com.supermanitu.advanceddispensers.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.minecraft.client.resources.I18n;
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
			System.err.println(I18n.format("error.versionCheck"));
			e.printStackTrace();
			return I18n.format("error.loadVersion");
		}
		
		InputStreamReader is = null;
		try 
		{
			is = new InputStreamReader(url.openStream());
		}
		catch (IOException e) 
		{
			System.out.println(I18n.format("error.versionCheck"));
			return I18n.format("error.loadVersion");
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
			return I18n.format("error.loadVersion");
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
			 return I18n.format("error.loadVersion");
		 }
        
		return h;
	}
}
