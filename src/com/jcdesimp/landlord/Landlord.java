package com.jcdesimp.landlord;

import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * Main plugin class for Landlord
 *
 */
public final class Landlord extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info(getDescription().getName() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

}
