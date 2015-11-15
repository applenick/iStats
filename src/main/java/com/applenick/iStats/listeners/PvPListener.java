package com.applenick.iStats.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.applenick.iStats.IStats;
import com.applenick.iStats.stats.StatPlayer;
/************************************************
		Created By AppleNick
Copyright © 2015 , AppleNick, All rights reserved.
		http://applenick.com
*************************************************/
public class PvPListener implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event){
		Player dead = event.getEntity();
		Player killer = dead.getKiller();
		
		if(dead != null && killer != null){
			StatPlayer deadSP = IStats.getStats().getPlayer(dead);
			StatPlayer killerSP = IStats.getStats().getPlayer(killer);
			
			
			if(deadSP != null ){
				deadSP.increaseDeaths();
				deadSP.resetKillstreak(killer);
				deadSP.save();
			}else{
				if(IStats.isDev()){
					IStats.get().console(dead.getName() + " could not be found...");
				}
			}
			
			if(killerSP != null){				
				killerSP.increaseKills();
				killerSP.increaseKillstreak();
				killerSP.save();
			}else{
				if(IStats.isDev()){
					IStats.get().console(killer.getName() + " could not be found...");
				}
			}
			
			return;	
		}
	}

}
