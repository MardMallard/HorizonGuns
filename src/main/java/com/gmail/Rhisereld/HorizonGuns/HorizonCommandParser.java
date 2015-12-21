package com.gmail.Rhisereld.HorizonGuns;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HorizonCommandParser implements CommandExecutor
{
	FileConfiguration config;
	
	public HorizonCommandParser(FileConfiguration config)
	{
		this.config = config;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
	{
		Player player = (Player) sender;
		
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

}