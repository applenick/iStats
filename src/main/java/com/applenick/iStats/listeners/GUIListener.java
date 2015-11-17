package com.applenick.iStats.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.applenick.iStats.IStats;

/************************************************
			 Created By AppleNick
Copyright © 2015 , AppleNick, All rights reserved.
			http://applenick.com
 *************************************************/
public class GUIListener implements Listener{
	
	
	//Removal of GUI Protection
	@EventHandler
	public void onInvClose(InventoryCloseEvent event){
		if(!(event.getPlayer() instanceof Player)){
			return;
		}
		
		Player player = (Player) event.getPlayer();
		IStats.getStats().removeViewer(player);
		return;
	}
	
	//GUI Protection
	@EventHandler
	public void onInvClick(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player)){
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		
		if(IStats.getStats().isViewingGUI(player)){
			event.setCancelled(true);
			return;
		}
		
	}

}
