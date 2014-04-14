package com.jcdesimp.landlord.landManagement;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.DBVersion;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * File created by jcdesimp on 4/11/14.
 */
public class FlagManager {
    HashMap<String, Landflag> registeredFlags;
    Landlord plugin;

    public FlagManager(Landlord plugin) {
        this.registeredFlags = new HashMap<String, Landflag>();
        this.plugin = plugin;

    }

    public HashMap<String, Landflag> getRegisteredFlags() {
        return registeredFlags;
    }

    public boolean registerFlag(Landflag f) {
        if(registeredFlags.containsKey(f.getClass().getSimpleName())) {
            plugin.getLogger().warning("Could not register flag \""+f.getClass().getSimpleName()+"\" because a flag is already registered with that name!");
            f.setUniqueName(f.getClass().getSimpleName());

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

    public int getFlagPermSLot(){
        //plugin.getDatabase().find(DBVersion.class).where()
        return 0;
    }

}

