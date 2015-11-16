package com.applenick.iStats.stats;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.applenick.iStats.IStats;
/************************************************
		Created By AppleNick
Copyright © 2015 , AppleNick, All rights reserved.
		http://applenick.com
*************************************************/
public class StatPlayer {
	
	private Player player;
	private int kills;
	private int deaths;
	private double kdr;
	private int current_killstreak;
	private int highest_killstreak;
	
	
	public StatPlayer(Player player , int kills, int deaths, int highest_killstreak){
		this.player = player;
		this.kills = kills;
		this.deaths = deaths;
		if(kills <= 0 || deaths <= 0){
			this.kdr = 0;
		}else{
			this.kdr = (kills / deaths);
		}
		this.highest_killstreak = highest_killstreak;
		this.current_killstreak = 0;
	}


	public Player getPlayer() {
		return player;
	}
	
	public double getKDR(){
		if(deaths >= 1){
			this.kdr = (kills / deaths);
		}
		return kdr;
	}


	public int getKills() {
		return kills;
	}


	public int getDeaths() {
		return deaths;
	}


	public int getCurrentKillstreak() {
		return current_killstreak;
	}

	public int getHighestKillstreak(){
		return highest_killstreak;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}


	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}


	public void setCurrentKillstreak(int current_killstreak) {
		this.current_killstreak = current_killstreak;
	}


	public void setHighestKillstreak(int highest_killstreak) {
		this.highest_killstreak = highest_killstreak;
	}
	
	public void increaseKillstreak(){
		this.current_killstreak = (current_killstreak + 1);
		
		if((current_killstreak % 5 == 0) && (current_killstreak >= 5)){
			IStats.get().getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " is on a killstreak of " + ChatColor.RED + current_killstreak);
		}
		
		if(current_killstreak > highest_killstreak){
			highest_killstreak = current_killstreak;
		}
		
	}
	
	public void resetKillstreak(Player ended){	
		if((current_killstreak % 5 == 0) && (current_killstreak >= 5)){
			player.sendMessage(ended.getDisplayName() + ChatColor.RED + " ended your killstreak of " + ChatColor.GREEN + current_killstreak);
		}
		this.current_killstreak = 0;
	}
	
	public void increaseKills(){
		this.kills = (kills + 1);
	}
	
	public void increaseDeaths(){
		this.deaths = (deaths + 1);
	}
	
	public void save(){
		ConfigurationSection config = IStats.get().getPlayerConfig().getConfigurationSection(player.getUniqueId().toString());
		config.set("kills", kills);
		config.set("deaths", deaths);
		config.set("killstreak", highest_killstreak);
		IStats.get().savePlayerConfig();
	}
}
