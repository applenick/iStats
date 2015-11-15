package com.applenick.iStats.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.applenick.iStats.IStats;
/************************************************
		Created By AppleNick
Copyright © 2015 , AppleNick, All rights reserved.
		http://applenick.com
 *************************************************/
public class StatsListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(IStats.getStats().hasStats(player)){
			IStats.getStats().addStats(player);
			if(IStats.isDev()){
				IStats.get().console(ChatColor.GOLD + player.getName() + " stats have been loaded onto the server.");
			}
		}else{
			if(IStats.isDev()){
				IStats.get().console(ChatColor.RED + player.getName() + " has no saved stats." + ChatColor.GREEN + "Creating them now...");
			}
			IStats.getStats().addDefaultStats(player);
		}
	}
	
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		IStats.getStats().removeStats(event.getPlayer());
	}
	
}
