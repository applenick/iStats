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

import com.applenick.iStats.listeners.GUIListener;
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
		pm.registerEvents(new GUIListener(), this);
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("stats")) { 
			if(args.length == 0){
				if(sender instanceof Player){
					Player player = (Player) sender;
					if(player != null){
						StatPlayer sp = getStats().getPlayer(player);
						if(sp.hasSetGUI()){
							sp.displayGUI(player);
							return true;
						}else{
							this.sendStats(sender, sp, player);
							return true;
						}						
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
						StatPlayer targetSP = getStats().getPlayer(player);

						if(sender instanceof Player){
							StatPlayer senderSP = getStats().getPlayer((Player)sender);

							if(senderSP.hasSetGUI()){
								targetSP.displayGUI((Player)sender);
								return true;
							}
						}

						sendStats(sender , targetSP, player);
						return true;
					}else{
						sender.sendMessage(ChatColor.GREEN + name + ChatColor.RED + " is currently not online.");
						return true;
					}
				}else{
					return false;
				}
			}	

			if(args.length >= 2){
				sender.sendMessage(ChatColor.RED + "Please provide a proper username.");
				return false;
			}

			return true;
		} else if(cmd.getName().equalsIgnoreCase("reloadstats")){
			manager.reload();
			sender.sendMessage(ChatColor.GREEN + "Player Stats" + ChatColor.GOLD + " have been reloaded from the disk.");
			return true;
		}

		else if(cmd.getName().equalsIgnoreCase("gui")){

			//TOGGLE

			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Only Players can toggle their GUI");
				return true;
			}

			Player player = (Player) sender;
			StatPlayer statPlayer = getStats().getPlayer(player);
			statPlayer.setGUI(!statPlayer.hasSetGUI());
			statPlayer.save();

			sender.sendMessage(ChatColor.AQUA + "Stat GUI " + ChatColor.GOLD + "\u00BB " + (statPlayer.hasSetGUI() ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
			return true;

		}

		return false; 
	}


	private void sendStats(CommandSender sender , StatPlayer targetSP , Player targetP){
		sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============" + ChatColor.RESET +   ChatColor.GREEN +  "" + ChatColor.BOLD + "i" + ChatColor.AQUA +  "" + ChatColor.BOLD + "Stats" + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============");
		sender.sendMessage(ChatColor.GREEN + targetP.getDisplayName() + "'s " + ChatColor.GRAY  + ""  + "Current Stats");
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.RED + "Kills: " + ChatColor.GOLD + targetSP.getKills());
		sender.sendMessage(ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + targetSP.getDeaths());
		sender.sendMessage(ChatColor.DARK_RED + "KDR: " + ChatColor.GOLD + targetSP.getKDR());
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.AQUA + "Current Killstreak: " + ChatColor.DARK_AQUA + targetSP.getCurrentKillstreak());
		sender.sendMessage(ChatColor.BLUE + "Highest Killstreak: " + ChatColor.DARK_AQUA + targetSP.getHighestKillstreak());
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "=============" + "========" + "=============");
	}

}
