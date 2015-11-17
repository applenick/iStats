package com.applenick.iStats.stats;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.applenick.iStats.IStats;
import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;
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
	private boolean gui;
	
	
	public StatPlayer(Player player , int kills, int deaths, int highest_killstreak , boolean gui){
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
		this.gui = gui;
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
	
	//GUI
	
	public boolean hasSetGUI(){
		return gui;
	}
	
	public void setGUI(boolean gui){
		this.gui = gui;
	}
	
	public void displayGUI(Player sender){
		Inventory inv = IStats.get().getServer().createInventory(null, 9, ChatColor.GREEN + player.getDisplayName() + "'s" + ChatColor.AQUA + " Stats");
	
		MenuItem miKills = new MenuItem(ChatColor.RED + "Kills" , ChatColor.GRAY + "Kills" + ChatColor.GOLD + " : " + ChatColor.RED.toString() + kills , 1 , Material.DIAMOND_SWORD , 1);
		MenuItem miDeaths = new MenuItem(ChatColor.DARK_PURPLE + "Deaths" , ChatColor.GRAY + "Deaths" + ChatColor.GOLD + " : " + ChatColor.DARK_PURPLE.toString() + deaths , 1 , Material.BONE , 3);
		MenuItem miKDR = new MenuItem(ChatColor.DARK_RED + "KDR" , ChatColor.GRAY + "KDR" + ChatColor.GOLD + " : " + ChatColor.DARK_RED.toString() + kdr , 1 , Material.TNT , 5);
		MenuItem miKS = new MenuItem(ChatColor.AQUA + "Killstreak" , ChatColor.GRAY + "Current Killstreak" + ChatColor.GOLD + " : " + ChatColor.AQUA.toString() + current_killstreak , 1 , Material.DIAMOND , 7);
		miKS.addLoreLine(ChatColor.GRAY + "Highest Killstreak" + ChatColor.GOLD + " : " + ChatColor.BLUE.toString() + highest_killstreak);
		
		inv.setItem(miKills.getSlot(), miKills.getItem());
		inv.setItem(miDeaths.getSlot(), miDeaths.getItem());
		inv.setItem(miKDR.getSlot(), miKDR.getItem());
		inv.setItem(miKS.getSlot(), miKS.getItem());
	
		
		sender.openInventory(inv);
		IStats.getStats().addViewer(sender);
		return;
	}
	
	public static class MenuItem{
		
		private String name;
		//private String subString;
		private List<String> loreS;
		private int amount; 
		private Material material;
		private int slot;
		
		public MenuItem(String name, String subString, int amount, Material material , int slot){
			this.name = name;
			this.loreS = Lists.newArrayList();
			this.loreS.add(subString);
			this.amount = amount;
			this.material = material;
			this.slot = slot;
		}
		
		public int getSlot(){
			return slot;
		}
		
		public void addLoreLine(String lore){
			this.loreS.add(lore);
		}
		
		public ItemStack getItem(){
			ItemStack item = new ItemStack(material);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(loreS);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
			item.setAmount(amount);
			return item;
		}
		
	}
	
	public void save(){
		ConfigurationSection config = IStats.get().getPlayerConfig().getConfigurationSection(player.getUniqueId().toString());
		config.set("kills", kills);
		config.set("deaths", deaths);
		config.set("killstreak", highest_killstreak);
		config.set("gui" , gui);
		IStats.get().savePlayerConfig();
	}
}
