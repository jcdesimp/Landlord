package com.jcdesimp.landlord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Command Executor class for LandLord
 */
public class LandlordCommandExecutor implements CommandExecutor {
    private Landlord plugin; //pointer to main class

    public LandlordCommandExecutor(Landlord plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("land")){ // If the player typed /land then do the following...
            plugin.getLogger().info("/land has been executed!");
            return false;
        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }

}
