package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by jcdesimp on 2/18/15.
 * Display the landlord map
 */
public class ShowMap implements LandlordCommand {

    private Landlord plugin;

    public ShowMap(Landlord plugin) {
        this.plugin = plugin;
    }

    /**
     * Toggles the land map display
     * @param sender who executed the command
     * @param args given with command
     * @return boolean
     */
    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        //mess ready
        String mapDisabled = "Land map is disabled.";
        String notPlayer = "This command can only be run by a player.";
        String noPerms = "You do not have permission.";
        String noMap = "Map unavailable.";

        if(!plugin.getConfig().getBoolean("options.enableMap", true)){      //conf
            sender.sendMessage(ChatColor.YELLOW+mapDisabled);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + notPlayer);
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.map")){
                player.sendMessage(ChatColor.RED+noPerms);
                return true;
            }
            try {
                plugin.getMapManager().toggleMap(player);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED+noMap); 
            }

        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        // only bother showing them this command if they have permission to do it.
        if(!sender.hasPermission("landlord.player.map") || !plugin.getConfig().getBoolean("options.enableMap",true)) {
            return null;
        }

        //mess ready
        String usage = "/#{label} #{cmd}"; // get the base usage string
        String desc = "Toggle the land map.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());

    }

    @Override
    public String[] getTriggers() {
        return new String[]{"map"}; //mess triggers
    }
}
