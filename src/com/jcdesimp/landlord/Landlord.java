package com.jcdesimp.landlord;

import com.avaje.ebean.EbeanServer;
//import com.lennardf1989.bukkitex.MyDatabase;
import com.jcdesimp.landlord.landFlags.Build;
import com.jcdesimp.landlord.landManagement.FlagManager;
//import com.jcdesimp.landlord.landManagement.LandListener;
import com.jcdesimp.landlord.landMap.MapManager;
import com.jcdesimp.landlord.persistantData.*;
import com.jcdesimp.landlord.pluginHooks.VaultHandler;
import com.jcdesimp.landlord.pluginHooks.WorldguardHandler;
import net.milkbowl.vault.Vault;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import static org.bukkit.Bukkit.getOfflinePlayer;

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
    private FlagManager flagManager;



    @Override
    public void onEnable() {
        plugin = this;
        //listner = new LandListener();
        //getServer().getPluginManager().registerEvents(new LandListener(this), this);
        flagManager = new FlagManager(this);
        getServer().getPluginManager().registerEvents(mapManager, this);


        //// CONFIG FILE MANAGEMENT ///




        Map<String,Object> oldConfig = getConfig().getValues(true);
        //Generates new config file if not present
        saveDefaultConfig();
        String header = getConfig().options().header();
        FileConfiguration config = getConfig();


        // checks for missing entries and applies new ones
        for (Map.Entry<String, Object> entry : config.getDefaults().getValues(true).entrySet())
        {
            if(oldConfig.containsKey(entry.getKey())){
                config.set(entry.getKey(),oldConfig.get(entry.getKey()));
            } else {
                config.set(entry.getKey(), entry.getValue());
            }

        }

        saveConfig();

        ////////////////////////////////



        // Database creation, configuration, and maintenance.
        setupDatabase();
        //getLogger().info(getDescription().getName() + ": Created by Jcdesimp");
        getLogger().info("Created by Jcdesimp!");

        //Plugin Metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }



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

        verifyDatabaseVersion();


        //Register default flags
        flagManager.registerFlag(new Build());


    }

    @Override
    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
        mapManager.removeAllMaps();
    }


    public FlagManager getFlagManager() {
        return flagManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public static Landlord getInstance() {
        return (Landlord)Bukkit.getPluginManager().getPlugin("Landlord");
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
                list.add(DBVersion.class);
                list.add(LandFlagPerm.class);

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

    public void verifyDatabaseVersion() {
        int CURRENT_VERSION = 1;
        if (this.getDatabase().find(DBVersion.class).where().eq("identifier","version").findUnique() == null) {
            //Convert Database
            this.getLogger().info("Starting OwnedLand conversion...");
            List<OwnedLand> allLand = plugin.getDatabase().find(OwnedLand.class).findList();
            for (OwnedLand l : allLand){
                if(l.getOwnerName().length() < 32){
                    //plugin.getLogger().info("Converting "+ l.getId() + "...");
                        /*
                         * *************************************
                         * mark for possible change    !!!!!!!!!
                         * *************************************
                         */
                    if(getOfflinePlayer(l.getOwnerName()).hasPlayedBefore()) {
                        //plugin.getLogger().info("Converting "+ l.getId() + "... Owner: "+l.getOwnerName());
                        l.setOwnerName(getOfflinePlayer(l.getOwnerName()).getUniqueId().toString());
                        plugin.getDatabase().save(l);

                    } else {
                        //plugin.getLogger().info("Deleting "+ l.getId() + "! Owner: "+l.getOwnerName());
                        plugin.getDatabase().delete(l);
                    }


                }
            }
            this.getLogger().info("Land Conversion completed!");

            this.getLogger().info("Starting Friend conversion...");
            List<Friend> allFriends = plugin.getDatabase().find(Friend.class).findList();
            for (Friend f : allFriends){
                if(f.getPlayerName().length() < 32){
                    //plugin.getLogger().info("Converting "+ l.getId() + "...");
                        /*
                         * *************************************
                         * mark for possible change    !!!!!!!!!
                         * *************************************
                         */
                    if(getOfflinePlayer(f.getPlayerName()).hasPlayedBefore()) {
                        //plugin.getLogger().info("Converting "+ f.getId() + "... Name: "+f.getPlayerName());
                        f.setPlayerName(getOfflinePlayer(f.getPlayerName()).getUniqueId().toString());
                        plugin.getDatabase().save(f);

                    } else {
                        //plugin.getLogger().info("Deleting "+ f.getId() + "! Name: "+f.getPlayerName());
                        plugin.getDatabase().delete(f);
                    }


                }
            }
            this.getLogger().info("Friend Conversion completed!");
            this.getLogger().info("Starting Permission conversion...");
            allLand = plugin.getDatabase().find(OwnedLand.class).findList();
            for (OwnedLand l : allLand){
                if(l.getPermissions() != null) {


                    String[] currPerms = l.getPermissions().split("\\|");
                    String newPermString = "";
                    for (int i = 0; i < currPerms.length; i++) {
                        currPerms[i]=currPerms[i].substring(0,3);
                        newPermString += Integer.toString(Integer.parseInt(currPerms[i], 2));
                        if (i < currPerms.length - 1) {
                            newPermString += "|";
                        }

                    }
                    l.setPermissions(newPermString);
                    plugin.getDatabase().save(l);
                }
            }
            //Entries for legacy flags
            this.getDatabase().save(LandFlagPerm.flagPermFromData("Build",1));
            this.getDatabase().save(LandFlagPerm.flagPermFromData("HarmAnimals",2));
            this.getDatabase().save(LandFlagPerm.flagPermFromData("UseContainers",3));

            this.getLogger().info("Permission Conversion completed!");
            DBVersion vUpdate = new DBVersion();
            vUpdate.setIdentifier("version");
            vUpdate.setIntData(1);
            this.getDatabase().save(vUpdate);
        }
        int currVersion = this.getDatabase().find(DBVersion.class).where().eq("identifier","version").findUnique().getIntData();
        if(currVersion < CURRENT_VERSION){
            this.getLogger().info("Database outdated!");
        }

    }


}
