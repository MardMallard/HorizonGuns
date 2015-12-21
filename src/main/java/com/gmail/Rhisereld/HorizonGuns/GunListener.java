package com.gmail.Rhisereld.HorizonGuns;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GunListener implements Listener
{	
	JavaPlugin plugin;
	FileConfiguration config;
	
	String ammoType;
	int cooldown;
	int reloadCooldown;
	int shotsPerReload;
	int damage;
	float projectileSpeed;
	
	public GunListener(JavaPlugin plugin)
	{
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}
	
	//Right-click -> fire
	//Left-click -> reload
	@EventHandler(priority = EventPriority.MONITOR)
	public void gun(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		
		//Right-click -> fire
	    if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) 
	    {   	
	    	//Check if the player is holding any of the gun types specified in configuration.
	    	Set<String> guns = config.getKeys(false);
	    	String gun = null;
	    	
	    	for (String g: guns)
	    		if (player.getItemInHand().getType().toString().equalsIgnoreCase(g))
	    			gun = g;
	    	
	    	if (gun == null)
	    		return;
	    	
	    	//Get configuration for gun
	    	ammoType = config.getString(gun + ".ammoType");
	    	projectileSpeed = (float) config.getDouble(gun + ".projectileSpeed");
	    	
	    	//Shoot the gun
	    	if (ammoType.equalsIgnoreCase("arrow"))
	    	{
		    	Arrow shootArrow = player.launchProjectile(Arrow.class);
		    	shootArrow.setVelocity(player.getEyeLocation().getDirection().multiply(projectileSpeed));
	    	}
	    	else
	    	{
	    		player.sendMessage(ChatColor.RED + "Incorrect configuration for gun: " + gun + "ammoType: " + ammoType 
	    				+ ". Please contact an administrator.");
	    		return;
	    	}
	    }
		    
	    //Left-click -> reload
	    if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))
	    {
	    	player.sendMessage("Reload.");
	    	//To do: reload
	    }
	}
}
