package com.gmail.Rhisereld.HorizonGuns;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GunListener implements Listener
{	
	String GUN_TYPE = "WOOD_SPADE";
	String AMMO_TYPE = "ARROW";
	int COOLDOWN = 10;
	int RELOAD_COOLDOWN = 20;
	int SHOTS_PER_RELOAD = 8;
	int DAMAGE = 2;
	float PROJECTILE_SPEED = 2;
	
	//Right-click -> fire
	//Left-click -> reload
	@EventHandler
	public void gun(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		
		//Right-click -> fire
	    if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
	    		&& player.getItemInHand().getType().toString().equals(GUN_TYPE)) 
	    {   	
	    	Arrow shootArrow = player.launchProjectile(Arrow.class);
	    	shootArrow.setVelocity(player.getEyeLocation().getDirection().multiply(PROJECTILE_SPEED));
	    }
		    
	    //Left-click -> reload
	    if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))
	    {
	    	player.sendMessage("Reload.");
	    	//To do: reload
	    }
	}
}
