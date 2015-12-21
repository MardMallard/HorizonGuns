package com.gmail.Rhisereld.HorizonGuns;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
	
	HashMap<String,Long> shootCooldown = new HashMap<String,Long>();
	
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
	    	
	    	//Check if the player is on cooldown.
	    	cooldown = config.getInt(gun + ".cooldown");
	    	
	    	if (shootCooldown.containsKey(player.getName()) && System.currentTimeMillis() - shootCooldown.get(player.getName()) <= cooldown)
	    		return;
	    	
	    	shootCooldown.remove(player.getName());
	    	
	    	//If player's ammo is 0, tell them to reload.
	    	float maxDurability = player.getItemInHand().getType().getMaxDurability();
	    	float currentDurability = player.getItemInHand().getDurability();
	    	shotsPerReload = config.getInt(gun + ".shotsPerReload");
	    	if (currentDurability > maxDurability - 8)
	    	{
	    		player.sendMessage(ChatColor.RED + "*click*");
	    		shootCooldown.put(player.getName(), System.currentTimeMillis());
	    		return;
	    	}
	    	
	    	//Get configuration for gun
	    	ammoType = config.getString(gun + ".ammoType");
	    	projectileSpeed = (float) config.getDouble(gun + ".projectileSpeed");
	    	
	    	//Shoot the gun
	    	if (ammoType.equalsIgnoreCase("arrow"))
	    	{
		    	Arrow shootArrow = player.launchProjectile(Arrow.class);
		    	shootArrow.setVelocity(player.getEyeLocation().getDirection().multiply(projectileSpeed));
		    	
		    	//Make the sound
		    	player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1.3F);
	    	}
	    	else
	    	{
	    		player.sendMessage(ChatColor.RED + "Incorrect configuration for gun: " + gun + " ammoType: " + ammoType 
	    				+ ". Please contact an administrator.");
	    		return;
	    	}
	    	
	    	//Remove from the item's durability
	    	if (currentDurability < 0)
	    		currentDurability = 0;
	    	float newDurability = currentDurability + maxDurability / shotsPerReload;
	    	player.getItemInHand().setDurability((short) (newDurability));
	    	
	    	//Set the cooldown.
	    	shootCooldown.put(player.getName(), System.currentTimeMillis());
	    }
		    
	    //Left-click -> reload
	    if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))
	    {
	    	player.sendMessage("Reload.");
	    	//To do: reload
	    }
	}
}
