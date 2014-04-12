package com.jcdesimp.landlord.landManagement;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * File created by jcdesimp on 4/11/14.
 */
public class FlagManager {
    HashMap<String, Landflag> registeredFlags;
    JavaPlugin plugin;

    public FlagManager(JavaPlugin plugin) {
        this.registeredFlags = new HashMap<String, Landflag>();
        this.plugin = plugin;

    }

    public HashMap<String, Landflag> getRegisteredFlags() {
        return registeredFlags;
    }

    public boolean registerFlag(Landflag f) {
        if(registeredFlags.containsKey(f.getClass().getSimpleName())) {
            plugin.getLogger().warning("Could not register flag \""+f.getClass().getSimpleName()+"\" because a flag is already registered with that name!");
            return false;
        }

        try {
            plugin.getServer().getPluginManager().registerEvents(f, plugin);
            registeredFlags.put(f.getClass().getSimpleName(), f);
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Error occured while registering flag \""+f.getClass().getSimpleName()+"\":");
            e.printStackTrace();
            return false;
        }




    }

}

