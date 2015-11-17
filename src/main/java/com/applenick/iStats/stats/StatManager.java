package com.applenick.iStats.stats;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.applenick.iStats.IStats;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;
/************************************************
			Created By AppleNick
Copyright © 2015 , AppleNick, All rights reserved.
			http://applenick.com
*************************************************/

public class StatManager {
	
	private HashMap<Player,StatPlayer> cache;
	
	private List<Player> viewing_gui;
	
	public StatManager(){
		this.cache = Maps.newHashMap();
		this.viewing_gui = Lists.newArrayList();
	}
	
	public void reload(){
		this.cache.clear();
		
		IStats.get().reloadPlayerConfig();
		
		for(Player p  : IStats.get().getServer().getOnlinePlayers()){
			addStats(p);
		}
		IStats.get().console(ChatColor.GREEN + "Player Stats have been reloaded from disk.");
		IStats.get().console(ChatColor.RED + "Make sure to restart the server next time, as it is more safe. ;)");
	}
	
		
	public StatPlayer getPlayer(Player player){
		for(Entry<Player, StatPlayer> sp : cache.entrySet()){
			if(sp.getKey() == player){
				return sp.getValue();
			}
		}
		return null;
	}
	
	public boolean hasStats(Player player){
		if(IStats.get().getPlayerConfig().isConfigurationSection(player.getUniqueId().toString())){
			return true;
		}else{
			return false;
		}
	
	}

	public void addStats(Player player){
		FileConfiguration config = IStats.get().getPlayerConfig();
		String path = player.getUniqueId().toString() + ".";		
		StatPlayer sp = new StatPlayer(player , config.getInt(path + "kills") , config.getInt(path + "deaths"), config.getInt(path + "killstreak") , config.getBoolean(path + "gui" , true));
		this.cache.put(player, sp);
	}
	
	public void removeStats(Player player){
		this.cache.remove(player);
	}
	
	public void addDefaultStats(Player player){
		IStats.get().getPlayerConfig().createSection(player.getUniqueId().toString());
		ConfigurationSection config = IStats.get().getPlayerConfig().getConfigurationSection(player.getUniqueId().toString());
		config.set("kills", 0);
		config.set("deaths", 0);
		config.set("killstreak", 0);
		config.set("gui", true);
		IStats.get().savePlayerConfig();
		addStats(player);
	}
	
	
	//GUI
	
	public boolean isViewingGUI(Player player){
		for(Player sp : viewing_gui){
			if(sp == player){
				return true;
			}
		}
		return false;
	}
	
	public void addViewer(Player player){
		this.viewing_gui.add(player);
	}
	
	public void removeViewer(Player player){
		this.viewing_gui.remove(player);
	}
	
}
