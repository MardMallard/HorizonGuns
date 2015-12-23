package com.gmail.Rhisereld.HorizonGuns;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HorizonCommandParser implements CommandExecutor
{
	JavaPlugin plugin;
	FileConfiguration config;
	
	public HorizonCommandParser(JavaPlugin plugin, FileConfiguration config)
	{
		this.plugin = plugin;
		this.config = config;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
	{	
		if (commandLabel.equalsIgnoreCase("checkammo"))
		{
			Player player;
			
			//Player-only command
			if (!(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "This command is only available to players.");
				return false;
			}
			else
			{
				player = (Player) sender;
			}
			
			//Check if the player is holding any of the gun types specified in configuration.
	    	Set<String> guns = config.getKeys(false);
	    	String gun = null;
	    	
	    	for (String g: guns)
	    		if (player.getItemInHand().getType().toString().equalsIgnoreCase(g))
	    			gun = g;
	    	
	    	if (gun == null)
	    	{
	    		player.sendMessage(ChatColor.RED + "You are not holding a gun!");
	    		return false;
	    	}
			
			double currentDurability = player.getItemInHand().getDurability();
			double maxDurability = player.getItemInHand().getType().getMaxDurability();
			int numShots = config.getInt(gun + ".shotsPerReload");
			int ammo = (int) ((maxDurability - currentDurability) / maxDurability * numShots);
			
			player.sendMessage(ChatColor.GOLD + "You have " + ammo + " charges left.");
			return true;
		}
		else if (commandLabel.equalsIgnoreCase("guns"))
			if (args.length > 0 && args[0].equalsIgnoreCase("reload"))
			{
				if (sender.hasPermission("horizonguns.admin.reload") || !(sender instanceof Player))
				{
					plugin.reloadConfig();
					config = plugin.getConfig();
					GunListener.updateConfig(config);
					sender.sendMessage(ChatColor.YELLOW + "Horizon Guns config reloaded.");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
					return false;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Command not recognised.");
				return false;
			}
		return false;
	}
}