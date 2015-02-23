package com.jcdesimp.landlord.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;

/**
 * File created by jcdesimp on 4/8/14.
 */
public class ConfigAccessor {

    HashMap<String, FileConfiguration> configs;

    JavaPlugin plugin;


    public ConfigAccessor(JavaPlugin plugin) {

        this.plugin = plugin;


    }

    public void registerConfig(String name, String defaultpath, String destPath) {


        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(plugin.getResource("testConf.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            try {
                defConfig.save(new File(plugin.getDataFolder(), destPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void reloadAll() {
        for (String key : configs.keySet()) {
            //todo reload each key
        }
    }

    public Boolean reloadByName(String configName) {
        if (!configs.containsKey(configName)) {
            return false;
        }

        //todo reload the specified config

        return true;

    }





    //To be implemented
}
