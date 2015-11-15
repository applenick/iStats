package com.applenick.iStats;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.applenick.iStats.listeners.PvPListener;
import com.applenick.iStats.listeners.StatsListener;
import com.applenick.iStats.stats.StatManager;
import com.applenick.iStats.stats.StatPlayer;
/************************************************
		Created By AppleNick
Copyright © 2015 , AppleNick, All rights reserved.
		http://applenick.com
*************************************************/
public class IStats extends JavaPlugin {
		
	public String PREFIX = ChatColor.GRAY + "[" + ChatColor.GREEN + "i" + ChatColor.AQUA + "Stats" + ChatColor.GRAY + "] ";
	
	private static boolean DEV = false;
	
	public static boolean isDev(){
		return DEV;
	}
	
	private static IStats plugin;
	public static IStats get(){
		return plugin;
	}
	
	private static StatManager manager;
	public static StatManager getStats(){
		return manager;
	}
	
	
	private FileConfiguration playerConfig = null;
	private File playerConfigFile = null;
	
	public void reloadPlayerConfig(){
		if(playerConfigFile == null){
			playerConfigFile = new File(getDataFolder(), "players.yml");;
		}
		playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);
	}
	
	public FileConfiguration getPlayerConfig() {
	    if (playerConfig == null) {
	        reloadPlayerConfig();
	    }
	    return playerConfig;
	}
	
	public void savePlayerConfig() {
	    if (playerConfig == null || playerConfigFile == null) {
	        return;
	    }
	    try {
	        getPlayerConfig().save(playerConfigFile);
	    } catch (IOException ex) {
	    	console(ChatColor.RED + "Could not save" + ChatColor.YELLOW + " players.yml " + ChatColor.RED + " to disk.");
	    }
	}
	
	@Override
	public void onEnable(){
		console(ChatColor.GOLD + "iStats is now starting up...");
		plugin = this;
		this.saveDefaultConfig();
		this.reloadPlayerConfig();
		
		DEV = this.getConfig().getBoolean("debug");
		
		if(DEV){
			console(ChatColor.GREEN +"Debug Mode Activated" + ChatColor.GRAY + "-" + ChatColor.GOLD + "Debug messages will appear in the console. Adjust to false in config if not needed.");
		}
		
		manager = new StatManager();
		this.register();
		console(ChatColor.GREEN + "iStats "  + ChatColor.GRAY + "-" + ChatColor.DARK_RED + " Created by AppleNick " + ChatColor.GRAY + "-"  + ChatColor.GREEN +" Is Now Enabled.");
	}
	
	@Override
	public void onDisable(){
		this.savePlayerConfig();
		console(ChatColor.GREEN + "Player Stats have been saved to disk.");
		console(ChatColor.RED + "iStats is now disabled " + ChatColor.AQUA + "Goodbye...");
	}
	
	public void console(String msg){
		this.getServer().getConsoleSender().sendMessage(PREFIX + msg);
	}
	
	private void register(){
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new PvPListener(), this);
		pm.registerEvents(new StatsListener(), this);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("stats")) { 
			if(args.length == 0){
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					if(player != null){
						StatPlayer sp = getStats().getPlayer(player);
						player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============" + ChatColor.RESET +   ChatColor.GREEN +  "" + ChatColor.BOLD + "i" + ChatColor.AQUA +  "" + ChatColor.BOLD + "Stats" + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============");
						player.sendMessage(ChatColor.GREEN + player.getDisplayName() + "'s " + ChatColor.GRAY  + ""  + "Current Stats");
						player.sendMessage(" ");
						player.sendMessage(ChatColor.RED + "Kills: " + ChatColor.GOLD + sp.getKills());
						player.sendMessage(ChatColor.DARK_RED + "Deaths: " + ChatColor.GOLD + sp.getDeaths());
						player.sendMessage(ChatColor.LIGHT_PURPLE + "KDR: " + ChatColor.GOLD + sp.getKDR());
						player.sendMessage(" ");
						player.sendMessage(ChatColor.AQUA + "Current Killstreak: " + ChatColor.DARK_AQUA + sp.getCurrentKillstreak());
						player.sendMessage(ChatColor.BLUE + "Highest Killstreak: " + ChatColor.DARK_AQUA + sp.getHighestKillstreak());
						player.sendMessage(" ");
						player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============" + "========" + "=============");
						return true;
					}					
				}else{
					sender.sendMessage(ChatColor.RED + "Only players can use this command.");
					return true;
				}
				
				
			}
			
			
			if(args.length == 1){
				String name = args[0];
				if(name != null){
					Player player = this.getServer().getPlayer(name);
					if(player != null){
						StatPlayer sp = getStats().getPlayer(player);
						player.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============" + ChatColor.RESET +   ChatColor.GREEN +  "" + ChatColor.BOLD + "i" + ChatColor.AQUA +  "" + ChatColor.BOLD + "Stats" + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============");
						sender.sendMessage(ChatColor.GREEN + player.getDisplayName() + "'s " + ChatColor.GRAY  + ""  + "Current Stats");
						sender.sendMessage(" ");
						sender.sendMessage(ChatColor.RED + "Kills: " + ChatColor.GOLD + sp.getKills());
						sender.sendMessage(ChatColor.DARK_RED + "Deaths: " + ChatColor.GOLD + sp.getDeaths());
						sender.sendMessage(" ");
						sender.sendMessage(ChatColor.AQUA + "Current Killstreak: " + ChatColor.DARK_AQUA + sp.getCurrentKillstreak());
						sender.sendMessage(ChatColor.BLUE + "Highest Killstreak: " + ChatColor.DARK_AQUA + sp.getHighestKillstreak());
						sender.sendMessage(" ");
						sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============" + "========" + "=============");
						return true;
					}else{
						sender.sendMessage(ChatColor.GREEN + name + ChatColor.RED + " is currently not online.");
						return true;
					}
				}
			}			
			return true;
		} else if(cmd.getName().equalsIgnoreCase("reloadstats")){
			manager.reload();
			sender.sendMessage(ChatColor.GREEN + "Player Stats" + ChatColor.GOLD + " have been reloaded from the disk.");
			return true;
		}
		
		return false; 
	}

}
