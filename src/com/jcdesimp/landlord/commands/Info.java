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

        //mess ready
        String notPlayer = "This command can only be run by a player.";
        String noPerms = "You do not have permission.";
        String noOwner = "None";
        String landInfoString = "You are in chunk #{chunkCoords} in world #{worldName} ";
        String landOwnerString = "Owned by: #{ownerName}";

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + notPlayer);
        } else {
            Player player = (Player) sender;
            if(!player.hasPermission("landlord.player.info")){
                player.sendMessage(ChatColor.RED+noPerms);
                return true;
            }
            Chunk currChunk = player.getLocation().getChunk();
            OwnedLand land = OwnedLand.getLandFromDatabase(currChunk.getX(), currChunk.getZ(), currChunk.getWorld().getName());
            String owner = ChatColor.GRAY + "" + ChatColor.ITALIC + noOwner;
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

            // Build the land info string
            String msg = ChatColor.DARK_GREEN + "--- " + landInfoString
                    .replace("#{chunkCoords}",(ChatColor.GOLD + "(" + currChunk.getX() + ", " + currChunk.getZ() + ")" + ChatColor.DARK_GREEN))
                    .replace("#{worldName}", ChatColor.GOLD + "\"" + currChunk.getWorld().getName()  + "\"") +

                    ChatColor.DARK_GREEN + "-----\n" + landOwnerString.replace("#{ownerName}", owner);
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
        return new String[]{"info"};    //mess triggers
    }
}
