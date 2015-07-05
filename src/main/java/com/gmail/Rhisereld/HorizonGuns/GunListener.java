package com.gmail.Rhisereld.HorizonGuns;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GunListener implements Listener
{	
	//Right-click -> fire
	//Left-click -> reload
	@EventHandler
	public void gun(PlayerInteractEvent event) 
	{
		final Player player = event.getPlayer();
		
		//Right-click -> fire
	    if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) 
	    {
	    	player.sendMessage("Fire gun.");
	    	//To do: fire gun
	    }
		    
	    //Left-click -> reload
	    if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))
	    {
	    	player.sendMessage("Reload.");
	    	//To do: reload
	    }
	}
}
