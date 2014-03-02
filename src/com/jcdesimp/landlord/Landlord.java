package com.jcdesimp.landlord;

import com.avaje.ebean.EbeanServer;
import com.lennardf1989.bukkitex.MyDatabase;
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





    /*
     * ***************************
     *       Database Stuff
     * ***************************
     */


    private void setupDatabase() {
        //Configuration config = getConfiguration();

        database = new MyDatabase(this) {
            protected java.util.List<Class<?>> getDatabaseClasses() {
                List<Class<?>> list = new ArrayList<Class<?>>();
                list.add(OwnedLand.class);
                list.add(Friend.class);

                return list;
            };
        };
        boolean rebuild = false;
        /*try {
            //getDatabase();
            //getDatabase().find(OwnedLand_Friend.class).findRowCount();
        } catch (PersistenceException ex) {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            //rebuild = true;
        }*/
        database.initializeDatabase(
                "org.sqlite.JDBC",
                "jdbc:sqlite:{DIR}{NAME}.db",
                "bukkit",
                "walrus",
                "SERIALIZABLE",
                false,
                rebuild
        );

        //config.setProperty("database.rebuild", false);

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
