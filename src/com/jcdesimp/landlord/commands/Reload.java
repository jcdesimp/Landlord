package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by jcdesimp on 2/19/15.
 * Administrative command to reload various resources/configuration files
 */
public class Reload implements LandlordCommand {

    private Landlord plugin;

    public Reload(Landlord plugin) {
        this.plugin = plugin;
    }

    /**
     * Reload landlord configuration file
     * @param sender who executed the command
     * @param args given with command
     * @param label exact command (or alias) run
     * @return boolean of success
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {

        //mess ready
        String configReloaded = "Landlord config reloaded.";
        String noPerms = "You do not have permission.";

        if(sender.hasPermission("landlord.admin.reload")){
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN+configReloaded);
            return true;
        }
        sender.sendMessage(ChatColor.RED+noPerms);    
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        if(!sender.hasPermission("landlord.admin.list")){
            return null;
        }

        //mess ready
        String usage = "/#{label} #{cmd}"; // get the base usage string
        String desc = "Reloads the Landlord config file.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());
    }

    @Override
    public String[] getTriggers() {
        return new String[]{"reload"};      //mess triggers
    }
}
