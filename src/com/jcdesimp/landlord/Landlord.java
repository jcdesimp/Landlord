package com.jcdesimp.landlord;

import com.avaje.ebean.EbeanServer;
import com.lennardf1989.bukkitex.MyDatabase;
import net.milkbowl.vault.Vault;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 *
 * Main plugin class for Landlord
 *
 */
public final class Landlord extends JavaPlugin {

    private MyDatabase database;
    private Landlord plugin;
    private MapManager mapManager = new MapManager();
    private WorldguardHandler wgHandler;
    private VaultHandler vHandler;



    @Override
    public void onEnable() {
        plugin = this;
        //listner = new LandListener();
        getServer().getPluginManager().registerEvents(new LandListener(this), this);
        getServer().getPluginManager().registerEvents(mapManager, this);
        //Generates new config file if not present
        saveDefaultConfig();
        setupDatabase();
        //getLogger().info(getDescription().getName() + ": Created by Jcdesimp");
        getLogger().info("Created by Jcdesimp!");



        // Command Executor
        getCommand("landlord").setExecutor(new LandlordCommandExecutor(this));

        //Worldguard Check
        if(!hasWorldGuard() && this.getConfig().getBoolean("worldguard.blockRegionClaim", true)){
            getLogger().warning("Worldguard not found, worldguard features disabled.");
        } else {
            getLogger().info("Worldguard found!");
            wgHandler = new WorldguardHandler(getWorldGuard());
        }

        //Vault Check
        if(!hasVault() && this.getConfig().getBoolean("economy.enable", true)){
            getLogger().warning("Vault not found, economy features disabled.");
        } else {
            getLogger().info("Vault found!");
            vHandler = new VaultHandler();
            if(!vHandler.hasEconomy()){
                getLogger().warning("No economy found, economy features disabled.");
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
        mapManager.removeAllMaps();
    }


    public MapManager getMapManager() {
        return mapManager;
    }

    public static Plugin getInstance() {
        return Bukkit.getPluginManager().getPlugin("Landlord");
        //return Bukkit.getPluginManager().getPlugin("MyPlugin");
    }



    /*
     * ***************************
     *      Dependency Stuff
     * ***************************
     */


    /*
     * **************
     *   Worldguard
     * **************
     */
    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }


    /**
     * Provides access to the Landlord WorldGuardHandler
     * @return ll wg handler
     */
    public WorldguardHandler getWgHandler(){
        return wgHandler;
    }

    public boolean hasWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin) || !this.getConfig().getBoolean("worldguard.blockRegionClaim", true)) {
            return false;
        }

        return true;
    }

    /*
     * **************
     *     Vault
     * **************
     */

    public boolean hasVault(){
        Plugin plugin = getServer().getPluginManager().getPlugin("Vault");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof Vault) || !this.getConfig().getBoolean("economy.enable", true)) {
            return false;
        }

        return true;
    }

    public VaultHandler getvHandler(){
        return vHandler;
    }



    /*
     * ***************************
     *       Database Stuff
     * ***************************
     */


    private void setupDatabase() {
        Configuration config = getConfig();

        database = new MyDatabase(this) {
            protected java.util.List<Class<?>> getDatabaseClasses() {
                List<Class<?>> list = new ArrayList<Class<?>>();
                list.add(OwnedLand.class);
                list.add(Friend.class);

                return list;
            };
        };

        database.initializeDatabase(
                config.getString("database.driver","org.sqlite.JDBC"),
                config.getString("database.url","jdbc:sqlite:{DIR}{NAME}.db"),
                config.getString("database.username","bukkit"),
                config.getString("database.password","walrus"),
                config.getString("database.isolation","SERIALIZABLE"),
                config.getBoolean("database.logging", false),
                config.getBoolean("database.rebuild", false)
        );

        config.set("database.rebuild", false);

    }

    /*@Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(OwnedLand.class);
        list.add(Friend.class);
        //list.add(OwnedLand_Friend.class);
        return list;
    }*/

    @Override
    public EbeanServer getDatabase() {
        return database.getDatabase();
    }


}
