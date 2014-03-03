package com.jcdesimp.landlord;

import com.avaje.ebean.EbeanServer;
import com.lennardf1989.bukkitex.MyDatabase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Main plugin class for Landlord
 *
 */
public final class Landlord extends JavaPlugin {

    private MyDatabase database;
    private static Landlord plugin;

    @Override
    public void onEnable() {
        plugin = this;
        setupDatabase();
        getLogger().info(getDescription().getName() + " has been enabled!");

        //Generates new config file if not present
        saveDefaultConfig();

        // Command Executor
        getCommand("landlord").setExecutor(new LandlordCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }




    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(Landlord.class);
        //return Bukkit.getPluginManager().getPlugin("MyPlugin");
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
                config.getBoolean("database.rebuild", true)
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
