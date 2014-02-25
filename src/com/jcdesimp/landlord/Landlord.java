package com.jcdesimp.landlord;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * Main plugin class for Landlord
 *
 */
public class Landlord extends JavaPlugin {
    public void onEnable() {
        getLogger().info(getDescription().getName() + " has been enabled!");
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }
}
