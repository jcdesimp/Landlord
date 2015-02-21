package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by jcdesimp on 2/19/15.
 * Base landlord command, no args.
 * Will display basic info about the plugin
 *
 * Command is never technically registered
 */
public class Base implements LandlordCommand {

    /**
     * Called when base command /landlord or aliases (/ll /land)
     * are executed with no parameters
     *
     * @param sender who executed the command
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        sender.sendMessage(ChatColor.DARK_GREEN + "--|| Landlord v"+ Landlord.getInstance().getDescription().getVersion() +
                " Created by " + ChatColor.BLUE+"Jcdesimp "+ChatColor.DARK_GREEN +"||--\n"+
                //ChatColor.GRAY+"(Aliases: /landlord, /land, or /ll)\n"+
                ChatColor.DARK_GREEN+"Type " +ChatColor.YELLOW+"/"+label+" help "+ChatColor.DARK_GREEN +"for a list of commands");
        return true;
    }

    /**
     * No help text since base command
     * @return  an empty string
     * @param sender
     */
    @Override
    public String getHelpText(CommandSender sender) {
        return "";
    }


    /**
     * No aliases for this since base command
     * @return  an empty string array
     */
    @Override
    public String[] getTriggers() {
        return new String[0];
    }
}
