package com.gmail.Rhisereld.HorizonGuns;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GunListener implements Listener
{	
	JavaPlugin plugin;
	static FileConfiguration config;
	
	HashMap<String,Long> shootCooldown = new HashMap<String,Long>();
	
	public GunListener(JavaPlugin plugin)
	{
		this.plugin = plugin;
		GunListener.config = plugin.getConfig();
	}
	
	public static void updateConfig(FileConfiguration config)
	{
		GunListener.config = config;
	}
	
	//Right-click -> fire
	//Left-click -> reload
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRightClick(PlayerInteractEvent event) 
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
	    	if (shootCooldown.containsKey(player.getName()) && System.currentTimeMillis() < shootCooldown.get(player.getName()))
	    	{
	    		//Clicking shouldn't do anything else.
		    	event.setCancelled(true);
	    		return;
	    	}

	    	shootCooldown.remove(player.getName());
	    	
	    	//If player's ammo is 0, tell them to reload.
	    	float maxDurability = player.getItemInHand().getType().getMaxDurability();
	    	float currentDurability = player.getItemInHand().getDurability();
	    	int shotsPerReload = config.getInt(gun + ".shotsPerReload");
    		int cooldown = config.getInt(gun + ".cooldown");
	    	if (currentDurability > maxDurability - 8)
	    	{
	    		player.sendMessage(ChatColor.RED + "*click*");
	    		shootCooldown.put(player.getName(), System.currentTimeMillis() + cooldown);
	    		
	    		//Clicking shouldn't do anything else.
		    	event.setCancelled(true);
	    		return;
	    	}
	    	
	    	//Get configuration for gun
	    	String ammoType = config.getString(gun + ".ammoType");
	    	float projectileSpeed = (float) config.getDouble(gun + ".projectileSpeed");
	    	
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
	    		
	    		//Clicking shouldn't do anything else.
		    	event.setCancelled(true);
	    		return;
	    	}
	    	
	    	//Remove from the item's durability
	    	if (currentDurability < 0)
	    		currentDurability = 0;
	    	float newDurability = currentDurability + maxDurability / (float) shotsPerReload;
	    	player.getItemInHand().setDurability((short)(newDurability));
	    	
	    	//Set the cooldown.
	    	shootCooldown.put(player.getName(), System.currentTimeMillis() + cooldown);
	    	
	    	//Clicking shouldn't do anything else.
	    	event.setCancelled(true);
	    	return;
	    }
		    
	    //Left-click -> reload
	    if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))
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
	    	if (shootCooldown.containsKey(player.getName()) && System.currentTimeMillis() < shootCooldown.get(player.getName()))
	    	{
	    		//Clicking shouldn't do anything else.
		    	event.setCancelled(true);
	    		return;
	    	}

	    	shootCooldown.remove(player.getName());
	    	
	    	//Check if they have any ammo
	    	Material ammo;
	    	String ammoType = config.getString(gun + ".ammoType");
	    	if (ammoType.equalsIgnoreCase("arrow"))
	    		ammo = Material.ARROW;
	    	else
	    	{
	    		player.sendMessage(ChatColor.RED + "Incorrect configuration for gun: " + gun + " ammoType: " + ammoType 
	    				+ ". Please contact an administrator.");
	    		//Clicking shouldn't do anything else.
		    	event.setCancelled(true);
	    		return;
	    	}
	    	
			if (!player.getInventory().contains(ammo))
			{
				String ammoName = config.getString(gun + ".ammoName");
				if (ammoName == null)
					ammoName = ammoType;
				player.sendMessage(ChatColor.RED + "You have no ammunition left! This gun requires: " + ammoName);
				//Clicking shouldn't do anything else.
		    	event.setCancelled(true);
				return;
			}
			
	    	
			//If durability is already full don't do anything.
			double currentDurability = player.getItemInHand().getDurability();
			if (currentDurability <= 0)
				return;
			
			//Tell them they're reloading and set cooldown
	    	player.sendMessage(ChatColor.GOLD + "Reloading...");
	    	shootCooldown.put(player.getName(), System.currentTimeMillis() + config.getInt(gun + ".reloadCooldown"));
			
			//Remove one cell
			player.getInventory().removeItem(new ItemStack(ammo, 1));
			
			//Set durability to full
			player.getItemInHand().setDurability((short) 0);
						
			//Clicking shouldn't do anything else.
	    	event.setCancelled(true);
	    }  
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onArrowHit (EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Arrow)
	    {
	        Arrow arrow = (Arrow) event.getDamager();
	        if (arrow.getShooter() instanceof Player)
	        {
	        	Player shooter = (Player) arrow.getShooter();
	        	
	        	//Check if the player is holding any of the gun types specified in configuration.
		    	Set<String> guns = config.getKeys(false);
		    	String gun = null;
		    	
		    	for (String g: guns)
		    		if (shooter.getItemInHand().getType().toString().equalsIgnoreCase(g))
		    			gun = g;
		    	
		    	if (gun == null)
		    		return;
		    	
		    	event.setDamage(config.getDouble(gun + ".damage"));
	        }
	    }
	}
}
