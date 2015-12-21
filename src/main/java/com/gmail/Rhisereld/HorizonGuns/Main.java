package com.gmail.Rhisereld.HorizonGuns;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{		
	public void onEnable()
	{	
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new GunListener(this), this);
		this.getCommand("checkammo").setExecutor(new HorizonCommandParser(this.getConfig()));
	}
	
	public void onDisable()
	{}
}
	