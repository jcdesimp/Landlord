package com.jcdesimp.landlord.commands;

import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by jcdesimp on 2/19/15.
 * Command to view info about the current land.
 */
public class Info implements LandlordCommand {

    private Landlord plugin;

    public Info(Landlord plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args, String label) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player.");   //mess
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.info")){
                player.sendMessage(ChatColor.RED+"You do not have permission.");        //mess
                return true;
            }
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            String owner = ChatColor.GRAY + "" + ChatColor.ITALIC + "None";     //mess
            if( land != null ){

                /*
                 * *************************************
                 * mark for possible change    !!!!!!!!!
                 * *************************************
                 */
                owner = ChatColor.GOLD + land.getOwnerUsername();
            } else {
                land = OwnedLand.landFromProperties(null,currChunk);
            }

            if(plugin.getConfig().getBoolean("options.particleEffects")){
                land.highlightLand(player, Effect.LAVADRIP);
            }

            //mess
            String msg = ChatColor.DARK_GREEN + "--- You are in chunk " + ChatColor.GOLD + "(" + currChunk.getX() + ", " + currChunk.getZ() + ") " +
                    ChatColor.DARK_GREEN + " in world \"" + ChatColor.GOLD + currChunk.getWorld().getName()  + ChatColor.DARK_GREEN + "\"\n"+ "----- Owned by: " +
                    owner;
            player.sendMessage(msg);

        }
        return true;
    }

    @Override
    public String getHelpText(CommandSender sender) {

        if(!sender.hasPermission("landlord.player.info")){   // make sure player has permission to do this command
            return null;
        }

        //mess ready
        String usage = "/#{label} #{cmd}"; // get the base usage string
        String desc = "View info about this chunk.";   // get the description

        // return the constructed and colorized help string
        return Utils.helpString(usage, desc, getTriggers()[0].toLowerCase());
    }

    @Override
    public String[] getTriggers() {
        return new String[]{"info"};
    }
}
